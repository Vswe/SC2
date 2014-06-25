package vswe.stevescarts.old.Modules.Addons;

import vswe.stevescarts.vehicles.entities.EntityModularCart;

public class ModuleSmelterAdv extends ModuleSmelter {

	public ModuleSmelterAdv(EntityModularCart cart) {
		super(cart);
	}
	
	@Override
	protected boolean canUseAdvancedFeatures() {
		return true;
	}

}
