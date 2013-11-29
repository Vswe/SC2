package vswe.stevescarts.Modules.Storages.Chests;
import java.util.HashMap;

import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Models.Cart.ModelCartbase;
import vswe.stevescarts.Models.Cart.ModelTopChest;

public class ModuleTopChest extends ModuleChest {
	public ModuleTopChest(MinecartModular cart) {
		super(cart);
	}

	@Override
	protected int getInventoryWidth()
	{
		return 6;
	}
	@Override
	protected int getInventoryHeight() {
		return 3;
	}
	@Override
	protected String getChestName() {
		return "Top Chest";
	}

}