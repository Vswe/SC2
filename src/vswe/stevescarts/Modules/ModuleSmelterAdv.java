package vswe.stevescarts.Modules;

import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.Addons.ModuleSmelter;

public class ModuleSmelterAdv extends ModuleSmelter {

	public ModuleSmelterAdv(MinecartModular cart) {
		super(cart);
	}
	
	@Override
	protected boolean canUseAdvancedFeatures() {
		return true;
	}

}
