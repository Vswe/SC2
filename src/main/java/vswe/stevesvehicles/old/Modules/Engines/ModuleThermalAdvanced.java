package vswe.stevesvehicles.old.Modules.Engines;
import vswe.stevesvehicles.vehicles.entities.EntityModularCart;

public class ModuleThermalAdvanced extends ModuleThermalBase {
	public ModuleThermalAdvanced(EntityModularCart cart) {
		super(cart);
	}
	
	@Override
	protected int getEfficiency() {
		return 60;
	}
	
	@Override
	protected int getCoolantEfficiency() {
		return 90;
	}
	
}