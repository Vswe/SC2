package vswe.stevescarts.old.Modules.Storages.Tanks;
import vswe.stevescarts.vehicles.entities.EntityModularCart;

public class ModuleFrontTank extends ModuleTank{
	public ModuleFrontTank(EntityModularCart cart) {
		super(cart);
	}

	
	@Override
	protected int getTankSize() {
		return 8000;
	}

	
}