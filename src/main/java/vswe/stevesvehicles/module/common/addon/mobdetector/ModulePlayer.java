package vswe.stevesvehicles.module.common.addon.mobdetector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayerMP;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.Localization;

public class ModulePlayer extends ModuleEntityDetector {
	public ModulePlayer(VehicleBase vehicleBase) {
		super(vehicleBase);
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