package vswe.stevescarts.Slots;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotBuilder extends SlotBase
{
    public SlotBuilder(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return itemstack.getItem().itemID == Block.rail.blockID;
    }
}
