package vswe.stevescarts.Slots;
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
        return itemstack.getItem().itemID == Item.bone.itemID || (itemstack.getItem().itemID == Item.dyePowder.itemID && itemstack.getItemDamage() == 15);
    }
}
