package vswe.stevesvehicles.module.common.addon.mobdetector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityBat;
import vswe.stevesvehicles.localization.entry.module.LocalizationShooter;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleBat extends ModuleEntityDetector {
	public ModuleBat(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	public String getName() {
		return LocalizationShooter.BAT_TITLE.translate();
	}
	public boolean isValidTarget(Entity target) {
		return
		(
			target instanceof EntityBat		
		)
		;
	}
}