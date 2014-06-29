package vswe.stevesvehicles.module.common.addon.mobdetector;
import net.minecraft.entity.Entity;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.module.common.addon.ModuleAddon;

public abstract class ModuleEntityDetector extends ModuleAddon {
	public ModuleEntityDetector(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	public abstract String getName();
	public abstract boolean isValidTarget(Entity target);

}