package vswe.stevesvehicles.client.rendering.models.common;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.client.rendering.models.ModelVehicle;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.common.storage.tank.ModuleTank;
import vswe.stevesvehicles.client.rendering.RendererVehicle;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelSideTanks extends ModelVehicle {
	private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/models/tanksModel.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return TEXTURE;
	}	

    @Override
	protected int getTextureHeight() {
		return 16;
	}
	
    public ModelSideTanks() {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				ModelRenderer tankSide = new ModelRenderer(this, 0, 0);
				addRenderer(tankSide);

				tankSide.addBox(
                    -6,         //X
                    -3,         //Y
                    -0.5F,      //Z
                    12,         //Size X
                    6,          //Size Y
                    1,          //Size Z
                    0.0F
                );
				tankSide.setRotationPoint(
                    -2.0F,                              //X
                    -0.5F,                              //Y
                    -10.5F + i * 22 + -3F + j * 5       //Z
                );

				ModelRenderer tankTopBot = new ModelRenderer(this, 0, 7);
				addRenderer(tankTopBot);

				tankTopBot.addBox(
                    -6,             //X
                    -2,             //Y
                    -0.5F,          //Z
                    12,             //Size X
                    4,              //Size Y
                    1,              //Size Z
                    0.0F
                );
				tankTopBot.setRotationPoint(
                    -2.0F,                          //X
                    -0.5F + -2.5F + j * 5,          //Y
                    -11F + i * 22                   //Z
                );

				tankTopBot.rotateAngleX = (float)Math.PI / 2;
			}

			ModelRenderer tankFront = new ModelRenderer(this, 26, 0);
			addRenderer(tankFront);

			tankFront.addBox(
                -2,             //X
                -2,             //Y
                -0.5F,          //Z
                4,              //Size X
                4,              //Size Y
                1,              //Size Z
                0.0F
            );
			tankFront.setRotationPoint(
                -2.0F - 5.5F,           //X
                -0.5F,                  //Y
                -11F + i * 22            //Z
            );

			tankFront.rotateAngleY = (float)Math.PI / 2;

			ModelRenderer tankBack = new ModelRenderer(this, 36, 0);
			addRenderer(tankBack);

			tankBack.addBox(
                -2,         //X
                -2,         //Y
                -0.5F,      //Z
                4,          //Size X
                4,          //Size Y
                1,           //Size Z
                0.0F
            );
			tankBack.setRotationPoint(
                -2.0F + 6.5F,       //X
                -0.5F,              //Y
                -11F + i * 22       //Z
            );

			tankBack.rotateAngleY = (float)Math.PI / 2;

			ModelRenderer tube1 = new ModelRenderer(this, 26, 5);
			addRenderer(tube1);

			tube1.addBox(
				-1, 	        //X
				-1, 	        //Y
				-1F,	 	    //Z
				2,				//Size X
				2,				//Size Y
				2,			    //Size Z
				0.0F
			);
			tube1.setRotationPoint(
				-2.0F + 7.5F, 		//X
				-0.5F,		        //Y
				-11F + i * 22 		//Z
			);

			ModelRenderer tube2 = new ModelRenderer(this, 26, 5);
			addRenderer(tube2);

			tube2.addBox(
				-2, 	            //X
				-1, 	            //Y
				-1F,	 	        //Z
				4,					//Size X
				2,					//Size Y
				2,			     	//Size Z
				0.0F
			);
			tube2.setRotationPoint(
				-2.0F + 9.5F, 		//X
				-0.5F,		        //Y
				-10F + i*20 		//Z
			);

			tube2.rotateAngleY = (float)Math.PI / 2;

			ModelRenderer connection = new ModelRenderer(this, 36, 0);
			addRenderer(connection);

			connection.addBox(
				-2, 	            //X
				-2, 	            //Y
				-0.5F,	 	        //Z
				4,					//Size X
				4,					//Size Y
				1,			     	//Size Z
				0.0F
			);
			connection.setRotationPoint(
				-2.0F + 9.5F, 		//X
				-0.5F,		        //Y
				-8.5F + i * 17 		//Z
			);
		}
    }


	@Override
	public void render(ModuleBase module, float yaw, float pitch, float roll, float multiplier, float partialTime) {
		super.render(module,yaw,pitch,roll, multiplier, partialTime);
		if (module != null) {
			FluidStack liquid = ((ModuleTank)module).getFluid();
			if (liquid != null) {
				RendererVehicle.renderLiquidCuboid(liquid,  ((ModuleTank)module).getCapacity(), -2, -0.5F, -11, 10, 4, 4, multiplier);
                RendererVehicle.renderLiquidCuboid(liquid,  ((ModuleTank)module).getCapacity(), -2, -0.5F, 11, 10, 4, 4, multiplier);
			}
		}
    }
}
