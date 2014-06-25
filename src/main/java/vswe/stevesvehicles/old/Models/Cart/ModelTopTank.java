package vswe.stevesvehicles.old.Models.Cart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.modules.ModuleBase;
import vswe.stevesvehicles.old.Modules.Storages.Tanks.ModuleTank;
import vswe.stevesvehicles.old.Renders.RendererMinecart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelTopTank extends ModelCartbase
{

	
	private static ResourceLocation texture = ResourceHelper.getResource("/models/tankModelTop.png");
	private static ResourceLocation textureOpen = ResourceHelper.getResource("/models/tankModelTopOpen.png");
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return open ? textureOpen : texture;
	}			

	protected int getTextureHeight() {
		return 32;
	}
	
	private boolean open;
    public ModelTopTank(boolean open)
    {
		this.open = open;

		for (int i = 0; i < 2; i++) {
			ModelRenderer tankside = new ModelRenderer(this, 0, 13);
			AddRenderer(tankside);

			tankside.addBox(
				-8, 	//X
				-2.5F, 	//Y
				-0.5F,	 	//Z
				16,					//Size X
				5,					//Size Y
				1,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			tankside.setRotationPoint(
				0F, 		//X
				-8.5F,			//Y
				-5.5F + i*11			//Z
			);

			if (!open || i == 0) {
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
					- 5.5F - i*6,		//Y
					0			//Z
				);
				
				tanktopbot.rotateAngleX = (float)Math.PI / 2;
			}

			
			
			ModelRenderer tankfrontback = new ModelRenderer(this, 0, 19);
			AddRenderer(tankfrontback);

			tankfrontback.addBox(
				-5, 	//X
				-2.5F, 	//Y
				-0.5F,	 	//Z
				10,					//Size X
				5,					//Size Y
				1,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			tankfrontback.setRotationPoint(
				-7.5F + i*15, 		//X
				-8.5F,		//Y
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
				((RendererMinecart)render).renderLiquidCuboid(liquid,  ((ModuleTank)module).getCapacity(), 0, open ? -7.25F : -8.5F, 0, 14, open ? 2.5F : 5, 10, mult);
			}
		}
    }
}
