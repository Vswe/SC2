package vswe.stevesvehicles.old.Modules.Storages.Tanks;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;

public class ModuleFrontTank extends ModuleTank{
	public ModuleFrontTank(EntityModularCart cart) {
		super(cart);
	}

	
	@Override
	protected int getTankSize() {
		return 8000;
	}

	
}