package vswe.stevescarts.old.Models.Cart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.old.Helpers.ResourceHelper;
import vswe.stevescarts.vehicles.modules.ModuleBase;
import vswe.stevescarts.old.Modules.Engines.ModuleCoalBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelEngineInside extends ModelEngineBase
{
	
	
	private static ResourceLocation[] textures;
	static {
		textures = new ResourceLocation[5];
		textures[0] = ResourceHelper.getResource("/models/engineModelBack.png");
		for (int i = 1; i < textures.length; i++) {
			textures[i] = ResourceHelper.getResource("/models/engineModelFire" + i + ".png");	
		}
	}
	
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		int index = module == null ? 0 : ((ModuleCoalBase)module).getFireIndex();
		return textures[index];
	}
	
	

	protected int getTextureWidth() {
		return 8;
	}
	protected int getTextureHeight() {
		return 4;
	}

    public ModelEngineInside()
    {

		ModelRenderer back = new ModelRenderer(this, 0, 0);
		anchor.addChild(back);
		fixSize(back);

		back.addBox(
			-3.5F, 	//X
			-2F, 	//Y
			0.0F,	 	//Z
			7,					//Size X
			4,					//Size Y
			0,			     	//Size Z
			0.0F			 	//Size Increasement
		);
		back.setRotationPoint(
			0, 		//X
			-0.5F,			//Y
			0.3F			//Z
		);	
		
	}


}
