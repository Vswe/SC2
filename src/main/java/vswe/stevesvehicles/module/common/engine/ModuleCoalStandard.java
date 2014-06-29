package vswe.stevesvehicles.module.common.engine;

import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleCoalStandard extends ModuleCoalBase {
	public ModuleCoalStandard(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	@Override
	public double getFuelMultiplier() {
		return 2.25;
	}
	
}