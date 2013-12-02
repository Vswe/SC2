package vswe.stevescarts.Modules.Storages.Chests;
import java.util.HashMap;

import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Models.Cart.ModelCartbase;
import vswe.stevescarts.Models.Cart.ModelFrontChest;

public class ModuleFrontChest extends ModuleChest {
	public ModuleFrontChest(MinecartModular cart) {
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