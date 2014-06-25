package vswe.stevescarts.old.Models.Cart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.old.Helpers.ResourceHelper;
import vswe.stevescarts.old.Modules.ModuleBase;
import vswe.stevescarts.old.Modules.Addons.ModuleShield;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ModelShield extends ModelCartbase
{

	private static ResourceLocation texture = ResourceHelper.getResource("/models/shieldModel.png");
	
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return texture;
	}		


	protected int getTextureWidth() {
		return 8;
	}
	protected int getTextureHeight() {
		return 4;
	}

	private ModelRenderer[][] shieldAnchors;
	private ModelRenderer[][] shields;

    public ModelShield()
    {

		shields = new ModelRenderer[4][5];
		shieldAnchors = new ModelRenderer[shields.length][shields[0].length];
		for (int i = 0; i < shields.length; i++) {
			for (int j = 0; j < shields[i].length; j++) {
				shieldAnchors[i][j] = new ModelRenderer(this);
				AddRenderer(shieldAnchors[i][j]);

				shields[i][j] = new ModelRenderer(this, 0, 0);
				fixSize(shields[i][j]);
				shieldAnchors[i][j].addChild(shields[i][j] );

				shields[i][j].addBox(
					-1F, //X
					-1F,	 //Y
					-1F, 			 //Z
					2,		 //Size X
					2,		 //Size Y
					2,			     //Size Z
					0.0F			 //Size Increasement
				);
				shields[i][j].setRotationPoint(
					0.0F, 			//X
					0,	//Y
					0.0F			//Z
				);
			}
		}
    }

    public void render(Render render,ModuleBase module, float yaw, float pitch, float roll, float mult, float partialtime)
    {

		if (render == null || module == null || ((ModuleShield)module).hasShield()) {
			super.render(render,module,yaw,pitch,roll,mult, partialtime);
		}
    }

	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {
		float shieldAngle = module == null ? 0 : ((ModuleShield)module).getShieldAngle();
		float shieldDistance = module == null ? 18 : ((ModuleShield)module).getShieldDistance();

		for (int i = 0; i < shields.length; i++) {
			for (int j = 0; j < shields[i].length; j++) {
				float a = shieldAngle + (float)(Math.PI * 2) * (j / (float)shields[i].length + i / (float)shields.length);
				a = (float)(a % (Math.PI * 100));
				shieldAnchors[i][j].rotateAngleY = a;
				shields[i][j].rotationPointY = ((float)Math.sin(a/5) * 3F + (i - (shields.length - 1) / 2F) * 5F - 5F) * shieldDistance / 18F;
				shields[i][j].rotationPointZ = shieldDistance;
			}
		}
	}
}
