package vswe.stevesvehicles.client.rendering.models.cart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.client.rendering.models.ModelVehicle;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.cart.tool.ModuleDrill;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelDrill extends ModelVehicle {
	@Override
	public ResourceLocation getResource(ModuleBase module) {
		return resource;
	}

	private ModelRenderer drillAnchor;
	private ResourceLocation resource;

    @Override
	protected int getTextureWidth() {
		return 32;
	}
    @Override
	protected int getTextureHeight() {
		return 32;
	}

    public ModelDrill(ResourceLocation resource) {
		this.resource = resource;

		drillAnchor = new ModelRenderer(this);

		addRenderer(drillAnchor);
		drillAnchor.rotateAngleY = ((float)Math.PI * 3F / 2F);
		int srcY = 0;
		for (int i = 0; i < 6; i++) {
			ModelRenderer drill = fixSize(new ModelRenderer(this, 0, srcY));

			drillAnchor.addChild(drill);

			drill.addBox(
				-3 + i * 0.5F,			//X
				-3 + i * 0.5F, 			//Y
				i, 						//Z
				6-i, 					//Size X
				6-i,					//Size Y
				1,						//Size Z
				0.0F
			);
			drill.setRotationPoint(
				0,					//X
				0,					//Y
				cartLength / 2 + 1	//Z
			);

			srcY += 7-i;
		}
    }

    @Override
	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {
		for (Object drill : drillAnchor.childModels) {
			((ModelRenderer)drill).rotateAngleZ =  module == null ? 0 : ((ModuleDrill)module).getDrillRotation();
		}
	}
}
