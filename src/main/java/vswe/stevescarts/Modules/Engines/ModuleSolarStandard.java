package vswe.stevescarts.Modules.Engines;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleSolarStandard extends ModuleSolarTop {
	public ModuleSolarStandard(MinecartModular cart) {
		super(cart);
	}

	@Override
	protected int getPanelCount() {
		return 4;
	}
	
	@Override
	protected int getMaxCapacity(){
		return 800000;
	}
	
	@Override
	protected int getGenSpeed() {
		return 5;
	}
	
}