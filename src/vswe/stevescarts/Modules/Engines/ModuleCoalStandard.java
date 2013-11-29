package vswe.stevescarts.Modules.Engines;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleCoalStandard extends ModuleCoalBase {
	public ModuleCoalStandard(MinecartModular cart) {
		super(cart);
	}

	@Override
	public double getFuelMultiplier() {
		return 2.25;
	}
	
}