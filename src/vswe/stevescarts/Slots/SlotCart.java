package vswe.stevescarts.Slots;
import vswe.stevescarts.StevesCarts;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;
public class SlotCart extends Slot
{
    public SlotCart(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

	@Override
    public boolean isItemValid(ItemStack itemstack)
    {
        return itemstack != null && itemstack.getItem().itemID == StevesCarts.instance.carts.itemID && itemstack.getTagCompound() != null && !itemstack.getTagCompound().hasKey("maxTime");
    }
}