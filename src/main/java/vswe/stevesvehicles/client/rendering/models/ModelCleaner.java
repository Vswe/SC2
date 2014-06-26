package vswe.stevesvehicles.client.rendering.models;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.modules.ModuleBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelCleaner extends ModelVehicle
{
	private static ResourceLocation texture = ResourceHelper.getResource("/models/cleanerModel.png");
	
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

    public ModelCleaner()
    {
		ModelRenderer box = new ModelRenderer(this, 0, 0);
		AddRenderer(box);

		box.addBox(
			-4, 	//X
			-3, 	//Y
			-4,	 	//Z
			8,					//Size X
			6,					//Size Y
			8,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		box.setRotationPoint(
			4.0F, 		//X
			-0.0F,			//Y
			-0.0F			//Z
		);

		for (int i = 0; i < 2; i++) {
			ModelRenderer sidetube = new ModelRenderer(this, 0, 14);
			AddRenderer(sidetube);

			sidetube.addBox(
				-2, 	//X
				-2, 	//Y
				-1,	 	//Z
				4,					//Size X
				4,					//Size Y
				2,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			sidetube.setRotationPoint(
				4, 		//X
				-0.0F,			//Y
				-5.0F * (i * 2 -1)			//Z
			);
		}

		ModelRenderer tube = new ModelRenderer(this, 0, 14);
		AddRenderer(tube);

		tube.addBox(
			-2, 	//X
			-2, 	//Y
			-1,	 	//Z
			4,					//Size X
			4,					//Size Y
			2,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		tube.setRotationPoint(
			-1F, 		//X
			0.0F,			//Y
			0			//Z
		);

		tube.rotateAngleY = (float)Math.PI / 2;

		for (int i = 0; i < 2; i++) {
			ModelRenderer endtube = new ModelRenderer(this, 0, 14);
			AddRenderer(endtube);

			endtube.addBox(
				-2, 	//X
				-2, 	//Y
				-1,	 	//Z
				4,					//Size X
				4,					//Size Y
				2,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			endtube.setRotationPoint(
				-7, 		//X
				-0.0F,			//Y
				-3.0F * (i * 2 -1)			//Z
			);

			endtube.rotateAngleY = (float)Math.PI / 2;
		}

		ModelRenderer connectiontube = new ModelRenderer(this, 0, 20);
		AddRenderer(connectiontube);

		connectiontube.addBox(
			-5, 	//X
			-5, 	//Y
			-1,	 	//Z
			10,					//Size X
			4,					//Size Y
			4,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		connectiontube.setRotationPoint(
			-5F, 		//X
			3F,			//Y
			0			//Z
		);

		connectiontube.rotateAngleY = (float)Math.PI / 2;

		for (int i = 0; i < 2; i++) {
			ModelRenderer externaltube = new ModelRenderer(this, 0, 14);
			AddRenderer(externaltube);

			externaltube.addBox(
				-2, 	//X
				-2, 	//Y
				-1,	 	//Z
				4,					//Size X
				4,					//Size Y
				2,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			externaltube.setRotationPoint(
				-10.95F, 		//X
				-0.0F,			//Y
				-3.05F * (i * 2 -1)			//Z
			);

			externaltube.rotateAngleY = (float)Math.PI / 2;
		}
    }


}
