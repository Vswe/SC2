package vswe.stevesvehicles.client.rendering.models;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.cart.tool.ModuleWoodcutter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelWoodCutter extends ModelVehicle
{

	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return resource;
	}	
	
	protected int getTextureWidth() {
		return 16;
	}
	protected int getTextureHeight() {
		return 8;
	}

	private ModelRenderer[] anchors;
	private ResourceLocation resource;
    public ModelWoodCutter(ResourceLocation resource)
    {
    	this.resource = resource;
    	
		anchors = new ModelRenderer[5];
		for (int i = -2; i <= 2; i++) {
			ModelRenderer anchor = new ModelRenderer(this);
			anchors[i+2] = anchor;
			AddRenderer(anchor);

			ModelRenderer main = new ModelRenderer(this, 0, 0);
			anchor.addChild(main);
			fixSize(main);

			main.addBox(
				-3.5F, 	//X
				-1.5F, 	//Y
				-0.5F,	 	//Z
				7,					//Size X
				3,					//Size Y
				1,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			main.setRotationPoint(
				-13, 		//X
				0,			//Y
				i*2			//Z
			);

			ModelRenderer tip = new ModelRenderer(this, 0, 4);
			main.addChild(tip);
			fixSize(tip);

			tip.addBox(
				-0.5F, 	//X
				-0.5F, 	//Y
				-0.5F,	 	//Z
				1,					//Size X
				1,					//Size Y
				1,			     	//Size Z
				0.0F			 	//Size Increasement
			);
			tip.setRotationPoint(
				-4F, 		//X
				0,			//Y
				0			//Z
			);
		}
    }

	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {
		float commonAngle = module == null ? 0 : ((ModuleWoodcutter)module).getCutterAngle();
		for (int i = 0; i < anchors.length; i++) {
			float specificAngle;

			if (i % 2 == 0) {
				specificAngle = (float)Math.sin(commonAngle);
			}else{
				specificAngle = (float)Math.cos(commonAngle);
			}

			anchors[i].rotationPointX = (specificAngle * 1.25F);
		}
	}
}
