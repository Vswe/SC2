package vswe.stevescarts.Slots;
import vswe.stevescarts.Helpers.TransferHandler.TRANSFER_TYPE;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotCartCrafter extends SlotFake
{
    public SlotCartCrafter(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return true;
    }
    
	
}
