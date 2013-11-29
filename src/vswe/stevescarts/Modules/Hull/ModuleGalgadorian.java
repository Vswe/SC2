package vswe.stevescarts.Modules.Hull;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleGalgadorian extends ModuleHull {
	public ModuleGalgadorian(MinecartModular cart) {
		super(cart);
	}

	
	@Override
	public int getConsumption(boolean isMoving) {
		if (!isMoving) {
			return super.getConsumption(isMoving);
		}else{
			return 9;		
		}
	}
	
}