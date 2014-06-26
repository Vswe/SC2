package vswe.stevesvehicles.client.rendering.models;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.modules.ModuleBase;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleSeat;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelSeat extends ModelVehicle
{

	private static ResourceLocation texture = ResourceHelper.getResource("/models/chairModel.png");
	
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

	ModelRenderer sit ;
    public ModelSeat()
    {

		sit = new ModelRenderer(this, 0, 0);
		AddRenderer(sit);

		sit.addBox(
			-4, 	//X
			-2F, 	//Y
			-2,	 	//Z
			8,					//Size X
			4,					//Size Y
			4,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		sit.setRotationPoint(
			0, 		//X
			1F,			//Y
			0F			//Z
		);

		ModelRenderer back = new ModelRenderer(this, 0, 8);
		sit.addChild(back);
		fixSize(back);

		back.addBox(
			-4, 	//X
			-2F, 	//Y
			-1,	 	//Z
			8,					//Size X
			12,					//Size Y
			2,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		back.setRotationPoint(
			0, 		//X
			-8F,			//Y
			3F			//Z
		);
    }

	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {
		sit.rotateAngleY = module == null ? (float)Math.PI / 2 : ((ModuleSeat)module).getChairAngle() + ( ((ModuleSeat)module).useRelativeRender() ? 0 : yaw);
	}
}
