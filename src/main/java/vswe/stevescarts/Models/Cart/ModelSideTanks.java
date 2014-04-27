package vswe.stevescarts.Models.Cart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Storages.Tanks.ModuleTank;
import vswe.stevescarts.Renders.RendererMinecart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelSideTanks extends ModelCartbase
{

	private static ResourceLocation texture = ResourceHelper.getResource("/models/tanksModel.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return texture;
	}	

	protected int getTextureHeight() {
		return 16;
	}
	
    public ModelSideTanks()
    {

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				ModelRenderer tankside = new ModelRenderer(this, 0, 0);
				AddRenderer(tankside);

				tankside.addBox(
					-6, 	//X
					-3, 	//Y
					-0.5F,	 	//Z
					12,					//Size X
					6,					//Size Y
					1,			     	//Size Z
					0.0F			 	//Size Increasement
				);
				tankside.setRotationPoint(
					-2.0F, 		//X
					-0.5F,			//Y
					-10.5F + i*22 + -3F + j*5			//Z
				);

				ModelRenderer tanktopbot = new ModelRenderer(this, 0, 7);
				AddRenderer(tanktopbot);

				tanktopbot.addBox(
					-6, 	//X
					-2, 	//Y
					-0.5F,	 	//Z
					12,					//Size X
					4,					//Size Y
					1,			     	//Size Z
					0.0F			 	//Size Increasement
				);
				tanktopbot.setRotationPoint(
					-2.0F, 		//X
					-0.5F + -2.5F + j*5,		//Y
					-11F + i*22 			//Z
				);

				tanktopbot.rotateAngleX = (float)Math.PI / 2;
			}

			ModelRenderer tankfront = new ModelRenderer(this, 26, 0);
			AddRenderer(tankfront);

			tankfront.addBox(
				-2, 	//X
				-2, 	//Y
				-0.5F,	 	//Z
				4,					//Size X
				4,					//Size Y
				1,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			tankfront.setRotationPoint(
				-2.0F - 5.5F, 		//X
				-0.5F,		//Y
				-11F + i*22 			//Z
			);

			tankfront.rotateAngleY = (float)Math.PI / 2;

			ModelRenderer tankback = new ModelRenderer(this, 36, 0);
			AddRenderer(tankback);

			tankback.addBox(
				-2, 	//X
				-2, 	//Y
				-0.5F,	 	//Z
				4,					//Size X
				4,					//Size Y
				1,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			tankback.setRotationPoint(
				-2.0F + 6.5F, 		//X
				-0.5F,		//Y
				-11F + i*22 			//Z
			);

			tankback.rotateAngleY = (float)Math.PI / 2;

			ModelRenderer tube1 = new ModelRenderer(this, 26, 5);
			AddRenderer(tube1);

			tube1.addBox(
				-1, 	//X
				-1, 	//Y
				-1F,	 	//Z
				2,					//Size X
				2,					//Size Y
				2,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			tube1.setRotationPoint(
				-2.0F + 7.5F, 		//X
				-0.5F,		//Y
				-11F + i*22 			//Z
			);

			ModelRenderer tube2 = new ModelRenderer(this, 26, 5);
			AddRenderer(tube2);

			tube2.addBox(
				-2, 	//X
				-1, 	//Y
				-1F,	 	//Z
				4,					//Size X
				2,					//Size Y
				2,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			tube2.setRotationPoint(
				-2.0F + 9.5F, 		//X
				-0.5F,		//Y
				-10F + i*20 			//Z
			);

			tube2.rotateAngleY = (float)Math.PI / 2;

			ModelRenderer connection = new ModelRenderer(this, 36, 0);
			AddRenderer(connection);

			connection.addBox(
				-2, 	//X
				-2, 	//Y
				-0.5F,	 	//Z
				4,					//Size X
				4,					//Size Y
				1,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			connection.setRotationPoint(
				-2.0F + 9.5F, 		//X
				-0.5F,		//Y
				-8.5F + i*17 			//Z
			);
		}
    }


	@Override
	public void render(Render render,ModuleBase module, float yaw, float pitch, float roll, float mult, float partialtime)
    {


		super.render(render,module,yaw,pitch,roll, mult, partialtime);
		if (render != null && module != null) {
			FluidStack liquid = ((ModuleTank)module).getFluid();
			if (liquid != null) {
				((RendererMinecart)render).renderLiquidCuboid(liquid,  ((ModuleTank)module).getCapacity(), -2, -0.5F, -11, 10, 4, 4, mult);
				((RendererMinecart)render).renderLiquidCuboid(liquid,  ((ModuleTank)module).getCapacity(), -2, -0.5F, 11, 10, 4, 4, mult);
			}
		}
    }
}
