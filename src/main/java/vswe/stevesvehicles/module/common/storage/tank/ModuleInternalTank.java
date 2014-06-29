package vswe.stevesvehicles.module.common.storage.tank;
import vswe.stevesvehicles.vehicle.VehicleBase;


public class ModuleInternalTank extends ModuleTank{
	public ModuleInternalTank(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	@Override
	protected int getTankSize() {
		return 4000;
	}

	@Override
	public boolean hasVisualTank() {
		return false;
	}

}