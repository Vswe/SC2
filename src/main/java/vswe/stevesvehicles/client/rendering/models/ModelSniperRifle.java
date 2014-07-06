package vswe.stevesvehicles.client.rendering.models;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.common.attachment.ModuleShooterAdvanced;

@SideOnly(Side.CLIENT)
public class ModelSniperRifle extends ModelGun {
	private ModelRenderer anchor;
	private ModelRenderer gun;

    public ModelSniperRifle() {
		anchor = new ModelRenderer(this);
		addRenderer(anchor);

		gun = createGun(anchor);
	}

    @Override
	public void applyEffects(ModuleBase module,  float yaw, float pitch, float roll) {	
		gun.rotateAngleZ = module == null ? 0 : ((ModuleShooterAdvanced)module).getPipeRotation(0);
		anchor.rotateAngleY = module == null ? 0 : (float)Math.PI + ((ModuleShooterAdvanced)module).getRifleDirection() + yaw;
	}
}
