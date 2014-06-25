package vswe.stevesvehicles.old.Slots;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
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
        return Block.getBlockFromItem(itemstack.getItem()) == Blocks.rail;
    }
}
