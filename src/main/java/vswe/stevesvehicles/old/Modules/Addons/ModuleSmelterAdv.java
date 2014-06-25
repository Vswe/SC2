package vswe.stevesvehicles.old.Modules.Addons;

import vswe.stevesvehicles.vehicles.entities.EntityModularCart;

public class ModuleSmelterAdv extends ModuleSmelter {

	public ModuleSmelterAdv(EntityModularCart cart) {
		super(cart);
	}
	
	@Override
	protected boolean canUseAdvancedFeatures() {
		return true;
	}

}
