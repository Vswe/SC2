package vswe.stevesvehicles.old.Modules.Storages.Tanks;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;

public class ModuleAdvancedTank extends ModuleTank{
	public ModuleAdvancedTank(EntityModularCart cart) {
		super(cart);
	}
	
	@Override
	protected int getTankSize() {
		return 32000;
	}
	
	
}