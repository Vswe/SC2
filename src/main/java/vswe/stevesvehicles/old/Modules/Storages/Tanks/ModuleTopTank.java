package vswe.stevesvehicles.old.Modules.Storages.Tanks;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;

public class ModuleTopTank extends ModuleTank{
	public ModuleTopTank(EntityModularCart cart) {
		super(cart);
	}
	
	@Override
	protected int getTankSize() {
		return 14000;
	}


}