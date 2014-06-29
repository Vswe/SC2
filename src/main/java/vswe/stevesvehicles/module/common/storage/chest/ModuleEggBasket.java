package vswe.stevesvehicles.module.common.storage.chest;

import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleEggBasket extends ModuleChest {
	public ModuleEggBasket(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	@Override
	protected int getInventoryWidth() {
		return 6;
	}

	@Override
	protected int getInventoryHeight() {
		return 4;
	}

	@Override
	protected boolean playChestSound() {
		return false;
	}	

    @Override
	protected float getLidSpeed() {
		return (float)(Math.PI / 150);
	}	

    @Override
	protected float chestFullyOpenAngle() {
		return (float)Math.PI / 8F;
	}	

}