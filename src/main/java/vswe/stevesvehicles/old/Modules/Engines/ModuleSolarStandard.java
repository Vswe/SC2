package vswe.stevesvehicles.old.Modules.Engines;

import vswe.stevesvehicles.vehicles.entities.EntityModularCart;

public class ModuleSolarStandard extends ModuleSolarTop {
	public ModuleSolarStandard(EntityModularCart cart) {
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