package vswe.stevescarts.Slots;
import vswe.stevescarts.Helpers.TransferHandler.TRANSFER_TYPE;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotMilker extends SlotBase
{
    public SlotMilker(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return itemstack != null && itemstack.getItem() == Item.bucketEmpty;
    }


}
