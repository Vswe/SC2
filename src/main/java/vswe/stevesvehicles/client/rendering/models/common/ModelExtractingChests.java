package vswe.stevesvehicles.client.rendering.models.common;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.client.rendering.models.ModelVehicle;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.common.storage.chest.ModuleExtractingChests;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelExtractingChests extends ModelVehicle {
	
	private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/models/codeSideChestsModel.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return TEXTURE;
	}		

	private ModelRenderer lid1;
    private ModelRenderer lid2;
    private ModelRenderer base1;
    private ModelRenderer base2;

    public ModelExtractingChests() {
		ModelRenderer[] temp = AddChest(false);
		base1 = temp[0];
		lid1 = temp[1];
		temp = AddChest(true);
		base2 = temp[0];
		lid2 = temp[1];
    }

	private ModelRenderer[] AddChest(boolean opposite) {
		ModelRenderer chestAnchor = new ModelRenderer(this);
		addRenderer(chestAnchor);

		if (opposite) {
			chestAnchor.rotateAngleY = (float)Math.PI;
		}

		ModelRenderer base = new ModelRenderer(this, 0, 17);
		fixSize(base);
		chestAnchor.addChild(base);

		base.addBox(
			8, 	        //X
			3, 	        //Y
			2F,	 	    //Z
			16,			//Size X
			6,			//Size Y
			14,			//Size Z
			0.0F
		);
		base.setRotationPoint(
			-16.0F, 		//X
			-5.5F,			//Y
			-14.0F			//Z
		);

		ModelRenderer lid = new ModelRenderer(this, 0, 0);
		fixSize(lid);
		chestAnchor.addChild(lid);

		lid.addBox(
			8, 	        //X
			-3, 	    //Y
			-14F,	 	//Z
			16,			//Size X
			3,			//Size Y
			14,			//Size Z
			0.0F
		);
		lid.setRotationPoint(
			-16.0F, 	//X
			-1.5F,		//Y
			2F			//Z
		);

		ModelRenderer lock = new ModelRenderer(this, 0, 37);
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
			14.0F, 		//X
			-3F,		//Y
			-15.5F		//Z
		);

		return new ModelRenderer[] {base, lid};
	}

    @Override
	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {
		if (module == null) {
			lid1.rotateAngleX = 0;
			lid2.rotateAngleX = 0;
			lid1.rotationPointZ = 2;
			lid2.rotationPointZ = 2;
			base1.rotationPointZ = -14;
			base2.rotationPointZ = -14;			
		}else{
			ModuleExtractingChests chest = (ModuleExtractingChests)module;

			lid1.rotateAngleX = -chest.getChestAngle();
			lid2.rotateAngleX = -chest.getChestAngle();

			lid1.rotationPointZ = chest.getChestOffset() + 16;
			lid2.rotationPointZ = chest.getChestOffset() + 16;
			base1.rotationPointZ = chest.getChestOffset();
			base2.rotationPointZ = chest.getChestOffset();
		}
	}
}
