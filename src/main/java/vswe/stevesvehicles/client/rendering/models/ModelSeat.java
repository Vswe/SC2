package vswe.stevesvehicles.client.rendering.models;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.common.attachment.ModuleSeat;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelSeat extends ModelVehicle {

	private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/models/chairModel.png");
	
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

	private ModelRenderer base;
    public ModelSeat() {

		base = new ModelRenderer(this, 0, 0);
		addRenderer(base);

		base.addBox(
            -4,         //X
            -2F,        //Y
            -2,         //Z
            8,          //Size X
            4,          //Size Y
            4,          //Size Z
            0.0F
        );
		base.setRotationPoint(
            0,              //X
            1F,             //Y
            0F              //Z
        );

		ModelRenderer back = new ModelRenderer(this, 0, 8);
		base.addChild(back);
		fixSize(back);

		back.addBox(
			-4, 	        //X
			-2F, 	        //Y
			-1,	 	        //Z
			8,				//Size X
			12,				//Size Y
			2,			    //Size Z
			0.0F
		);
		back.setRotationPoint(
			0, 		        //X
			-8F,			//Y
			3F			    //Z
		);
    }

    @Override
	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {
		base.rotateAngleY = module == null ? (float)Math.PI / 2 : ((ModuleSeat)module).getChairAngle() + (((ModuleSeat)module).useRelativeRender() ? 0 : yaw);
	}
}
