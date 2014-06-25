package vswe.stevescarts.old.Modules.Storages.Tanks;
import vswe.stevescarts.vehicles.entities.EntityModularCart;

public class ModuleAdvancedTank extends ModuleTank{
	public ModuleAdvancedTank(EntityModularCart cart) {
		super(cart);
	}
	
	@Override
	protected int getTankSize() {
		return 32000;
	}
	
	
}