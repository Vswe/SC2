package vswe.stevescarts.old.Models.Cart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.old.Helpers.ResourceHelper;
import vswe.stevescarts.vehicles.modules.ModuleBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelTrackRemover extends ModelCartbase
{

	
	private static ResourceLocation texture = ResourceHelper.getResource("/models/removerModel.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return texture;
	}		
	

    public ModelTrackRemover()
    {
		super();

		ModelRenderer base = new ModelRenderer(this, 0, 0);
		AddRenderer(base);

		base.addBox(
			-5, 	//X
			-5, 	//Y
			-0.5F,	 	//Z
			10,					//Size X
			10,					//Size Y
			1,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		base.setRotationPoint(
			0.0F, 		//X
			-5.5F,			//Y
			-0.0F			//Z
		);

		base.rotateAngleX = (float)Math.PI / 2;

		ModelRenderer pipe = new ModelRenderer(this, 0, 11);
		AddRenderer(pipe);

		pipe.addBox(
			-2.5F, 	//X
			-2.5F, 	//Y
			-2.5F,	 	//Z
			6,					//Size X
			5,					//Size Y
			5,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		pipe.setRotationPoint(
			0.0F, 		//X
			-9.5F,			//Y
			-0.0F			//Z
		);

		pipe.rotateAngleZ = (float)Math.PI / 2;

		ModelRenderer pipe2 = new ModelRenderer(this, 0, 21);
		pipe.addChild(pipe2);
		fixSize(pipe2);

		pipe2.addBox(
			-2.5F, 	//X
			-2.5F, 	//Y
			-2.5F,	 	//Z
			19,					//Size X
			5,					//Size Y
			5,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		pipe2.setRotationPoint(
			0.005F, 		//X
			-0.005F,			//Y
			-0.005F			//Z
		);
		pipe2.rotateAngleZ = (float)-Math.PI / 2;

		ModelRenderer pipe3 = new ModelRenderer(this, 22, 0);
		pipe2.addChild(pipe3);
		fixSize(pipe3);

		pipe3.addBox(
			-2.5F, 	//X
			-2.5F, 	//Y
			-2.5F,	 	//Z
			14,					//Size X
			5,					//Size Y
			5,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		pipe3.setRotationPoint(
			14F+0.005F, 		//X
			-0.005F,			//Y
			0.005F			//Z
		);

		pipe3.rotateAngleZ = (float)Math.PI / 2;

		ModelRenderer end = new ModelRenderer(this, 0, 31);
		pipe3.addChild(end);
		fixSize(end);

		end.addBox(
			-7, 	//X
			-11, 	//Y
			-0.5F,	 	//Z
			14,					//Size X
			14,					//Size Y
			1,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		end.setRotationPoint(
			12F, 		//X
			0,			//Y
			-0.0F			//Z
		);

		end.rotateAngleY = (float)Math.PI / 2;
    }
}
