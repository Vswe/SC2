package vswe.stevesvehicles.client.rendering.models;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.common.storage.tank.ModuleTank;
import vswe.stevesvehicles.client.rendering.RendererVehicle;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelAdvancedTank extends ModelVehicle {

	private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/models/tankModelLarge.png");

	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return TEXTURE;
	}


	
    public ModelAdvancedTank() {

		for (int i = 0; i < 2; i++) {
			ModelRenderer tankSide = new ModelRenderer(this, 0, 13);
			addRenderer(tankSide);

			tankSide.addBox(
                    -8,                     //X
                    -6.5F,                  //Y
                    -0.5F,                  //Z
                    16,                     //Size X
                    13,                     //Size Y
                    1,                      //Size Z
                    0.0F
            );
			tankSide.setRotationPoint(
                    0F,                 //X
                    -4.5F,              //Y
                    -5.5F + i * 11      //Z
            );

			ModelRenderer tankTopBot = new ModelRenderer(this, 0, 0);
			addRenderer(tankTopBot);

			tankTopBot.addBox(
                    -8,             //X
                    -6,             //Y
                    -0.5F,          //Z
                    16,             //Size X
                    12,             //Size Y
                    1,              //Size Z
                    0.0F
            );
			tankTopBot.setRotationPoint(
                    0,                  //X
                    2.5F - i * 14,      //Y
                    0                   //Z
            );

			tankTopBot.rotateAngleX = (float)Math.PI / 2;
			
			ModelRenderer tankFrontBack = new ModelRenderer(this, 0, 27);
			addRenderer(tankFrontBack);

			tankFrontBack.addBox(
                    -5,             //X
                    -6.5F,          //Y
                    -0.5F,          //Z
                    10,             //Size X
                    13,             //Size Y
                    1,              //Size Z
                    0.0F
            );
			tankFrontBack.setRotationPoint(
                    -7.5F + i * 15,     //X
                    -4.5F,              //Y
                    0                   //Z
            );

			tankFrontBack.rotateAngleY = (float)Math.PI / 2;
		}


		
    }


	@Override
	public void render(ModuleBase module, float yaw, float pitch, float roll, float multiplier, float partialTime) {
		super.render(module, yaw, pitch, roll, multiplier, partialTime);
		if (module != null) {
			FluidStack liquid = ((ModuleTank)module).getFluid();
			if (liquid != null) {
                RendererVehicle.renderLiquidCuboid(liquid,  ((ModuleTank)module).getCapacity(), 0, -4.5F, 0, 14, 13, 10, multiplier);
			}
		}
    }
}
