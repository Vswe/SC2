package vswe.stevesvehicles.module.common.engine;

import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleCoalTiny extends ModuleCoalBase {
	public ModuleCoalTiny(VehicleBase vehicleBase) {
		super(vehicleBase);
	}
	
	@Override
	protected int getInventoryWidth() {
		return 1;
	}
	
	@Override
	public double getFuelMultiplier() {
		return 1.5;
	}
	
}