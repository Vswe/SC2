package vswe.stevesvehicles.module.common.storage.chest;

import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleSideChests extends ModuleChest {
	public ModuleSideChests(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	@Override
	protected int getInventoryWidth() {
		return 5;
	}

	@Override
	protected int getInventoryHeight() {
		return 3;
	}


}