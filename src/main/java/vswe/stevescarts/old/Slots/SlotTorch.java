package vswe.stevescarts.old.Slots;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotTorch extends SlotBase
{
    public SlotTorch(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return Block.getBlockFromItem(itemstack.getItem()) == Blocks.torch;
    }
}