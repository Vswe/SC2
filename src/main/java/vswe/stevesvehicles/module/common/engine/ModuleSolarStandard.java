package vswe.stevesvehicles.module.common.engine;

import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleSolarStandard extends ModuleSolarTop {
	public ModuleSolarStandard(VehicleBase vehicleBase) {
		super(vehicleBase);
	}
	
	@Override
	protected int getMaxCapacity(){
		return 800000;
	}
	
	@Override
	protected int getGenSpeed() {
		return 5;
	}
	
}