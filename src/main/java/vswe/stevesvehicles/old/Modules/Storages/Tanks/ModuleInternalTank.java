package vswe.stevesvehicles.old.Modules.Storages.Tanks;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;


public class ModuleInternalTank extends ModuleTank{
	public ModuleInternalTank(EntityModularCart cart) {
		super(cart);
	}

	
	@Override
	protected int getTankSize() {
		return 4000;
	}

	@Override
	public boolean hasVisualTank() {
		return false;
	}

}