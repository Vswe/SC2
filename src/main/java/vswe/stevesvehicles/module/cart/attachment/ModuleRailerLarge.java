package vswe.stevesvehicles.module.cart.attachment;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleRailerLarge extends ModuleRailer {
	public ModuleRailerLarge(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	@Override
	protected int getInventoryHeight() {
		return 2;
	}
}