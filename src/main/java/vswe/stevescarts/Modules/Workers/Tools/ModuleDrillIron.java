package vswe.stevescarts.Modules.Workers.Tools;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;

public class ModuleDrillIron extends ModuleDrill {
	public ModuleDrillIron(MinecartModular cart) {
		super(cart);
	}

	//returns how far the drill should drill above itself, i.e. how tall is the  hole
	protected int blocksOnTop() {
		return 3;
	}

	//returns how far the drill should drill on each side
	protected int blocksOnSide() {
		return 1;
	}

	@Override
	protected float getTimeMult() {
		return 40;
	}
	
	@Override
	public int getMaxDurability() {
		return 50000;
	}
	@Override
	public String getRepairItemName() {
		return Localization.MODULES.TOOLS.IRON.translate();
	}
	@Override
	public int getRepairItemUnits(ItemStack item) {
		if (item != null && item.getItem() == Item.ingotIron) {
			return 20000;
		}
		return 0;
	}
	@Override
	public int getRepairSpeed() {
		return 50;
	}
	
	@Override
	public boolean useDurability() {
		return true;
	}	
}