package vswe.stevescarts.Modules.Addons.Mobdetectors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayerMP;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;

public class ModulePlayer extends ModuleMobdetector {
	public ModulePlayer(MinecartModular cart) {
		super(cart);
	}

	public String getName() {
		return Localization.MODULES.ADDONS.DETECTOR_PLAYERS.translate();
	}
	public boolean isValidTarget(Entity target) {
		return
		(
			target instanceof EntityPlayerMP
			||
			(
				(target instanceof EntityTameable)
				&&
				((EntityTameable)target).isTamed()
			)			
		)
		;
	}
}