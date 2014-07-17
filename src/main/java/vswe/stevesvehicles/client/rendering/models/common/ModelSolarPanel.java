package vswe.stevesvehicles.client.rendering.models.common;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import vswe.stevesvehicles.client.rendering.models.ModelVehicle;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.common.engine.ModuleSolarTop;
@SideOnly(Side.CLIENT)
public abstract class ModelSolarPanel extends ModelVehicle {
	protected ModelRenderer createMovingHolder(int x, int y) {
		ModelRenderer moving = new ModelRenderer(this, x, y);

		this.moving = moving;
		addRenderer(moving);
		return moving;
	}

	private ModelRenderer moving;

    @Override
	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {
		moving.rotationPointY = module == null ? -4 : ((ModuleSolarTop)module).getMovingLevel();
	}
}
