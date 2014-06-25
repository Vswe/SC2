package vswe.stevescarts.old.Modules.Storages.Tanks;
import vswe.stevescarts.vehicles.entities.EntityModularCart;

public class ModuleSideTanks extends ModuleTank{
	public ModuleSideTanks(EntityModularCart cart) {
		super(cart);
	}

	@Override
	protected int getTankSize() {
		return 8000;
	}

	
}