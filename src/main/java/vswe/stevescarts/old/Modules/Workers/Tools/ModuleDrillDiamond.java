package vswe.stevescarts.old.Modules.Workers.Tools;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.Helpers.Localization;

public class ModuleDrillDiamond extends ModuleDrill {
	public ModuleDrillDiamond(EntityModularCart cart) {
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
		return Localization.MODULES.TOOLS.DIAMONDS.translate();
	}
	@Override
	public int getRepairItemUnits(ItemStack item) {
		if (item != null && item.getItem() == Items.diamond) {
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