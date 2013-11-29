package vswe.stevescarts.Modules.Workers.Tools;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;

public class ModuleFarmerDiamond extends ModuleFarmer {

	public ModuleFarmerDiamond(MinecartModular cart) {
		super(cart);
	}
	
	@Override
	public int getMaxDurability() {
		return 300000;
	}
	
	@Override
	public String getRepairItemName() {
		return "Diamonds";
	}
	
	@Override
	public int getRepairItemUnits(ItemStack item) {
		if (item != null && item.getItem() == Item.diamond) {
			return 150000;
		}
		return 0;
	}
	
	@Override
	public boolean useDurability() {
		return true;
	}
	
	@Override
	public int getRepairSpeed() {
		return 500;
	}

	@Override
	public int getRange() {
		return 1;
	}

}
