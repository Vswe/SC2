package vswe.stevesvehicles.old.Modules.Storages.Tanks;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;

public class ModuleSideTanks extends ModuleTank{
	public ModuleSideTanks(EntityModularCart cart) {
		super(cart);
	}

	@Override
	protected int getTankSize() {
		return 8000;
	}

	
}