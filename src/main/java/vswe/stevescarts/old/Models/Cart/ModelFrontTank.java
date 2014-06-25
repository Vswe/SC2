package vswe.stevescarts.old.Models.Cart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevescarts.old.Helpers.ResourceHelper;
import vswe.stevescarts.vehicles.modules.ModuleBase;
import vswe.stevescarts.old.Modules.Storages.Tanks.ModuleTank;
import vswe.stevescarts.old.Renders.RendererMinecart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelFrontTank extends ModelCartbase
{
	
	private static ResourceLocation texture = ResourceHelper.getResource("/models/tankModelFront.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return texture;
	}		
	

	protected int getTextureWidth() {
		return 32;
	}	
	
	protected int getTextureHeight() {
		return 32;
	}
	
    public ModelFrontTank()
    {

		for (int i = 0; i < 2; i++) {
			ModelRenderer tankside = new ModelRenderer(this, 0, 15);
			AddRenderer(tankside);

			tankside.addBox(
				-4, 	//X
				-3F, 	//Y
				-0.5F,	 	//Z
				8,					//Size X
				6,					//Size Y
				1,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			tankside.setRotationPoint(
				- 14, 		//X
				0,			//Y
				-6.5F + i*13			//Z
			);

			ModelRenderer tanktopbot = new ModelRenderer(this, 0, 0);
			AddRenderer(tanktopbot);

			tanktopbot.addBox(
				-4, 	//X
				-7, 	//Y
				-0.5F,	 	//Z
				8,					//Size X
				14,					//Size Y
				1,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			tanktopbot.setRotationPoint(
				- 14, 		//X
				3.5F - i*7,		//Y
				0			//Z
			);

			tanktopbot.rotateAngleX = (float)Math.PI / 2;
			
			ModelRenderer tankfrontback = new ModelRenderer(this, 0, 22);
			AddRenderer(tankfrontback);

			tankfrontback.addBox(
				-6, 	//X
				-3F, 	//Y
				-0.5F,	 	//Z
				12,					//Size X
				6,					//Size Y
				1,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			tankfrontback.setRotationPoint(
				-17.5F + i*7, 		//X
				0,		//Y
				0 			//Z
			);

			tankfrontback.rotateAngleY = (float)Math.PI / 2;			
		}


		
    }


	@Override
	public void render(Render render, ModuleBase module, float yaw, float pitch, float roll, float mult, float partialtime)
    {


		super.render(render,module,yaw,pitch,roll, mult, partialtime);
		if (render != null && module != null) {
			FluidStack liquid = ((ModuleTank)module).getFluid();
			if (liquid != null) {
				((RendererMinecart)render).renderLiquidCuboid(liquid,  ((ModuleTank)module).getCapacity(), -14, 0, 0, 6, 6, 12, mult);
			}
		}
    }
}
