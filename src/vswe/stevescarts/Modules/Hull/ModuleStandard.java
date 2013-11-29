package vswe.stevescarts.Modules.Hull;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleStandard extends ModuleHull {
	public ModuleStandard(MinecartModular cart) {
		super(cart);
	}

	@Override
	public int getConsumption(boolean isMoving) {
		if (!isMoving) {
			return super.getConsumption(isMoving);
		}else{
			return 1;		
		}
	}
	
}