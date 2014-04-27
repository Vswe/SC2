package vswe.stevescarts.Modules.Workers.Tools;

import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;

public class ModuleDrillGalgadorian extends ModuleDrill {
	public ModuleDrillGalgadorian(MinecartModular cart) {
		super(cart);
	}

	//returns how far the drill should drill above itself, i.e. how tall is the  hole
    @Override
	protected int blocksOnTop()
    {
        return 9;
    }

	//returns how far the drill should drill on each side
	@Override
	protected int blocksOnSide() {
		return 4;
	}

	@Override
	protected float getTimeMult() {
		return 0;
	}
	
	
	public int getMaxDurability() {
		return 1;
	}
	public String getRepairItemName() {
		return null;
	}
	public int getRepairItemUnits(ItemStack item) {
		return 0;
	}
	public boolean useDurability() {
		return false;
	}
	public int getRepairSpeed() {
		return 1;
	}		
	
}