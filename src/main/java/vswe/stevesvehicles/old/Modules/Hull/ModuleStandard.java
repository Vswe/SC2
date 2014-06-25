package vswe.stevesvehicles.old.Modules.Hull;

import vswe.stevesvehicles.vehicles.entities.EntityModularCart;

public class ModuleStandard extends ModuleHull {
	public ModuleStandard(EntityModularCart cart) {
		super(cart);
	}

	@Override
	public int getConsumption(boolean isMoving) {
		if (!isMoving) {
			return super.getConsumption(false);
		}else{
			return 1;		
		}
	}
	
}