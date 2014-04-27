package vswe.stevescarts.Modules.Addons;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleSmelterAdv extends ModuleSmelter {

	public ModuleSmelterAdv(MinecartModular cart) {
		super(cart);
	}
	
	@Override
	protected boolean canUseAdvancedFeatures() {
		return true;
	}

}
