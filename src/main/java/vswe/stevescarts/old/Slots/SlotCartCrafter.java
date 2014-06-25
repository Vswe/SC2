package vswe.stevescarts.old.Slots;
import net.minecraft.inventory.IInventory;
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
