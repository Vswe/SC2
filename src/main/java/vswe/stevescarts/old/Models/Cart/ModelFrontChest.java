package vswe.stevescarts.old.Models.Cart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.old.Helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.old.Modules.Storages.Chests.ModuleChest;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelFrontChest extends ModelCartbase
{
	
	private static ResourceLocation texture = ResourceHelper.getResource("/models/frontChestModel.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return texture;
	}		


	protected int getTextureHeight() {
		return 32;
	}

	ModelRenderer lid;

    public ModelFrontChest()
    {
		lid = AddChest();
    }

	private ModelRenderer AddChest() {
		ModelRenderer chestAnchor = new ModelRenderer(this);
		AddRenderer(chestAnchor);

		chestAnchor.rotateAngleY = (float)Math.PI / 2;
		chestAnchor.setRotationPoint(
			-3.5F, 		//X
			0,			//Y
			0			//Z
		);

		ModelRenderer base = new ModelRenderer(this, 0, 11);
		fixSize(base);
		chestAnchor.addChild(base);

		base.addBox(
			7, 	//X
			3, 	//Y
			4F,	 	//Z
			14,					//Size X
			6,					//Size Y
			8,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		base.setRotationPoint(
			-14.0F, 		//X
			-5.5F,			//Y
			-18.5F			//Z
		);

		ModelRenderer lid = new ModelRenderer(this, 0, 0);
		fixSize(lid);
		chestAnchor.addChild(lid);

		lid.addBox(
			7, 	//X
			-3, 	//Y
			-8F,	 	//Z
			14,					//Size X
			3,					//Size Y
			8,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		lid.setRotationPoint(
			-14.0F, 		//X
			-1.5F,			//Y
			-6.5F			//Z
		);

		ModelRenderer lock = new ModelRenderer(this, 0, 25);
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
			12.0F, 		//X
			-3F,			//Y
			-9.5F			//Z
		);

		return lid;
	}

	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {
		lid.rotateAngleX = module == null ? 0 : -((ModuleChest)module).getChestAngle();		
	}
}
