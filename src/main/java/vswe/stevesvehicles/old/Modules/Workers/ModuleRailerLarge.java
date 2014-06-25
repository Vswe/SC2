package vswe.stevesvehicles.old.Modules.Workers;
import vswe.stevesvehicles.vehicles.entities.EntityModularCart;

public class ModuleRailerLarge extends ModuleRailer {
	public ModuleRailerLarge(EntityModularCart cart) {
		super(cart);
	}
	@Override
	protected int getInventoryHeight() {
		return 2;
	}
}