package vswe.stevescarts.Modules.Storages.Chests;
import vswe.stevescarts.Carts.MinecartModular;

public class ModuleInternalStorage extends ModuleChest {
	public ModuleInternalStorage(MinecartModular cart) {
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
	protected String getChestName() {
		return "Internal Chest";
	}
	@Override
	protected boolean hasVisualChest() {
		return false;
	}
	@Override
	public int guiWidth() {
		return super.guiWidth() + 10;
	}	
}