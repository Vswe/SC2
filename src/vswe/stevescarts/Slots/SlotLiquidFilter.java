package vswe.stevescarts.Slots;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import vswe.stevescarts.Helpers.TransferHandler.TRANSFER_TYPE;
public class SlotLiquidFilter extends SlotBase implements ISpecialItemTransferValidator
{

    public SlotLiquidFilter(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean isItemValidForTransfer(ItemStack item, TRANSFER_TYPE type) {
    	return false;
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return isItemStackValid(itemstack);
    }
    
    public static boolean isItemStackValid(ItemStack itemstack) {
    	return FluidContainerRegistry.isFilledContainer(itemstack);
    }
    
	@Override
	public int getSlotStackLimit() {
		return 1;
	}
}
