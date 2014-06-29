package vswe.stevesvehicles.module.common.storage.chest;

import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleGiftStorage extends ModuleChest {
	public ModuleGiftStorage(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	@Override
	protected int getInventoryWidth() {
		return 9;
	}

	@Override
	protected int getInventoryHeight() {
		return 4;
	}


}