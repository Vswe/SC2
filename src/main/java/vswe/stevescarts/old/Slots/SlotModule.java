package vswe.stevescarts.old.Slots;
import vswe.stevescarts.old.Items.ModItems;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;
public class SlotModule extends Slot
{
    public SlotModule(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

	@Override
    public boolean isItemValid(ItemStack itemstack)
    {
        return itemstack.getItem() == ModItems.modules;
    }
}