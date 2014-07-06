package vswe.stevesvehicles.client.rendering.models;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.common.storage.chest.ModuleChest;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelFrontChest extends ModelVehicle {
	
	private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/models/frontChestModel.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return TEXTURE;
	}		

    @Override
	protected int getTextureHeight() {
		return 32;
	}

	private ModelRenderer lid;

    public ModelFrontChest() {
		lid = addChest();
    }

	private ModelRenderer addChest() {
		ModelRenderer chestAnchor = new ModelRenderer(this);
		addRenderer(chestAnchor);

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
			7, 	        //X
			3, 	        //Y
			4F,	 	    //Z
			14,		    //Size X
			6,		    //Size Y
			8,		    //Size Z
			0.0F
		);
		base.setRotationPoint(
			-14.0F, 	//X
			-5.5F,		//Y
			-18.5F		//Z
		);

		ModelRenderer lid = new ModelRenderer(this, 0, 0);
		fixSize(lid);
		chestAnchor.addChild(lid);

		lid.addBox(
			7, 	        //X
			-3, 	    //Y
			-8F,	 	//Z
			14,			//Size X
			3,			//Size Y
			8,			//Size Z
			0.0F
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
			1F, 	    //X
			1.5F, 	    //Y
			0.5F,	 	//Z
			2,			//Size X
			3,			//Size Y
			1,			//Size Z
			0.0F
		);
		lock.setRotationPoint(
			12.0F, 		//X
			-3F,			//Y
			-9.5F			//Z
		);

		return lid;
	}

    @Override
	public void applyEffects(ModuleBase module, float yaw, float pitch, float roll) {
		lid.rotateAngleX = module == null ? 0 : -((ModuleChest)module).getChestAngle();		
	}
}
