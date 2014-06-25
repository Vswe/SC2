package vswe.stevesvehicles.old.Modules.Hull;

import vswe.stevesvehicles.vehicles.entities.EntityModularCart;

public class ModuleGalgadorian extends ModuleHull {
	public ModuleGalgadorian(EntityModularCart cart) {
		super(cart);
	}

	
	@Override
	public int getConsumption(boolean isMoving) {
		if (!isMoving) {
			return super.getConsumption(false);
		}else{
			return 9;		
		}
	}
	
}