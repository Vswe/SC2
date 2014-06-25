package vswe.stevescarts.old.Slots;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotFirework extends SlotBase
{
    public SlotFirework(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    public boolean isItemValid(ItemStack itemstack)
    {
		Item item = itemstack.getItem();
	
        return 
			item == Items.fireworks ||
            item == Items.gunpowder ||
            item == Items.firework_charge ||
            item == Items.dye ||
            item == Items.paper ||
            item == Items.glowstone_dust ||
            item == Items.diamond ||
            item == Items.fire_charge ||
            item == Items.feather ||
            item == Items.gold_nugget ||
            item == Items.skull;
    }
}
