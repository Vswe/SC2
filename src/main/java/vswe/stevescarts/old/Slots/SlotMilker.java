package vswe.stevescarts.old.Slots;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotMilker extends SlotBase
{
    public SlotMilker(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return itemstack != null && itemstack.getItem() == Items.bucket;
    }


}
