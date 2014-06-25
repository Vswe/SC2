package vswe.stevescarts.old.Renders;
import java.util.ArrayList;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.Helpers.IconData;
import vswe.stevescarts.old.Helpers.ResourceHelper;
import vswe.stevescarts.old.Helpers.Tank;
import vswe.stevescarts.old.Models.Cart.ModelCartbase;
import vswe.stevescarts.modules.ModuleBase;

public class RendererMinecart extends Render
{
    public RendererMinecart()
    {
        this.shadowSize = 0.5F;
    }

    public void renderCart(EntityModularCart cart, double x, double y, double z, float yaw, float partialTickTime)
    {
		if (cart.getModules() != null) {
			for (ModuleBase module : cart.getModules()) {
				if (!module.shouldCartRender()) {
					return;
				}
			}
		}
	


		GL11.glPushMatrix();

		//calculate the current positions and the current pitch(since the cart should still be rendered between ticks)
        double partialPosX = cart.lastTickPosX + (cart.posX - cart.lastTickPosX) * (double)partialTickTime;
        double partialPosY = cart.lastTickPosY + (cart.posY - cart.lastTickPosY) * (double)partialTickTime;
        double partialPosZ = cart.lastTickPosZ + (cart.posZ - cart.lastTickPosZ) * (double)partialTickTime;
        float partialRotPitch = cart.prevRotationPitch + (cart.rotationPitch - cart.prevRotationPitch) * partialTickTime;

		/*Vec3 rotations = engine.getRenderRotation((float)partialPosX,(float)partialPosY,(float)partialPosZ,partialTickTime);
		if (rotations != null) {
			yaw = (float)rotations.xCoord * 180F / (float)Math.PI;
		}*/

        Vec3 posFromRail = cart.func_70489_a(partialPosX, partialPosY, partialPosZ);

		//if cart is on a rail the yaw and the pitch should be calculated accordingly(instead of just use given values)
        if (posFromRail != null && cart.canUseRail())
        {
			//predict the last and next position of the cart with the given prediction time span
			double predictionLength = 0.30000001192092896D;
            Vec3 lastPos = cart.func_70495_a(partialPosX, partialPosY, partialPosZ, predictionLength);
            Vec3 nextPos = cart.func_70495_a(partialPosX, partialPosY, partialPosZ, -predictionLength);

			//if the last pos wasn't on the rail
            if (lastPos == null)
            {
                lastPos = posFromRail;
            }

			//if the next pos won't be on the rail
            if (nextPos == null)
            {
                nextPos = posFromRail;
            }

			//fix the coordinates accordingly to the rail
            x += posFromRail.xCoord - partialPosX;
            y += (lastPos.yCoord + nextPos.yCoord) / 2.0D - partialPosY;
            z += posFromRail.zCoord - partialPosZ;

			//get the difference beetween the next and the last pos
            Vec3 difference = nextPos.addVector(-lastPos.xCoord, -lastPos.yCoord, -lastPos.zCoord);

			//if there exist any difference
            if (difference.lengthVector() != 0.0D)
            {
                difference = difference.normalize();

				//calculate the yaw and the pitch
                yaw = (float)(Math.atan2(difference.zCoord, difference.xCoord) * 180.0D / Math.PI);
                partialRotPitch = (float)(Math.atan(difference.yCoord) * 73.0D);
            }
        }

		yaw = 180F - yaw;
		partialRotPitch *= -1;

		//calculate and apply the rotation caused by the cart being damaged
        float damageRot = (float)cart.getRollingAmplitude() - partialTickTime;
        float damageTime = (float)cart.getDamage() - partialTickTime;
		float damageDir = (float)cart.getRollingDirection();

        if (damageTime < 0.0F)
        {
            damageTime = 0.0F;
        }

		boolean flip = (cart.motionX > 0) != (cart.motionZ > 0);
		if (cart.cornerFlip) {
			flip = !flip;
		}

		if (cart.getRenderFlippedYaw(yaw + (flip ? 0F : 180F)) ) {
			flip = !flip;
		}

		//apply the rotations
        GL11.glTranslatef((float)x, (float)y, (float)z);

		GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(partialRotPitch, 0.0F, 0.0F, 1.0F);
		if (damageRot > 0.0F)
        {
            damageRot = MathHelper.sin(damageRot) * damageRot * damageTime / 10.0F * damageDir;
			GL11.glRotatef(damageRot, 1.0F, 0.0F, 0.0F);
        }
		yaw += flip ? 0F : 180F;
		GL11.glRotatef(flip ? 0F : 180F, 0.0F, 1.0F, 0.0F);

		//render the cart's models
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		renderModels(cart, (float)(Math.PI * yaw / 180F), partialRotPitch, damageRot, 0.0625F, partialTickTime);

		GL11.glPopMatrix();

		//render any labels above the cart
		renderLabel(cart,x,y,z);
		
		
    }

	public void renderModels(EntityModularCart cart, float yaw, float pitch, float roll, float mult, float partialtime) {
		if (cart.getModules() != null) {
			for (ModuleBase module : cart.getModules()) {
				if (module.haveModels()) {
					for (ModelCartbase model : module.getModels()) {
						model.render(this, module, yaw, pitch, roll, mult, partialtime);
					}
				}
			}
		}
	}

	public void renderLiquidCuboid(FluidStack liquid, int tankSize, float x, float y, float z, float sizeX, float sizeY, float sizeZ, float mult) {


		
		IconData data = Tank.getIconAndTexture(liquid);
		
		if (data == null || data.getIcon() == null) {
			return;
		}
		
		if (liquid.amount > 0) {
		
			float filled = liquid.amount / (float)tankSize;
			
	        GL11.glPushMatrix();
	        GL11.glTranslatef(x * mult, (y + sizeY * (1 - filled) / 2) * mult , z * mult);
			
	        ResourceHelper.bindResource(data.getResource());
	        Tank.applyColorFilter(liquid);
	        float scale = 0.5F;
	        GL11.glScalef(scale, scale, scale);
			GL11.glDisable(GL11.GL_LIGHTING);
			mult /= scale;
			renderCuboid(data.getIcon(), sizeX * mult, sizeY * mult * filled, sizeZ * mult);
			GL11.glEnable(GL11.GL_LIGHTING);		
	        GL11.glDisable(GL11.GL_BLEND);
	        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	        GL11.glPopMatrix();		
	        
	        GL11.glColor4f(1F, 1F, 1F, 1F);
		}
	}
	
	private void renderCuboid(IIcon icon, double sizeX, double sizeY, double sizeZ) {
		renderFace(icon, sizeX, sizeZ, 0, 		90F, 	0F, 					-(float)(sizeY / 2),	0F					);
		renderFace(icon, sizeX, sizeZ, 0, 		-90F, 	0F, 					(float)(sizeY / 2), 	0F					);
		renderFace(icon, sizeX, sizeY, 0, 		0, 		0F, 					0F, 					(float)(sizeZ / 2)	);
		renderFace(icon, sizeX, sizeY, 180F, 	0F, 	0F, 					0F, 					-(float)(sizeZ / 2)	);	
		renderFace(icon, sizeZ, sizeY, 90F, 	0, 		(float)(sizeX / 2), 	0F, 					0F					);
		renderFace(icon, sizeZ, sizeY, -90F, 	0F, 	-(float)(sizeX / 2), 	0F, 					0F					);		
	}
	
	
	private void renderFace(IIcon icon,  double totalTargetW, double totalTargetH, float yaw, float roll, float offX, float offY, float offZ) {
        GL11.glPushMatrix();

		GL11.glTranslatef(offX, offY, offZ);
		
		GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(roll, 1.0F, 0.0F, 0.0F);
		
		Tessellator tessellator = Tessellator.instance;
        double srcX = icon.getMinU();
		double srcY = icon.getMinV();
		
        double srcW = icon.getMaxU() - srcX;
        double srcH = icon.getMaxV() - srcY;		
		
		double d = 0.001D;
		double currentTargetX = 0D;
		while (totalTargetW - currentTargetX > d * 2) {
			double currentTargetW = Math.min(totalTargetW - currentTargetX, 1D);
			double currentTargetY = 0D;
			while (totalTargetH - currentTargetY > d * 2) {
				double currentTargetH = Math.min(totalTargetH - currentTargetY, 1D);
			
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, 1.0F, 0.0F);
				tessellator.addVertexWithUV(currentTargetX - totalTargetW / 2, 						currentTargetY - totalTargetH / 2, 						0D, 	srcX,							srcY							);
				tessellator.addVertexWithUV(currentTargetX + currentTargetW - totalTargetW / 2, 	currentTargetY  - totalTargetH / 2, 					0D, 	srcX + srcW * currentTargetW,	srcY							);
				tessellator.addVertexWithUV(currentTargetX + currentTargetW - totalTargetW / 2, 	currentTargetY + currentTargetH  - totalTargetH / 2, 	0D, 	srcX + srcW * currentTargetW, 	srcY + srcH * currentTargetH	);
				tessellator.addVertexWithUV(currentTargetX - totalTargetW / 2, 						currentTargetY + currentTargetH  - totalTargetH / 2, 	0D, 	srcX, 							srcY + srcH * currentTargetH	);
				tessellator.draw();
			
				currentTargetY += currentTargetH - d;
			}
			currentTargetX += currentTargetW  - d;
		}	
		GL11.glPopMatrix();	
	}
	
	
	 protected void renderLabel(EntityModularCart cart, double x, double y, double z)
    {
		ArrayList<String> labels = cart.getLabel();

		if (labels != null && labels.size() > 0) {
			float distance = cart.getDistanceToEntity(this.renderManager.livingPlayer);

			if (distance <= 64F)
			{
					//place everything where it belongs
					FontRenderer frend = this.getFontRendererFromRenderManager();
					float var12 = 1.6F;
					float var13 = 0.016666668F * var12;
					GL11.glPushMatrix();
					GL11.glTranslatef((float)x + 0.0F, (float)y + 1F + (labels.size() - 1) * 0.12F, (float)z);
					GL11.glNormal3f(0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
					GL11.glScalef(-var13, -var13, var13);
					GL11.glDisable(GL11.GL_LIGHTING);
					GL11.glDepthMask(false);
					GL11.glDisable(GL11.GL_DEPTH_TEST);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

					int boxwidth = 0;
					int boxheight = 0;
					for (String label : labels) {
						boxwidth = Math.max(boxwidth, frend.getStringWidth(label));
						boxheight += frend.FONT_HEIGHT;
					}

					int halfW = boxwidth / 2;
					int halfH = boxheight / 2;

					//background
					Tessellator tes = Tessellator.instance;
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					tes.startDrawingQuads();
					tes.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
					tes.addVertex((double)(-halfW - 1), (double)(-halfH - 1), 0.0D);
					tes.addVertex((double)(-halfW - 1), (double)(halfH + 1), 0.0D);
					tes.addVertex((double)(halfW + 1), (double)(halfH + 1), 0.0D);
					tes.addVertex((double)(halfW + 1), (double)(-halfH - 1), 0.0D);
					tes.draw();

					//the text
					GL11.glEnable(GL11.GL_TEXTURE_2D);
					int yPos = -halfH;
					for (String label : labels) {
						frend.drawString(label, -frend.getStringWidth(label) / 2, yPos, 553648127/*A=32,R=255,G=255,B=255*/);
						yPos += frend.FONT_HEIGHT;
					}
					GL11.glEnable(GL11.GL_DEPTH_TEST);
					GL11.glDepthMask(true);
					yPos = -halfH;
					for (String label : labels) {
						frend.drawString(label, -frend.getStringWidth(label) / 2, yPos, -1);
						yPos += frend.FONT_HEIGHT;
					}
					GL11.glEnable(GL11.GL_LIGHTING);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glPopMatrix();
			}
		}
    }

	 @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return null;
    }	 
	 
    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double x, double y, double z, float yaw, float partialTickTime)
    {
        this.renderCart((EntityModularCart)par1Entity, x, y, z, yaw, partialTickTime);
    }
}
