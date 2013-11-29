package vswe.stevescarts.Modules.Engines;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleSolarBasic extends ModuleSolarTop {
	public ModuleSolarBasic(MinecartModular cart) {
		super(cart);
	}

	@Override
	protected int getPanelCount() {
		return 2;
	}
	
	@Override
	protected int getMaxCapacity(){
		return 100000;
	}
	
	@Override
	protected int getGenSpeed() {
		return 2;
	}
	
}