package vswe.stevesvehicles.container.slots;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import vswe.stevesvehicles.old.Helpers.TransferHandler;

public class SlotLiquidOutput extends SlotBase implements ISpecialItemTransferValidator {

    public SlotLiquidOutput(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        return isItemStackValid(itemstack);
    }
    
	@Override
	public boolean isItemValidForTransfer(ItemStack item, TransferHandler.TransferType type) {
		return type == TransferHandler.TransferType.OTHER && FluidContainerRegistry.isContainer(item);
	}    
    
    public static boolean isItemStackValid(ItemStack itemstack) {
        return false;
    }   
}
