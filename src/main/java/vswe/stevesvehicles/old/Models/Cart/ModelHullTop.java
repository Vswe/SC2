package vswe.stevesvehicles.old.Models.Cart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vswe.stevesvehicles.modules.ModuleBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelHullTop extends ModelCartbase
{

	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return resource;
	}

	protected int getTextureHeight() {
		return 16;
	}


	private ResourceLocation resource;
	private boolean useColors;
	public ModelHullTop(ResourceLocation resource) {
		this(resource, true);
	}
	
    public ModelHullTop(ResourceLocation resource, boolean useColors)
    {
		super();
		this.resource = resource;
		this.useColors = useColors;

		ModelRenderer top = new ModelRenderer(this, 0, 0);
		AddRenderer(top);

        top.addBox(
			-8, 	//X
			-6, 	//Y
			-1,	 	//Z
			16,		//Size X
			12,		//Size Y
			2,			     	//Size Z
			0.0F			 	//Size Increasement
		);
        top.setRotationPoint(
			0.0F, 					//X
			-4/*+yOffset*/,	//Y
			0.0F					//Z
		);

		top.rotateAngleX = (float)-Math.PI / 2;
    }
	
	public void render(Render render,ModuleBase module, float yaw, float pitch, float roll, float mult, float partialtime)
    {
		if (useColors && module != null) {
			float[] color = module.getCart().getColor();
			GL11.glColor4f(color[0], color[1], color[2], 1.0F);
		}
		super.render(render,module,yaw,pitch,roll, mult, partialtime);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }	
}
