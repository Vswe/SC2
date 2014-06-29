package vswe.stevesvehicles.old.Modules.Engines;

import vswe.stevesvehicles.vehicle.entity.EntityModularCart;

public class ModuleSolarBasic extends ModuleSolarTop {
	public ModuleSolarBasic(EntityModularCart cart) {
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