package vswe.stevescarts.Modules.Storages.Chests;
import java.util.HashMap;

import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Models.Cart.ModelCartbase;
import vswe.stevescarts.Models.Cart.ModelSideChests;

public class ModuleSideChests extends ModuleChest {
	public ModuleSideChests(MinecartModular cart) {
		super(cart);
	}

	@Override
	protected int getInventoryWidth()
	{
		return 5;
	}
	@Override
	protected int getInventoryHeight() {
		return 3;
	}
	@Override
	protected String getChestName() {
		return "Side Chests";
	}

}