package vswe.stevesvehicles.client.rendering;
import java.util.ArrayList;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vswe.stevesvehicles.client.rendering.models.ModelVehicle;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.client.IconData;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.tank.Tank;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.vehicle.entity.IVehicleEntity;

public abstract class RendererVehicle extends Render {
    public RendererVehicle() {
        this.shadowSize = 0.5F;
    }

    public class MatrixObject {
        public double x;
        public double y;
        public double z;
        public float yaw;
        public float pitch;
        public float roll;
        public boolean flip;

        public MatrixObject(double x, double y, double z, float yaw) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
        }
    }

    public void renderVehicle(VehicleBase vehicle, double x, double y, double z, float yaw, float partialTickTime) {
		if (vehicle.getModules() != null) {
			for (ModuleBase module : vehicle.getModules()) {
				if (!module.shouldVehicleRender()) {
					return;
				}
			}
		}
	


		GL11.glPushMatrix();

        MatrixObject matrix = new MatrixObject(x, y, z, yaw);
        applyMatrixUpdates(vehicle, matrix, partialTickTime);

		//apply the rotations
        GL11.glTranslatef((float)matrix.x, (float)matrix.y, (float)matrix.z);

		GL11.glRotatef(matrix.yaw, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(matrix.pitch, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(matrix.roll, 1.0F, 0.0F, 0.0F);
		matrix.yaw += matrix.flip ? 180 : 0;
		GL11.glRotatef(matrix.flip ? 180 : 0, 0.0F, 1.0F, 0.0F);

		//render the cart's models
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		renderModels(vehicle, (float)(Math.PI * matrix.yaw / 180F), matrix.pitch, matrix.roll, 0.0625F, partialTickTime);

		GL11.glPopMatrix();

		//render any labels above the cart
		renderLabel(vehicle, matrix.x, matrix.y, matrix.z);
		
		
    }

    protected abstract void applyMatrixUpdates(VehicleBase vehicle, MatrixObject matrix, float partialTickTime);

	public void renderModels(VehicleBase vehicle, float yaw, float pitch, float roll, float multiplier, float partialTickTime) {
		if (vehicle.getModules() != null) {
			for (ModuleBase module : vehicle.getModules()) {
				if (module.haveModels()) {
					for (ModelVehicle model : module.getModels()) {
						model.render(module, yaw, pitch, roll, multiplier, partialTickTime);
					}
				}
			}
		}
	}

	public static void renderLiquidCuboid(FluidStack liquid, int tankSize, float x, float y, float z, float sizeX, float sizeY, float sizeZ, float multiplier) {
		IconData data = Tank.getIconAndTexture(liquid);
		
		if (data == null || data.getIcon() == null) {
			return;
		}
		
		if (liquid.amount > 0) {
		
			float filled = liquid.amount / (float)tankSize;
			
	        GL11.glPushMatrix();
	        GL11.glTranslatef(x * multiplier, (y + sizeY * (1 - filled) / 2) * multiplier , z * multiplier);
			
	        ResourceHelper.bindResource(data.getResource());
	        Tank.applyColorFilter(liquid);
	        float scale = 0.5F;
	        GL11.glScalef(scale, scale, scale);
			GL11.glDisable(GL11.GL_LIGHTING);
			multiplier /= scale;
			renderCuboid(data.getIcon(), sizeX * multiplier, sizeY * multiplier * filled, sizeZ * multiplier);
			GL11.glEnable(GL11.GL_LIGHTING);		
	        GL11.glDisable(GL11.GL_BLEND);
	        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	        GL11.glPopMatrix();		
	        
	        GL11.glColor4f(1F, 1F, 1F, 1F);
		}
	}
	
	private static void renderCuboid(IIcon icon, double sizeX, double sizeY, double sizeZ) {
		renderFace(icon, sizeX, sizeZ, 0, 		90F, 	0F, 					-(float)(sizeY / 2),	0F					);
		renderFace(icon, sizeX, sizeZ, 0, 		-90F, 	0F, 					(float)(sizeY / 2), 	0F					);
		renderFace(icon, sizeX, sizeY, 0, 		0, 		0F, 					0F, 					(float)(sizeZ / 2)	);
		renderFace(icon, sizeX, sizeY, 180F, 	0F, 	0F, 					0F, 					-(float)(sizeZ / 2)	);	
		renderFace(icon, sizeZ, sizeY, 90F, 	0, 		(float)(sizeX / 2), 	0F, 					0F					);
		renderFace(icon, sizeZ, sizeY, -90F, 	0F, 	-(float)(sizeX / 2), 	0F, 					0F					);		
	}
	
	
	private static void renderFace(IIcon icon,  double totalTargetW, double totalTargetH, float yaw, float roll, float offX, float offY, float offZ) {
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
	
	
	 protected void renderLabel(VehicleBase vehicle, double x, double y, double z) {
		ArrayList<String> labels = vehicle.getLabel();

		if (labels != null && labels.size() > 0) {
			float distance = vehicle.getEntity().getDistanceToEntity(this.renderManager.livingPlayer);

			if (distance <= 64F) {
					//place everything where it belongs
					FontRenderer fontRenderer = this.getFontRendererFromRenderManager();
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

					int boxWidth = 0;
					int boxHeight = 0;
					for (String label : labels) {
						boxWidth = Math.max(boxWidth, fontRenderer.getStringWidth(label));
						boxHeight += fontRenderer.FONT_HEIGHT;
					}

					int halfW = boxWidth / 2;
					int halfH = boxHeight / 2;

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
						fontRenderer.drawString(label, -fontRenderer.getStringWidth(label) / 2, yPos, 553648127/*A=32,R=255,G=255,B=255*/);
						yPos += fontRenderer.FONT_HEIGHT;
					}
					GL11.glEnable(GL11.GL_DEPTH_TEST);
					GL11.glDepthMask(true);
					yPos = -halfH;
					for (String label : labels) {
						fontRenderer.drawString(label, -fontRenderer.getStringWidth(label) / 2, yPos, -1);
						yPos += fontRenderer.FONT_HEIGHT;
					}
					GL11.glEnable(GL11.GL_LIGHTING);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glPopMatrix();
			}
		}
    }

	 @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity) {
        return null;
    }	 

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime) {
        /*
            The yaw received as a parameter is defined as:
            float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTickTime;

            However, if the previous angle was 180 and the current is -180, those are exactly the same angle but the
            above algorithm thinks there's a difference of 360 which will cause that update tick to have an entity
            spinning out of control. This will just last for the frames for that tick which will make the entity
            flash in wrong angles (unless the difference is wrongly calculated multiple ticks in a row).

            This is why the supplied yaw is disregarded, this might however cause problems when the yaw specified is
            supposed to be something completely different than the actual one, for example when being drawn in
            interfaces. This is why the supplied yaw is actually used if the difference between the current and previous
            yaw rotations are big enough.
         */

        if (Math.abs(entity.rotationYaw - entity.prevRotationYaw) >= 1) {
            float currentYaw = fitAngleWithin360(entity.rotationYaw);
            float previousYaw = fitAngleWithin360(entity.prevRotationYaw);


            float normalDifference = currentYaw - previousYaw;
            float bestDifference = normalDifference;
            for (int i = -360; i <= 360; i += 360) {
                float difference = normalDifference + i;
                if (Math.abs(difference) < Math.abs(bestDifference)) {
                    bestDifference = difference;
                }
            }

            yaw = fitAngleWithin360(previousYaw + bestDifference * partialTickTime);
        }

        this.renderVehicle(((IVehicleEntity) entity).getVehicle(), x, y, z, yaw, partialTickTime);
    }

    private float fitAngleWithin360(float angle) {
        angle %= 360;
        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }
}
