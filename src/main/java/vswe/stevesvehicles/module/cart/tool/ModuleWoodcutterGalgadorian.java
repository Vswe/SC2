package vswe.stevesvehicles.module.cart.tool;

import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleWoodcutterGalgadorian extends ModuleWoodcutter {

	public ModuleWoodcutterGalgadorian(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	@Override
	public int getPercentageDropChance() {
		return 125;
	}

}
