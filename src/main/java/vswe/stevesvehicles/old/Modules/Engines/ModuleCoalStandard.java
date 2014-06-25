package vswe.stevesvehicles.old.Modules.Engines;

import vswe.stevesvehicles.vehicles.entities.EntityModularCart;

public class ModuleCoalStandard extends ModuleCoalBase {
	public ModuleCoalStandard(EntityModularCart cart) {
		super(cart);
	}

	@Override
	public double getFuelMultiplier() {
		return 2.25;
	}
	
}