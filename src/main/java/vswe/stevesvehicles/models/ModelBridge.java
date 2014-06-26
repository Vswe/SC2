package vswe.stevesvehicles.models;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.modules.ModuleBase;
import vswe.stevesvehicles.old.Modules.Workers.ModuleBridge;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.vehicles.entities.EntityModularCart;

@SideOnly(Side.CLIENT)
public class ModelBridge extends ModelVehicle
{

	private static ResourceLocation normal = ResourceHelper.getResource("/models/aiModelNormal.png");
	private static ResourceLocation down = ResourceHelper.getResource("/models/aiModelDown.png");
	private static ResourceLocation up = ResourceHelper.getResource("/models/aiModelUp.png");

	private static ResourceLocation normalWarning = ResourceHelper.getResource("/models/aiModelNormalWarning.png");
	private static ResourceLocation downWarning = ResourceHelper.getResource("/models/aiModelDownWarning.png");
	private static ResourceLocation upWarning = ResourceHelper.getResource("/models/aiModelUpWarning.png");
	
	public ResourceLocation getResource(ModuleBase module) {
		if (module == null) {
			return normal;
		}
		
		boolean needBridge;
		int yDif;


		needBridge = ((ModuleBridge)module).needBridge();
		Vec3 next = ((ModuleBridge)module).getNextblock();
		int y = (int) next.yCoord;
		yDif =  ((EntityModularCart)module.getVehicle().getEntity()).getYTarget() - y;


		if (needBridge) {
			if (yDif > 0)
			{
			   return upWarning;
			}
			else if (yDif < 0)
			{
				return downWarning;
			}
			else
			{
				return normalWarning;
			}
		}else{
			if (yDif > 0)
			{
			   return up;
			}
			else if (yDif < 0)
			{
				return down;
			}
			else
			{
				return normal;
			}
		}
	}

	private ModelRenderer drillAnchor;

	protected int getTextureWidth() {
		return 8;
	}
	protected int getTextureHeight() {
		return 8;
	}

    public ModelBridge()
    {
		ModelRenderer side1 = new ModelRenderer(this, 0, 0);
		AddRenderer(side1);

        side1.addBox(
			1F,		//X
			3, 		//Y
			0.5F, 					//Z
			2, 						//Size X
			6,						//Size Y
			1,						//Size Z
			0.0F					//Size Increasement
		);
        side1.setRotationPoint(
			-11.5F,	//X
			-6.0F,			//Y
			8.0F					//Z
		);

		side1.rotateAngleY = ((float)Math.PI / 2F);

		ModelRenderer side2 = new ModelRenderer(this, 0, 0);
		AddRenderer(side2);

        side2.addBox(
			1F,		//X
			3, 		//Y
			0.5F, 					//Z
			2, 						//Size X
			6,						//Size Y
			1,						//Size Z
			0.0F					//Size Increasement
		);
        side2.setRotationPoint(
			-11.5F,	//X
			-6.0F,			//Y
			-4.0F					//Z
		);

		side2.rotateAngleY = ((float)Math.PI / 2F);
    }
}
