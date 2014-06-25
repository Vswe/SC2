package vswe.stevescarts.old.Modules.Storages.Chests;
import vswe.stevescarts.vehicles.entities.EntityModularCart;

public class ModuleInternalStorage extends ModuleChest {
	public ModuleInternalStorage(EntityModularCart cart) {
		super(cart);
	}

	@Override
	protected int getInventoryWidth()
	{
		return 3;
	}
	@Override
	protected int getInventoryHeight() {
		return 3;
	}

	@Override
	protected boolean hasVisualChest() {
		return false;
	}
	@Override
	public int guiWidth() {
		return super.guiWidth() + 30;
	}	
}