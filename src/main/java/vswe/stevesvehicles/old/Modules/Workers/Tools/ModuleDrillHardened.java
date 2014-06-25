package vswe.stevesvehicles.old.Modules.Workers.Tools;

import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.old.Helpers.ComponentTypes;
import vswe.stevesvehicles.old.Items.ModItems;
import vswe.stevesvehicles.vehicles.entities.EntityModularCart;

public class ModuleDrillHardened extends ModuleDrill {
	public ModuleDrillHardened(EntityModularCart cart) {
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
		return ComponentTypes.REINFORCED_METAL.getLocalizedName();
	}
	
	@Override
	public int getRepairItemUnits(ItemStack item) {
		if (item != null && item.getItem() == ModItems.component && item.getItemDamage() == 22) {
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