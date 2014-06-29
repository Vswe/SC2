package vswe.stevesvehicles.module.common.addon;

import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleSmelterAdv extends ModuleSmelter {

	public ModuleSmelterAdv(VehicleBase vehicleBase) {
		super(vehicleBase);
	}
	
	@Override
	protected boolean canUseAdvancedFeatures() {
		return true;
	}

}
