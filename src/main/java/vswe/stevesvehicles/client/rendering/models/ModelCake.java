package vswe.stevesvehicles.client.rendering.models;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.common.attachment.ModuleCakeServer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelCake extends ModelVehicle
{

	
	@Override
	protected int getTextureHeight() {
		return 256;
	}

	private static ResourceLocation texture = ResourceHelper.getResource("/models/cakeModel.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return texture;
	}		
	
	private ModelRenderer[] cakes;

    public ModelCake()
    {
    	cakes = new ModelRenderer[6];
    	for (int i = 0; i < cakes.length; i++) {
    		cakes[i] = createCake(6 - i);
    	}
    }
    
    private ModelRenderer createCake(int slices) {
		ModelRenderer cake = new ModelRenderer(this, 0, 22 * (6 - slices));
		AddRenderer(cake);

		cake.addBox(
				-7, 	//X
				-4, 	//Y
				-7,	 	//Z
				2 * slices + (slices == 6 ? 2 : 1),					//Size X
				8,					//Size Y
				14,			     	//Size Z
				0.0F			 	//Size Increasement
			);
		cake.setRotationPoint(
			0, 		//X
			-9,			//Y
			0			//Z
		);
		
		return cake;
    }

@Override
public void applyEffects(ModuleBase module, float yaw, float pitch, float roll) {
	int count;
	if (module != null) {
		count = ((ModuleCakeServer)module).getRenderSliceCount();
	}else{
		count = 6;
	}
	
    for (int i = 0; i < cakes.length; i++) {
    	cakes[i].isHidden = (6 - i) != count;
    }
}


}
