package vswe.stevesvehicles.old.Models.Cart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.modules.ModuleBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelSolarPanelBase extends ModelSolarPanel
{
	
	private static ResourceLocation texture = ResourceHelper.getResource("/models/panelModelBase.png");
	
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

    public ModelSolarPanelBase()
    {
		ModelRenderer base = new ModelRenderer(this, 0, 0);
		AddRenderer(base);

		base.addBox(
			-1, 	//X
			-5, 	//Y
			-1,	 	//Z
			2,					//Size X
			10,					//Size Y
			2,			     	//Size Z
			0.0F			 	//Size Increasement
		);

		base.setRotationPoint(
			0, 		//X
			-4.5F,			//Y
			0			//Z
		);

		ModelRenderer moving = createMovingHolder(8,0);
		moving.addBox(
			-2, 	//X
			-3.5F, 	//Y
			-2,	 	//Z
			4,					//Size X
			7,					//Size Y
			4,			     	//Size Z
			0.0F			 	//Size Increasement
		);

		ModelRenderer top = new ModelRenderer(this, 0, 12);
		fixSize(top);
		moving.addChild(top);

		top.addBox(
			-6, 	//X
			-1.5F, 	//Y
			-2,	 	//Z
			12,					//Size X
			3,					//Size Y
			4,			     	//Size Z
			0.0F			 	//Size Increasement
		);

		top.setRotationPoint(
			0, 		//X
			-5F,			//Y
			0			//Z
		);
    }
}
