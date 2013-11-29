package vswe.stevescarts.Modules.Workers;
import vswe.stevescarts.Carts.MinecartModular;

public class ModuleRailerLarge extends ModuleRailer {
	public ModuleRailerLarge(MinecartModular cart) {
		super(cart);
	}
	@Override
	protected int getInventoryHeight() {
		return 2;
	}
}