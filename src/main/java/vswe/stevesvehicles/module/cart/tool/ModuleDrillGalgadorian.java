package vswe.stevesvehicles.module.cart.tool;

import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleDrillGalgadorian extends ModuleDrill {
	public ModuleDrillGalgadorian(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	//returns how far the drill should drill above itself, i.e. how tall is the  hole
    @Override
	protected int blocksOnTop(){
        return 9;
    }

	//returns how far the drill should drill on each side
	@Override
	protected int blocksOnSide() {
		return 4;
	}

	@Override
	protected float getTimeMultiplier() {
		return 0;
	}

	
}