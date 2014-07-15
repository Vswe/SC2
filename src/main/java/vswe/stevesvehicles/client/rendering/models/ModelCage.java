package vswe.stevesvehicles.client.rendering.models;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.module.ModuleBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelCage extends ModelVehicle {
	
	private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/models/cageModel.png");
	
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

    private static final int CAGE_HEIGHT_TALL = 26;
    private static final int CAGE_HEIGHT_SHORT = 14;
	private final int cageHeight;
    public ModelCage(boolean isPlaceholder) {
		cageHeight = isPlaceholder ? CAGE_HEIGHT_SHORT : CAGE_HEIGHT_TALL;

		for (float x = -9; x <= 9; x+=9*2 / 3F) {			
			if (Math.abs(x) != 9) {
				createBar(x,7);
				createBar(x,-7);				
			}
			createTopBarShort(x);
		}

		for (float z = -7; z <= 7; z+=7*2/ 3F) {
			createBar(9,z);
			createBar(-9,z);
			createTopBarLong(z);
		}
		
    }

	private void createBar(float offsetX, float offsetZ) {

		ModelRenderer bar = new ModelRenderer(this, 0, 0);
		addRenderer(bar);

		bar.addBox(
			-0.5F, 	            //X
			-cageHeight / 2F, 	//Y
			-0.5F,	 	        //Z
			1,					//Size X
			cageHeight,			//Size Y
			1,			     	//Size Z
			0.0F
		);
		bar.setRotationPoint(
			offsetX, 		        //X
			-cageHeight / 2F - 4,	//Y
			offsetZ			        //Z
		);
		
	}
	
	private void createTopBarLong(float offsetZ) {

		ModelRenderer bar = new ModelRenderer(this, 0, 0);
		addRenderer(bar);

		bar.addBox(
			-0.5F, 	            //X
			-9.5F, 	            //Y
			-0.5F,	 	        //Z
			1,					//Size X
			19,					//Size Y
			1,			     	//Size Z
			0.0F
		);
		bar.setRotationPoint(
			0.005F, 		        //X
			-cageHeight -4.005F,	//Y
			offsetZ + 0.005F		//Z
		);
		
		bar.rotateAngleZ = (float)(Math.PI / 2);
	}	
	
	private void createTopBarShort(float offsetX) {

		ModelRenderer bar = new ModelRenderer(this, 0, 0);
		addRenderer(bar);

		bar.addBox(
			-0.5F, 	            //X
			-7.5F, 	            //Y
			-0.5F,	 	        //Z
			1,					//Size X
			15,					//Size Y
			1,			     	//Size Z
			0.0F
		);
		bar.setRotationPoint(
			offsetX - 0.005F, 		//X
			-cageHeight - 4 + 0.005F,			//Y
			-0.005F			//Z
		);
		
		bar.rotateAngleX = (float)(Math.PI / 2);
	}	

}
