package vswe.stevesvehicles.module.cart.tool;

import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleFarmerDiamond extends ModuleFarmer {

	public ModuleFarmerDiamond(VehicleBase vehicleBase) {
		super(vehicleBase);
	}


	@Override
	public int getRange() {
		return 1;
	}

}
