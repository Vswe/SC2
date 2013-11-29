package vswe.stevescarts.Modules.Hull;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleReinforced extends ModuleHull {
	public ModuleReinforced(MinecartModular cart) {
		super(cart);
	}

	
	@Override
	public int getConsumption(boolean isMoving) {
		if (!isMoving) {
			return super.getConsumption(isMoving);
		}else{
			return 3;		
		}
	}
	
}