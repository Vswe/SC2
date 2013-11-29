package vswe.stevescarts.Modules.Workers.Tools;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;

public class ModuleDrillDiamond extends ModuleDrill {
	public ModuleDrillDiamond(MinecartModular cart) {
		super(cart);
	}

	//returns how far the drill should drill above itself, i.e. how tall is the  hole
	@Override
	protected int blocksOnTop() {
		return 3;
	}

	//returns how far the drill should drill on each side
	@Override
	protected int blocksOnSide() {
		return 1;
	}

	@Override
	protected float getTimeMult() {
		return 8;
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
			return 100000;
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