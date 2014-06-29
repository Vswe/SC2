package vswe.stevesvehicles.client.rendering.models;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.cart.attachment.ModuleRailer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelRailer extends ModelVehicle
{
	
	private static ResourceLocation texture = ResourceHelper.getResource("/models/builderModel.png");
	
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

	private ModelRenderer[] rails;

    public ModelRailer(int railCount)
    {
		rails = new ModelRenderer[railCount];
		for (int r = 0; r < rails.length; r++) {
			ModelRenderer railAnchor = new ModelRenderer(this);
			AddRenderer(railAnchor);
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
				1F, 	//X
				8, 	//Y
				0.5F,	 	//Z
				2,					//Size X
				16,					//Size Y
				1,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			rail1.setRotationPoint(
				-16.0F, 			//X
				-6.5F,	//Y
				-7.0F			//Z
			);

			rail1.rotateAngleZ = ((float)Math.PI * 3F / 2F);
			rail1.rotateAngleY = ((float)Math.PI * 3F / 2F);

			ModelRenderer rail2 = new ModelRenderer(this, 24, 0);
			fixSize(rail2);
			railAnchor.addChild(rail2);

			rail2.addBox(
				1F, 	//X
				8, 	//Y
				0.5F,	 	//Z
				2,					//Size X
				16,					//Size Y
				1,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			rail2.setRotationPoint(
				-16.0F, 			//X
				-6.5F,	//Y
				3.0F			//Z
			);

			rail2.rotateAngleZ = ((float)Math.PI * 3F / 2F);
			rail2.rotateAngleY = ((float)Math.PI * 3F / 2F);

			for (int i = 0; i < 4; i++) {
				ModelRenderer railbedMiddle = new ModelRenderer(this, 0, 0);
				fixSize(railbedMiddle);
				railAnchor.addChild(railbedMiddle);

				railbedMiddle.addBox(
					4F, 	//X
					1, 	//Y
					0.5F,	 	//Z
					8,					//Size X
					2,					//Size Y
					1,			     	//Size Z
					0.0F			 	//Size Increasement
				);
				railbedMiddle.setRotationPoint(
					-8.0F+i*4, 			//X
					-6.5F,	//Y
					-8.0F			//Z
				);

				railbedMiddle.rotateAngleZ = ((float)Math.PI * 3F / 2F);
				railbedMiddle.rotateAngleY = ((float)Math.PI * 3F / 2F);

				ModelRenderer railbedSide1 = new ModelRenderer(this, 0, 3);
				fixSize(railbedSide1);
				railAnchor.addChild(railbedSide1);

				railbedSide1.addBox(
					0.5F, 	//X
					1, 	//Y
					0.5F,	 	//Z
					1,					//Size X
					2,					//Size Y
					1,			     	//Size Z
					0.0F			 	//Size Increasement
				);
				railbedSide1.setRotationPoint(
					-8.0F+i*4, 			//X
					-6.5F,	//Y
					-7.5F			//Z
				);

				railbedSide1.rotateAngleZ = ((float)Math.PI * 3F / 2F);
				railbedSide1.rotateAngleY = ((float)Math.PI * 3F / 2F);

				ModelRenderer railbedSide2 = new ModelRenderer(this, 0, 3);
				fixSize(railbedSide2);
				railAnchor.addChild(railbedSide2);

				railbedSide2.addBox(
					0.5F, 	//X
					1, 	//Y
					0.5F,	 	//Z
					1,					//Size X
					2,					//Size Y
					1,			     	//Size Z
					0.0F			 	//Size Increasement
				);
				railbedSide2.setRotationPoint(
					-8.0F+i*4, 			//X
					-6.5F,	//Y
					5.5F			//Z
				);

				railbedSide2.rotateAngleZ = ((float)Math.PI * 3F / 2F);
				railbedSide2.rotateAngleY = ((float)Math.PI * 3F / 2F);
			}
		}
    }

	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {
		int valid = module == null ? 1 : ((ModuleRailer)module).getRails();
	

		for (int i = 0; i < rails.length; i++) {
			rails[i].rotateAngleY = module == null ? 0 : ((ModuleRailer)module).getRailAngle(i);
		
			rails[i].isHidden = i >= valid;
		}
	}
}
