package vswe.stevesvehicles.module.common.storage.chest;

import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleTopChest extends ModuleChest {
	public ModuleTopChest(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	@Override
	protected int getInventoryWidth() {
		return 6;
	}

	@Override
	protected int getInventoryHeight() {
		return 3;
	}

}