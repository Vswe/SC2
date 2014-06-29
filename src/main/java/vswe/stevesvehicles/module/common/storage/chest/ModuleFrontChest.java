package vswe.stevesvehicles.module.common.storage.chest;

import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleFrontChest extends ModuleChest {
	public ModuleFrontChest(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	@Override
	protected int getInventoryWidth() {
		return 4;
	}

	@Override
	protected int getInventoryHeight() {
		return 3;
	}


}