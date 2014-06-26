package vswe.stevesvehicles.models;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vswe.stevesvehicles.modules.ModuleBase;
import vswe.stevesvehicles.modules.hull.HullPig;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelPigHelmet extends ModelVehicle
{

	@Override
	public ResourceLocation getResource(ModuleBase module) {
		HullPig pig = (HullPig)module;
		

		return pig.getHelmetResource(isOverlay);	
	}
	
	

	protected int getTextureHeight() {
		return 32;
	}

	private boolean isOverlay;

    public ModelPigHelmet(boolean isOverlay)
    {
		this.isOverlay = isOverlay;
		
        ModelRenderer Headwear = new ModelRenderer(this, 0, 0);
		AddRenderer(Headwear);
		
        Headwear.addBox(
			-4.0F, 
			-4.0F, 
			-4.0F, 
			8, 
			8, 
			8, 
			0.0F
		);
		
        Headwear.setRotationPoint(
			-12.2F + (isOverlay ? 0.2F : 0F), 
			-5.4F,
			0.0F
		);	
		Headwear.rotateAngleY = (float)Math.PI / 2;
    }

	@Override
	public void render(Render render,ModuleBase module, float yaw, float pitch, float roll, float mult, float partialtime)
    {

        if (render == null || module == null) {
			return;
		}

		HullPig pig = (HullPig)module;
		
		
		if (!pig.hasHelmet() || (isOverlay && !pig.getHelmetMultiRender())) {
			return;
		}

		final float sizemult = 1F + 1F / 16F + 1F / 32F + (isOverlay ? 1F / 48F : 0);
		GL11.glScalef(sizemult, sizemult, sizemult);
		
		if (pig.hasHelmetColor(isOverlay)) {
			int color = pig.getHelmetColor(isOverlay);
			float red = (float)(color >> 16 & 255) / 255.0F;
			float green = (float)(color >> 8 & 255) / 255.0F;
			float blue = (float)(color & 255) / 255.0F;
			GL11.glColor3f(red, green, blue);		
		}
		
		super.render(render,module,yaw,pitch,roll, mult, partialtime);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		GL11.glScalef(1 / sizemult, 1 / sizemult, 1 / sizemult);
    }	

}
