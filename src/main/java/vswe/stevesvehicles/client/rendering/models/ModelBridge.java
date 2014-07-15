package vswe.stevesvehicles.client.rendering.models;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.cart.attachment.ModuleBridge;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;

@SideOnly(Side.CLIENT)
public class ModelBridge extends ModelVehicle {

	private static final ResourceLocation NORMAL = ResourceHelper.getResource("/models/aiModelNormal.png");
	private static final ResourceLocation DOWN = ResourceHelper.getResource("/models/aiModelDown.png");
	private static final ResourceLocation UP = ResourceHelper.getResource("/models/aiModelUp.png");

	private static final ResourceLocation NORMAL_WARNING = ResourceHelper.getResource("/models/aiModelNormalWarning.png");
	private static final ResourceLocation DOWN_WARNING = ResourceHelper.getResource("/models/aiModelDownWarning.png");
	private static final ResourceLocation UP_WARNING = ResourceHelper.getResource("/models/aiModelUpWarning.png");

    @Override
	public ResourceLocation getResource(ModuleBase module) {
		if (module == null) {
			return NORMAL;
		}
		
		boolean needBridge;
		int yDif;


		needBridge = ((ModuleBridge)module).needBridge();
		Vec3 next = ((ModuleBridge)module).getNextBlock();
		int y = (int) next.yCoord;
		yDif =  ((EntityModularCart)module.getVehicle().getEntity()).getYTarget() - y;


		if (needBridge) {
			if (yDif > 0) {
			   return UP_WARNING;
			}else if (yDif < 0) {
				return DOWN_WARNING;
			}else {
				return NORMAL_WARNING;
			}
		}else{
			if (yDif > 0) {
			   return UP;
			}else if (yDif < 0) {
				return DOWN;
			}else {
				return NORMAL;
			}
		}
	}

    @Override
	protected int getTextureWidth() {
		return 8;
	}
    @Override
	protected int getTextureHeight() {
		return 8;
	}

    public ModelBridge() {
		ModelRenderer side1 = new ModelRenderer(this, 0, 0);
		addRenderer(side1);

        side1.addBox(
			1F,		                //X
			3, 		                //Y
			0.5F, 					//Z
			2, 						//Size X
			6,						//Size Y
			1,						//Size Z
			0.0F
		);
        side1.setRotationPoint(
			-11.5F,	        //X
			-6.0F,			//Y
			8.0F			//Z
		);

		side1.rotateAngleY = ((float)Math.PI / 2F);

		ModelRenderer side2 = new ModelRenderer(this, 0, 0);
		addRenderer(side2);

        side2.addBox(
			1F,		    //X
			3, 		    //Y
			0.5F, 		//Z
			2, 			//Size X
			6,			//Size Y
			1,			//Size Z
			0.0F
		);
        side2.setRotationPoint(
			-11.5F,	    //X
			-6.0F,		//Y
			-4.0F		//Z
		);

		side2.rotateAngleY = ((float)Math.PI / 2F);
    }
}
