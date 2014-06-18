package vswe.stevescarts.Slots;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotFertilizer extends SlotBase
{
    public SlotFertilizer(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return itemstack.getItem() == Items.bone || (itemstack.getItem() == Items.dye && itemstack.getItemDamage() == 15);
    }
}
