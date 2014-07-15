package vswe.stevesvehicles.client.rendering.models;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.common.attachment.ModuleDynamite;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelDynamite extends ModelVehicle {

	private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/models/tntModel.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return TEXTURE;
	}		
	
    @Override
	public float extraMultiplier() {
		return 0.25F;
	}

	private ModelRenderer anchor;
	private ModelRenderer[] dynamites;
    public ModelDynamite() {
		anchor = new ModelRenderer(this, 0, 0);
		addRenderer(anchor);

		dynamites = new ModelRenderer[54];

		dynamites[0] = createDynamite(0, 0, 0);
		dynamites[3] = createDynamite(-1, 0, 0);
		dynamites[4] = createDynamite(1, 0, 0);
		dynamites[18] = createDynamite(-2, 0, 0);
		dynamites[19] = createDynamite(2, 0, 0);

		dynamites[9] = createDynamite(-0.5F, 1, 0);
		dynamites[10] = createDynamite(0.5F, 1, 0);
		dynamites[24] = createDynamite(-1.5F, 1, 0);
		dynamites[25] = createDynamite(1.5F, 1, 0);

		dynamites[15] = createDynamite(0, 2, 0);
		dynamites[30] = createDynamite(-1, 2, 0);
		dynamites[31] = createDynamite(1, 2, 0);

		dynamites[36] = createDynamite(-3, 0, 0);
		dynamites[37] = createDynamite(3, 0, 0);

		dynamites[42] = createDynamite(-2.5F, 1, 0);
		dynamites[43] = createDynamite(2.5F, 1, 0);

		dynamites[48] = createDynamite(-2F, 2, 0);
		dynamites[49] = createDynamite(2F, 2, 0);

		dynamites[1] = createDynamite(0, 0, -1);
		dynamites[5] = createDynamite(-1, 0, -1);
		dynamites[7] = createDynamite(1, 0, -1);
		dynamites[20] = createDynamite(-2, 0, -1);
		dynamites[22] = createDynamite(2, 0, -1);

		dynamites[11] = createDynamite(-0.5F , 1, -1);
		dynamites[13] = createDynamite(0.5F , 1, -1);
		dynamites[26] = createDynamite(-1.5F , 1, -1);
		dynamites[28] = createDynamite(1.5F, 1, -1);

		dynamites[16] = createDynamite(0, 2, -1);
		dynamites[32] = createDynamite(-1, 2, -1);
		dynamites[34] = createDynamite(1, 2, -1);

		dynamites[38] = createDynamite(-3, 0, -1);
		dynamites[40] = createDynamite(3, 0, -1);

		dynamites[44] = createDynamite(-2.5F, 1, -1);
		dynamites[46] = createDynamite(2.5F, 1, -1);

		dynamites[50] = createDynamite(-2F, 2, -1);
		dynamites[52] = createDynamite(2F, 2, -1);

		dynamites[2] = createDynamite(0, 0, 1);
		dynamites[8] = createDynamite(-1, 0, 1);
		dynamites[6] = createDynamite(1, 0, 1);
		dynamites[21] = createDynamite(-2, 0, 1);
		dynamites[23] = createDynamite(2, 0, 1);

		dynamites[14] = createDynamite(-0.5F, 1, 1);
		dynamites[12] = createDynamite(0.5F, 1, 1);
		dynamites[29] = createDynamite(-1.5F, 1, 1);
		dynamites[27] = createDynamite(1.5F, 1, 1);

		dynamites[17] = createDynamite(0, 2, 1);
		dynamites[35] = createDynamite(-1, 2, 1);
		dynamites[33] = createDynamite(1, 2, 1);

		dynamites[41] = createDynamite(-3, 0, 1);
		dynamites[39] = createDynamite(3, 0, 1);

		dynamites[47] = createDynamite(-2.5F, 1, 1);
		dynamites[45] = createDynamite(2.5F, 1, 1);

		dynamites[53] = createDynamite(-2F, 2, 1);
		dynamites[51] = createDynamite(2F, 2, 1);
    }

	private ModelRenderer createDynamite(float x, float y, float z) {
		ModelRenderer dynamite = new ModelRenderer(this, 0, 0);
		anchor.addChild(dynamite);
		fixSize(dynamite);

		dynamite.addBox(
			-8, 	    //X
			-4F, 	    //Y
			-4,	 	    //Z
			16,			//Size X
			8,			//Size Y
			8,			//Size Z
			0.0F
		);
		dynamite.setRotationPoint(
			x * 10F, 		//X
			y * -8F,		//Y
			z * 18F			//Z
		);

		dynamite.rotateAngleY = (float)Math.PI/ 2;

		return dynamite;
	}

    @Override
	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {
		if (module == null) {
            for (ModelRenderer dynamite : dynamites) {
                dynamite.isHidden = false;
            }
		}else{	
			float size = ((ModuleDynamite)module).explosionSize();
			float max = 4F /*base*/ + 0.8F /* how much a tnt is worth */ * 50 /*the number of TNTs possible */;
			float perModel = max / dynamites.length;
			for (int i = 0; i < dynamites.length; i++) {
				dynamites[i].isHidden = i * perModel >= size;
			}
		}

		anchor.setRotationPoint(
			0, 		                //X
			-24 / sizeMultiplier,			//Y
			0			            //Z
		);
	}

	private float sizeMultiplier;
    @Override
	public void render(ModuleBase module, float yaw, float pitch, float roll, float multiplier, float partialTime) {
		if (module == null) {
			sizeMultiplier = 1;
			super.render(module,yaw,pitch,roll, multiplier, partialTime);
		}else{
	
			float fuseMultiplier = (float)Math.abs(Math.sin(((float)((ModuleDynamite)module).getFuse() / ((ModuleDynamite)module).getFuseLength()) * Math.PI * 6));

			sizeMultiplier = fuseMultiplier * 0.5F + 1;
			GL11.glScalef(sizeMultiplier, sizeMultiplier, sizeMultiplier);

			super.render(module,yaw,pitch,roll, multiplier, partialTime);

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, fuseMultiplier);
			super.render(module,yaw,pitch,roll, multiplier, partialTime);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_TEXTURE_2D);

			GL11.glScalef(1 / sizeMultiplier, 1 / sizeMultiplier, 1 / sizeMultiplier);
			
		}
    }
}
