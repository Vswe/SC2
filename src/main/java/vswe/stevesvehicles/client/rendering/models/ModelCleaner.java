package vswe.stevesvehicles.client.rendering.models;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.module.ModuleBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelCleaner extends ModelVehicle {
	private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/models/cleanerModel.png");
	
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

    public ModelCleaner() {
		ModelRenderer box = new ModelRenderer(this, 0, 0);
		addRenderer(box);

		box.addBox(
			-4, 	            //X
			-3, 	            //Y
			-4,	 	            //Z
			8,					//Size X
			6,					//Size Y
			8,			     	//Size Z
			0.0F
		);
		box.setRotationPoint(
			4.0F, 		    //X
			-0.0F,			//Y
			-0.0F			//Z
		);

		for (int i = 0; i < 2; i++) {
			ModelRenderer sideTube = new ModelRenderer(this, 0, 14);
			addRenderer(sideTube);

			sideTube.addBox(
                    -2,                   //X
                    -2,                   //Y
                    -1,                   //Z
                    4,                    //Size X
                    4,                    //Size Y
                    2,                    //Size Z
                    0.0F
            );
			sideTube.setRotationPoint(
                    4,                      //X
                    -0.0F,                  //Y
                    -5.0F * (i * 2 - 1)     //Z
            );
		}

		ModelRenderer tube = new ModelRenderer(this, 0, 14);
		addRenderer(tube);

		tube.addBox(
			-2, 	            //X
			-2, 	            //Y
			-1,	 	            //Z
			4,					//Size X
			4,					//Size Y
			2,			     	//Size Z
			0.0F
		);
		tube.setRotationPoint(
			-1F, 		//X
			0.0F,		//Y
			0			//Z
		);

		tube.rotateAngleY = (float)Math.PI / 2;

		for (int i = 0; i < 2; i++) {
			ModelRenderer endTube = new ModelRenderer(this, 0, 14);
			addRenderer(endTube);

			endTube.addBox(
                -2,         //X
                -2,         //Y
                -1,         //Z
                4,          //Size X
                4,          //Size Y
                2,          //Size Z
                0.0F
            );
			endTube.setRotationPoint(
                -7,                     //X
                -0.0F,                  //Y
                -3.0F * (i * 2 - 1)     //Z
            );

			endTube.rotateAngleY = (float)Math.PI / 2;
		}

		ModelRenderer connectionTube = new ModelRenderer(this, 0, 20);
		addRenderer(connectionTube);

		connectionTube.addBox(
            -5,         //X
            -5,         //Y
            -1,         //Z
            10,         //Size X
            4,          //Size Y
            4,          //Size Z
            0.0F
        );
    connectionTube.setRotationPoint(
            -5F,        //X
            3F,         //Y
            0           //Z
        );

		connectionTube.rotateAngleY = (float)Math.PI / 2;

		for (int i = 0; i < 2; i++) {
			ModelRenderer externalTube = new ModelRenderer(this, 0, 14);
			addRenderer(externalTube);

			externalTube.addBox(
                -2,         //X
                -2,         //Y
                -1,         //Z
                4,          //Size X
                4,          //Size Y
                2,          //Size Z
                0.0F
            );
			externalTube.setRotationPoint(
                -10.95F,                //X
                -0.0F,                  //Y
                -3.05F * (i * 2 - 1)    //Z
            );

			externalTube.rotateAngleY = (float)Math.PI / 2;
		}
    }


}
