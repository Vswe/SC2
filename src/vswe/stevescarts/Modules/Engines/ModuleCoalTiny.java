package vswe.stevescarts.Modules.Engines;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleCoalTiny extends ModuleCoalBase {
	public ModuleCoalTiny(MinecartModular cart) {
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