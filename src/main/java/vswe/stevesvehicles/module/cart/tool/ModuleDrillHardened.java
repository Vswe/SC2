package vswe.stevesvehicles.module.cart.tool;

import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleDrillHardened extends ModuleDrill {
	public ModuleDrillHardened(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	//returns how far the drill should drill above itself, i.e. how tall is the  hole
    @Override
	protected int blocksOnTop() {
        return 5;
    }

	//returns how far the drill should drill on each side
	@Override
	protected int blocksOnSide() {
		return 2;
	}

	@Override
	protected float getTimeMultiplier() {
		return 4;
	}

}