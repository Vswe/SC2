package vswe.stevesvehicles.module.cart.tool;

import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleDrillIron extends ModuleDrill {
	public ModuleDrillIron(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	//returns how far the drill should drill above itself, i.e. how tall is the  hole
	protected int blocksOnTop() {
		return 3;
	}

	//returns how far the drill should drill on each side
	protected int blocksOnSide() {
		return 1;
	}

	@Override
	protected float getTimeMultiplier() {
		return 40;
	}

}