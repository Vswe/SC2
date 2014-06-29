package vswe.stevesvehicles.module.cart.tool;

import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleWoodcutterHardened extends ModuleWoodcutter {

	public ModuleWoodcutterHardened(VehicleBase vehicleBase) {
		super(vehicleBase);
	}


	@Override
	public int getPercentageDropChance() {
		return 100;
	}


}
