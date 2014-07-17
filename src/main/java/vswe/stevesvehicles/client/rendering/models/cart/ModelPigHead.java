package vswe.stevesvehicles.client.rendering.models.cart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.client.rendering.models.ModelVehicle;
import vswe.stevesvehicles.module.ModuleBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelPigHead extends ModelVehicle {

	
	private static final ResourceLocation TEXTURE = ResourceHelper.getResourceFromPath("/entity/pig/pig.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return TEXTURE;
	}		

    @Override
	protected int getTextureHeight() {
		return 32;
	}


    public ModelPigHead() {
		ModelRenderer head = new ModelRenderer(this, 0, 0);
		addRenderer(head);
		
        head.addBox(
			-4.0F, 
			-4.0F, 
			-8.0F, 
			8, 
			8, 
			8, 
			0.0F
		);
		
		
        head.setRotationPoint(
			-9.0F, 
			-5, 
			0F
		);	
		head.rotateAngleY = (float)Math.PI / 2;

		head.setTextureOffset(16, 16).addBox(-2.0F, 0.0F, -9.0F, 4, 3, 1, 0);
    }

    @Override
	public void render(ModuleBase module, float yaw, float pitch, float roll, float multiplier, float partialTime) {
		if (module != null) {
			float[] color = module.getVehicle().getColor();
			GL11.glColor4f(color[0], color[1], color[2], 1.0F);
		}
		super.render(module,yaw,pitch,roll, multiplier, partialTime);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }		

}
