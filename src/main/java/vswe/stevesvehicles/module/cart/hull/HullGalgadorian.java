package vswe.stevesvehicles.module.cart.hull;

import vswe.stevesvehicles.vehicle.VehicleBase;

public class HullGalgadorian extends ModuleHull {
	public HullGalgadorian(VehicleBase vehicle) {
		super(vehicle);
	}

	@Override
	public int getConsumption(boolean isMoving) {
		if (!isMoving) {
			return super.getConsumption(false);
		} else {
			//ToDo: Keep energy consumption as constants
			return 9;
		}
	}
}