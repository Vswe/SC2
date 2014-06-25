package vswe.stevescarts.old.Modules.Storages.Chests;

import vswe.stevescarts.vehicles.entities.EntityModularCart;

public class ModuleFrontChest extends ModuleChest {
	public ModuleFrontChest(EntityModularCart cart) {
		super(cart);
	}

	@Override
	protected int getInventoryWidth()
	{
		return 4;
	}
	@Override
	protected int getInventoryHeight() {
		return 3;
	}


}