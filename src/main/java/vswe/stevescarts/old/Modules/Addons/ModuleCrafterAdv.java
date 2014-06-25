package vswe.stevescarts.old.Modules.Addons;

import vswe.stevescarts.vehicles.entities.EntityModularCart;

public class ModuleCrafterAdv extends ModuleCrafter {

	public ModuleCrafterAdv(EntityModularCart cart) {
		super(cart);
	}
	
	@Override
	protected boolean canUseAdvancedFeatures() {
		return true;
	}

}
