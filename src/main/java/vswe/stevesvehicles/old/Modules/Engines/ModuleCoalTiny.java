package vswe.stevesvehicles.old.Modules.Engines;

import vswe.stevesvehicles.vehicle.entity.EntityModularCart;

public class ModuleCoalTiny extends ModuleCoalBase {
	public ModuleCoalTiny(EntityModularCart cart) {
		super(cart);
	}
	
	@Override
	protected int getInventoryWidth() {
		return 1;
	}
	
	@Override
	public double getFuelMultiplier() {
		return 0.5;
	}
	
}