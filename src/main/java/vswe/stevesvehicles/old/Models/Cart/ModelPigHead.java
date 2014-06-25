package vswe.stevesvehicles.old.Models.Cart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.modules.ModuleBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelPigHead extends ModelCartbase
{

	
	private static ResourceLocation texture = ResourceHelper.getResourceFromPath("/entity/pig/pig.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return texture;
	}		

	protected int getTextureHeight() {
		return 32;
	}


    public ModelPigHead()
    {
		super();

		ModelRenderer head = new ModelRenderer(this, 0, 0);
		AddRenderer(head);
		
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

	public void render(Render render,ModuleBase module, float yaw, float pitch, float roll, float mult, float partialtime)
    {
		if (module != null) {
			float[] color = module.getCart().getColor();
			GL11.glColor4f(color[0], color[1], color[2], 1.0F);
		}
		super.render(render,module,yaw,pitch,roll, mult, partialtime);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }		

}
