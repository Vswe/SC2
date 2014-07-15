package vswe.stevesvehicles.client.rendering.models;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.cart.attachment.ModuleRailer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelRailer extends ModelVehicle {
	
	private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/models/builderModel.png");
	
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

	private final ModelRenderer[] rails;

    public ModelRailer(int railCount) {
		rails = new ModelRenderer[railCount];

		for (int r = 0; r < rails.length; r++) {
			ModelRenderer railAnchor = new ModelRenderer(this);
			addRenderer(railAnchor);
			rails[r] = railAnchor;

			railAnchor.setRotationPoint(
				0, 			//X
				-r,			//Y
				0			//Z
			);



			ModelRenderer rail1 = new ModelRenderer(this, 18, 0);
			fixSize(rail1);
			railAnchor.addChild(rail1);

			rail1.addBox(
				1F, 	    //X
				8, 	        //Y
				0.5F,	 	//Z
				2,			//Size X
				16,			//Size Y
				1,			//Size Z
				0.0F
			);
			rail1.setRotationPoint(
				-16.0F, 		//X
				-6.5F,	        //Y
				-7.0F			//Z
			);

			rail1.rotateAngleZ = ((float)Math.PI * 3F / 2F);
			rail1.rotateAngleY = ((float)Math.PI * 3F / 2F);

			ModelRenderer rail2 = new ModelRenderer(this, 24, 0);
			fixSize(rail2);
			railAnchor.addChild(rail2);

			rail2.addBox(
				1F, 	    //X
				8, 	        //Y
				0.5F,	 	//Z
				2,			//Size X
				16,			//Size Y
				1,			//Size Z
				0.0F
			);
			rail2.setRotationPoint(
				-16.0F, 		//X
				-6.5F,	        //Y
				3.0F			//Z
			);

			rail2.rotateAngleZ = ((float)Math.PI * 3F / 2F);
			rail2.rotateAngleY = ((float)Math.PI * 3F / 2F);

			for (int i = 0; i < 4; i++) {
				ModelRenderer railBedMiddle = new ModelRenderer(this, 0, 0);
				fixSize(railBedMiddle);
				railAnchor.addChild(railBedMiddle);

				railBedMiddle.addBox(
                    4F,         //X
                    1,          //Y
                    0.5F,       //Z
                    8,          //Size X
                    2,          //Size Y
                    1,          //Size Z
                    0.0F
                );
				railBedMiddle.setRotationPoint(
                    -8.0F + i * 4,      //X
                    -6.5F,              //Y
                    -8.0F               //Z
                );

				railBedMiddle.rotateAngleZ = ((float)Math.PI * 3F / 2F);
				railBedMiddle.rotateAngleY = ((float)Math.PI * 3F / 2F);

				ModelRenderer railBedSide1 = new ModelRenderer(this, 0, 3);
				fixSize(railBedSide1);
				railAnchor.addChild(railBedSide1);

				railBedSide1.addBox(
                    0.5F,           //X
                    1,              //Y
                    0.5F,           //Z
                    1,              //Size X
                    2,              //Size Y
                    1,              //Size Z
                    0.0F
                );
				railBedSide1.setRotationPoint(
                    -8.0F + i * 4,      //X
                    -6.5F,              //Y
                    -7.5F               //Z
                );

				railBedSide1.rotateAngleZ = ((float)Math.PI * 3F / 2F);
				railBedSide1.rotateAngleY = ((float)Math.PI * 3F / 2F);

				ModelRenderer railBedSide2 = new ModelRenderer(this, 0, 3);
				fixSize(railBedSide2);
				railAnchor.addChild(railBedSide2);

				railBedSide2.addBox(
                    0.5F,           //X
                    1,              //Y
                    0.5F,           //Z
                    1,              //Size X
                    2,              //Size Y
                    1,              //Size Z
                    0.0F
                );
				railBedSide2.setRotationPoint(
                    -8.0F + i * 4,      //X
                    -6.5F,              //Y
                    5.5F                //Z
                );

				railBedSide2.rotateAngleZ = ((float)Math.PI * 3F / 2F);
				railBedSide2.rotateAngleY = ((float)Math.PI * 3F / 2F);
			}
		}
    }

    @Override
	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {
		int valid = module == null ? 1 : ((ModuleRailer)module).getRails();

		for (int i = 0; i < rails.length; i++) {
			rails[i].rotateAngleY = module == null ? 0 : ((ModuleRailer)module).getRailAngle(i);
		
			rails[i].isHidden = i >= valid;
		}
	}
}
