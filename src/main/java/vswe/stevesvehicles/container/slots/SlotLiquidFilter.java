package vswe.stevesvehicles.container.slots;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import vswe.stevesvehicles.transfer.TransferHandler;

public class SlotLiquidFilter extends SlotBase implements ISpecialItemTransferValidator {

    public SlotLiquidFilter(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

    @Override
    public boolean isItemValidForTransfer(ItemStack item, TransferHandler.TransferType type) {
    	return false;
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
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
