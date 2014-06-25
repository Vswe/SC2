package vswe.stevesvehicles.old.Modules.Addons;

import vswe.stevesvehicles.vehicles.entities.EntityModularCart;

public class ModuleCrafterAdv extends ModuleCrafter {

	public ModuleCrafterAdv(EntityModularCart cart) {
		super(cart);
	}
	
	@Override
	protected boolean canUseAdvancedFeatures() {
		return true;
	}

}
