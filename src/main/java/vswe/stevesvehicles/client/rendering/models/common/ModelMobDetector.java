package vswe.stevesvehicles.client.rendering.models.common;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.client.rendering.models.ModelVehicle;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.common.attachment.ModuleShooterAdvanced;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelMobDetector extends ModelVehicle {
	private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/models/mobDetectorModel.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return TEXTURE;
	}		

    @Override
	protected int getTextureHeight() {
		return 16;
	}

	private ModelRenderer base;
    public ModelMobDetector() {
		base = new ModelRenderer(this, 0, 0);
		addRenderer(base);

		base.addBox(
			-1, 	        //X
			-2F, 	        //Y
			-1,	 	        //Z
			2,				//Size X
			4,				//Size Y
			2,			    //Size Z
			0.0F
		);
		base.setRotationPoint(
			0.0F, 		    //X
			-14F,			//Y
			-0.0F			//Z
		);

		ModelRenderer body = new ModelRenderer(this, 0, 8);
		base.addChild(body);
		fixSize(body);

		body.addBox(
			-2.5F, 	        //X
			-1.5F, 	        //Y
			-0.5F,	 	    //Z
			5,				//Size X
			3,				//Size Y
			1,			    //Size Z
			0.0F
		);
		body.setRotationPoint(
                0.0F,            //X
                -1.5F,            //Y
                -1.5F            //Z
        );

		for (int i = 0; i < 2; i++) {
			ModelRenderer side = new ModelRenderer(this, 0, 13);
			body.addChild(side);
			fixSize(side);

			side.addBox(
				-2.5F, 	        //X
				-0.5F, 	        //Y
				-0.5F,	 	    //Z
				5,				//Size X
				1,				//Size Y
				1,			    //Size Z
				0.0F
			);
			side.setRotationPoint(
				0.0F, 		        //X
				2F * (i * 2 - 1),	//Y
				-1F			        //Z
			);
		}

		for (int i = 0; i < 2; i++) {
			ModelRenderer side = new ModelRenderer(this, 12, 13);
			body.addChild(side);
			fixSize(side);

			side.addBox(
				-1.5F, 	        //X
				-0.5F, 	        //Y
				-0.5F,	 	    //Z
				3,				//Size X
				1,				//Size Y
				1,			    //Size Z
				0.0F
			);
			side.setRotationPoint(
				3F * (i * 2 - 1), 		//X
				0,			            //Y
				-1F			            //Z
			);

			side.rotateAngleZ = (float)(Math.PI / 2);
		}

		ModelRenderer receiver = new ModelRenderer(this, 8, 0);
		body.addChild(receiver);
		fixSize(receiver);

		receiver.addBox(
			-0.5F, 	        //X
			-0.5F, 	        //Y
			-0.5F,	 	    //Z
			1,				//Size X
			1,				//Size Y
			1,			    //Size Z
			0.0F
		);
		receiver.setRotationPoint(
			0, 		    //X
			0,			//Y
			-1F			//Z
		);

		receiver.rotateAngleY = (float)(Math.PI / 2);

		ModelRenderer dot = new ModelRenderer(this, 8, 2);
		body.addChild(dot);
		fixSize(dot);

		dot.addBox(
			-0.5F, 	        //X
			-0.5F, 	        //Y
			-0.5F,	 	    //Z
			1,				//Size X
			1,				//Size Y
			1,			    //Size Z
			0.0F
		);
		dot.setRotationPoint(
			0, 		        //X
			0,			    //Y
			-2F			    //Z
		);
    }

    @Override
	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {
		base.rotateAngleY = module == null ? 0 : ((ModuleShooterAdvanced)module).getDetectorAngle() + yaw;
	}
}
