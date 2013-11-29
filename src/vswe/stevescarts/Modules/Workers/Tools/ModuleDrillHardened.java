package vswe.stevescarts.Modules.Workers.Tools;

import net.minecraft.item.ItemStack;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Carts.MinecartModular;

public class ModuleDrillHardened extends ModuleDrill {
	public ModuleDrillHardened(MinecartModular cart) {
		super(cart);
	}

	//returns how far the drill should drill above itself, i.e. how tall is the  hole
    @Override
	protected int blocksOnTop()
    {
        return 5;
    }

	//returns how far the drill should drill on each side
	@Override
	protected int blocksOnSide() {
		return 2;
	}

	@Override
	protected float getTimeMult() {
		return 4;
	}
	
	@Override
	public int getMaxDurability() {
		return 1000000;
	}
	
	@Override
	public String getRepairItemName() {
		return "Reinforced Metal";
	}
	
	@Override
	public int getRepairItemUnits(ItemStack item) {
		if (item != null && item.getItem() == StevesCarts.component && item.getItemDamage() == 22) {
			return 450000;
		}
		return 0;
	}
	@Override
	public int getRepairSpeed() {
		return 200;
	}	
	
	@Override
	public boolean useDurability() {
		return true;
	}
}