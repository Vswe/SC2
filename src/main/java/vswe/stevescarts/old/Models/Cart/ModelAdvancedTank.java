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
public class ModelAdvancedTank extends ModelCartbase
{

	private static ResourceLocation texture = ResourceHelper.getResource("/models/tankModelLarge.png");

	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return texture;
	}


	
    public ModelAdvancedTank()
    {

		for (int i = 0; i < 2; i++) {
			ModelRenderer tankside = new ModelRenderer(this, 0, 13);
			AddRenderer(tankside);

			tankside.addBox(
				-8, 	//X
				-6.5F, 	//Y
				-0.5F,	 	//Z
				16,					//Size X
				13,					//Size Y
				1,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			tankside.setRotationPoint(
				0F, 		//X
				-4.5F,			//Y
				-5.5F + i*11			//Z
			);

			ModelRenderer tanktopbot = new ModelRenderer(this, 0, 0);
			AddRenderer(tanktopbot);

			tanktopbot.addBox(
				-8, 	//X
				-6, 	//Y
				-0.5F,	 	//Z
				16,					//Size X
				12,					//Size Y
				1,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			tanktopbot.setRotationPoint(
				0, 		//X
				2.5F - i*14,		//Y
				0			//Z
			);

			tanktopbot.rotateAngleX = (float)Math.PI / 2;
			
			ModelRenderer tankfrontback = new ModelRenderer(this, 0, 27);
			AddRenderer(tankfrontback);

			tankfrontback.addBox(
				-5, 	//X
				-6.5F, 	//Y
				-0.5F,	 	//Z
				10,					//Size X
				13,					//Size Y
				1,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			tankfrontback.setRotationPoint(
				-7.5F + i*15, 		//X
				-4.5F,		//Y
				0 			//Z
			);

			tankfrontback.rotateAngleY = (float)Math.PI / 2;			
		}


		
    }


	@Override
	public void render(Render render,ModuleBase module, float yaw, float pitch, float roll, float mult, float partialtime)
    {


		super.render(render,module,yaw,pitch,roll, mult, partialtime);
		if (render != null && module != null) {
			FluidStack liquid = ((ModuleTank)module).getFluid();
			if (liquid != null) {
				((RendererMinecart)render).renderLiquidCuboid(liquid,  ((ModuleTank)module).getCapacity(), 0, -4.5F, 0, 14, 13, 10, mult);
			}
		}
    }
}
