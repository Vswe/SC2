package vswe.stevescarts.old.Models.Cart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vswe.stevescarts.old.Helpers.ResourceHelper;
import vswe.stevescarts.old.Modules.ModuleBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelPigTail extends ModelCartbase
{
	
	private static ResourceLocation texture = ResourceHelper.getResource("/models/pigtailModel.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return texture;
	}	
	
	
	protected int getTextureHeight() {
		return 32;
	}


    public ModelPigTail()
    {
		super();

		ModelRenderer tailanchor = new ModelRenderer(this);
		AddRenderer(tailanchor);
		
        tailanchor.setRotationPoint(
			10.0F, 
			-4, 
			0F
		);			
		tailanchor.rotateAngleY = (float)Math.PI / 2;
		
		ModelRenderer tail1 = new ModelRenderer(this, 0, 0);
		fixSize(tail1);
		tailanchor.addChild(tail1);
		
        tail1.addBox(
			-1.5F, 
			-0.5F, 
			-0.0F, 
			3, 
			1, 
			1, 
			0.0F
		);
		
		
        tail1.setRotationPoint(
			0, 
			0, 
			0F
		);	
		

		ModelRenderer tail2 = new ModelRenderer(this, 0, 0);
		fixSize(tail2);
		tailanchor.addChild(tail2);
		
        tail2.addBox(
			-0.5F, 
			-1.5F, 
			-0.0F, 
			1, 
			3, 
			1, 
			0.0F
		);
		
		
        tail2.setRotationPoint(
			2, 
			-2, 
			0F
		);
		
		ModelRenderer tail3 = new ModelRenderer(this, 0, 0);
		fixSize(tail3);
		tailanchor.addChild(tail3);
		
        tail3.addBox(
			-1.0F, 
			-0.5F, 
			-0.0F, 
			2, 
			1, 
			1, 
			0.0F
		);
		
		
        tail3.setRotationPoint(
			0.5F, 
			-4, 
			0F
		);	

		ModelRenderer tail4 = new ModelRenderer(this, 0, 0);
		fixSize(tail4);
		tailanchor.addChild(tail4);
		
        tail4.addBox(
			-0.5F, 
			-0.5F, 
			-0.0F, 
			1, 
			1, 
			1, 
			0.0F
		);
		
		
        tail4.setRotationPoint(
			-1, 
			-3, 
			0F
		);

		ModelRenderer tail5 = new ModelRenderer(this, 0, 0);
		fixSize(tail5);
		tailanchor.addChild(tail5);
		
        tail5.addBox(
			-0.5F, 
			-0.5F, 
			-0.0F, 
			1, 
			1, 
			1, 
			0.0F
		);
		
		
        tail5.setRotationPoint(
			0, 
			-2, 
			0F
		);		
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
