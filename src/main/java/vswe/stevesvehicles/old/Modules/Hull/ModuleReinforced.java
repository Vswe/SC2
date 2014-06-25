package vswe.stevesvehicles.old.Modules.Hull;

import vswe.stevesvehicles.vehicles.entities.EntityModularCart;

public class ModuleReinforced extends ModuleHull {
	public ModuleReinforced(EntityModularCart cart) {
		super(cart);
	}

	
	@Override
	public int getConsumption(boolean isMoving) {
		if (!isMoving) {
			return super.getConsumption(false);
		}else{
			return 3;		
		}
	}
	
}