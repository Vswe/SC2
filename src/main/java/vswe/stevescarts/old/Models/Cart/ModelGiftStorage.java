package vswe.stevescarts.old.Models.Cart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.old.Helpers.ResourceHelper;
import vswe.stevescarts.vehicles.modules.ModuleBase;
import vswe.stevescarts.old.Modules.Storages.Chests.ModuleChest;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelGiftStorage extends ModelCartbase
{

	private static ResourceLocation texture = ResourceHelper.getResource("/models/giftStorageModel.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return texture;
	}		

	protected int getTextureHeight() {
		return 64;
	}

	ModelRenderer lid1;
	ModelRenderer lid2;

    public ModelGiftStorage()
    {

		lid1 = AddChest(false);
		lid2 = AddChest(true);
    }

	private ModelRenderer AddChest(boolean opposite) {
		ModelRenderer chestAnchor = new ModelRenderer(this);
		AddRenderer(chestAnchor);

		int offsetY = 0;
		if (opposite) {
			chestAnchor.rotateAngleY = (float)Math.PI;
			offsetY = 21;
		}

		ModelRenderer base = new ModelRenderer(this, 0, 7+offsetY);
		fixSize(base);
		chestAnchor.addChild(base);

		base.addBox(
			8, 	//X
			3, 	//Y
			2F,	 	//Z
			16,					//Size X
			6,					//Size Y
			4,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		base.setRotationPoint(
			-16.0F, 		//X
			-5.5F,			//Y
			-14.0F			//Z
		);

		ModelRenderer lid = new ModelRenderer(this, 0, offsetY);
		fixSize(lid);
		chestAnchor.addChild(lid);

		lid.addBox(
			8, 	//X
			-3, 	//Y
			-4F,	 	//Z
			16,					//Size X
			3,					//Size Y
			4,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		lid.setRotationPoint(
			-16.0F, 		//X
			-1.5F,			//Y
			-8F			//Z
		);

		ModelRenderer lock = new ModelRenderer(this, 0, 17+offsetY);
		fixSize(lock);
		lid.addChild(lock);

		lock.addBox(
			1F, 	//X
			1.5F, 	//Y
			0.5F,	 	//Z
			2,					//Size X
			3,					//Size Y
			1,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		lock.setRotationPoint(
			14.0F, 		//X
			-3F,			//Y
			-5.5F			//Z
		);

		return lid;
	}

	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {

		lid1.rotateAngleX = module == null ? 0 : -((ModuleChest)module).getChestAngle();
		lid2.rotateAngleX = module == null ? 0 : -((ModuleChest)module).getChestAngle();
		
	}
}
