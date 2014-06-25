package vswe.stevesvehicles.old.Slots;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import vswe.stevesvehicles.old.Helpers.TransferHandler.TRANSFER_TYPE;
public class SlotLiquidOutput extends SlotBase implements ISpecialItemTransferValidator
{

    public SlotLiquidOutput(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }


    public boolean isItemValid(ItemStack itemstack)
    {
        return isItemStackValid(itemstack);
    }
    
	@Override
	public boolean isItemValidForTransfer(ItemStack item, TRANSFER_TYPE type) {		
		return type == TRANSFER_TYPE.OTHER && FluidContainerRegistry.isContainer(item);
	}    
    
    public static boolean isItemStackValid(ItemStack itemstack)
    {
        return false;
    }   
}
