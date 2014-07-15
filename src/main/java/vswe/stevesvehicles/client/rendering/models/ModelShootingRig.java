package vswe.stevesvehicles.client.rendering.models;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.module.ModuleBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelShootingRig extends ModelVehicle {
	private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/models/rigModel.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return TEXTURE;
	}		

    @Override
	protected int getTextureHeight() {
		return 16;
	}

    public ModelShootingRig() {
		ModelRenderer base = new ModelRenderer(this, 0, 0);
		addRenderer(base);

		base.addBox(
			-7, 	        //X
			-0.5F, 	        //Y
			-3,	 	        //Z
			14,				//Size X
			1,				//Size Y
			6,			    //Size Z
			0.0F
		);
		base.setRotationPoint(
			0.0F, 		    //X
			-5.5F,			//Y
			-0.0F			//Z
		);

		base.rotateAngleY = (float)Math.PI / 2;

		ModelRenderer pillar = new ModelRenderer(this, 0, 7);
		addRenderer(pillar);

		pillar.addBox(
			-2, 	        //X
			-2.5F, 	        //Y
			-2,	 	        //Z
			4,				//Size X
			5,				//Size Y
			4,			    //Size Z
			0.0F
		);
		pillar.setRotationPoint(
			0.0F, 		    //X
			-8F,			//Y
			-0.0F			//Z
		);

		ModelRenderer top = new ModelRenderer(this, 16, 7);
		addRenderer(top);

		top.addBox(
			-3, 	    //X
			-1F, 	    //Y
			-3,	 	    //Z
			6,			//Size X
			2,			//Size Y
			6,			//Size Z
			0.0F
		);
		top.setRotationPoint(
			0.0F, 		    //X
			-11F,			//Y
			-0.0F			//Z
		);
    }
}
