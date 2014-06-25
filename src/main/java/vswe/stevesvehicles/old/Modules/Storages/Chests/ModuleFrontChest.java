package vswe.stevesvehicles.old.Modules.Storages.Chests;

import vswe.stevesvehicles.vehicles.entities.EntityModularCart;

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