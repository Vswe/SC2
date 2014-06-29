package vswe.stevesvehicles.module.cart.tool;

import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleFarmerGalgadorian extends ModuleFarmer {

	public ModuleFarmerGalgadorian(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	@Override
	public int getRange() {
		return 2;
	}	

}
