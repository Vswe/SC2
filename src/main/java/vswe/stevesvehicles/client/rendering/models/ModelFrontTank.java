package vswe.stevesvehicles.client.rendering.models;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.common.storage.tank.ModuleTank;
import vswe.stevesvehicles.client.rendering.RendererVehicle;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelFrontTank extends ModelVehicle {
	
	private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/models/tankModelFront.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return TEXTURE;
	}		
	
    @Override
	protected int getTextureWidth() {
		return 32;
	}	

    @Override
	protected int getTextureHeight() {
		return 32;
	}
	
    public ModelFrontTank() {

		for (int i = 0; i < 2; i++) {
			ModelRenderer tankSide = new ModelRenderer(this, 0, 15);
			addRenderer(tankSide);

			tankSide.addBox(
                -4,             //X
                -3F,            //Y
                -0.5F,          //Z
                8,              //Size X
                6,              //Size Y
                1,              //Size Z
                0.0F
            );
			tankSide.setRotationPoint(
                -14,                //X
                0,                  //Y
                -6.5F + i * 13      //Z
            );

			ModelRenderer tankTopBot = new ModelRenderer(this, 0, 0);
			addRenderer(tankTopBot);

			tankTopBot.addBox(
                -4,         //X
                -7,         //Y
                -0.5F,      //Z
                8,          //Size X
                14,         //Size Y
                1,          //Size Z
                0.0F
            );
			tankTopBot.setRotationPoint(
                -14,                //X
                3.5F - i * 7,        //Y
                0                   //Z
            );

			tankTopBot.rotateAngleX = (float)Math.PI / 2;
			
			ModelRenderer tankFrontBack = new ModelRenderer(this, 0, 22);
			addRenderer(tankFrontBack);

			tankFrontBack.addBox(
                -6,         //X
                -3F,        //Y
                -0.5F,      //Z
                12,         //Size X
                6,          //Size Y
                1,          //Size Z
                0.0F
            );
			tankFrontBack.setRotationPoint(
                -17.5F + i * 7,     //X
                0,                  //Y
                0                   //Z
            );

			tankFrontBack.rotateAngleY = (float)Math.PI / 2;
		}


		
    }


	@Override
	public void render(ModuleBase module, float yaw, float pitch, float roll, float multiplier, float partialTime) {
		super.render(module,yaw,pitch,roll, multiplier, partialTime);
		if (module != null) {
			FluidStack liquid = ((ModuleTank)module).getFluid();
			if (liquid != null) {
                RendererVehicle.renderLiquidCuboid(liquid,  ((ModuleTank)module).getCapacity(), -14, 0, 0, 6, 6, 12, multiplier);
			}
		}
    }
}
