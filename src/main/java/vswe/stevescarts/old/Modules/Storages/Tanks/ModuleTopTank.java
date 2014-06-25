package vswe.stevescarts.old.Modules.Storages.Tanks;
import vswe.stevescarts.vehicles.entities.EntityModularCart;

public class ModuleTopTank extends ModuleTank{
	public ModuleTopTank(EntityModularCart cart) {
		super(cart);
	}
	
	@Override
	protected int getTankSize() {
		return 14000;
	}


}