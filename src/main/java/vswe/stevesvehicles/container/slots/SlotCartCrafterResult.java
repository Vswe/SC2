package vswe.stevesvehicles.container.slots;
import vswe.stevesvehicles.transfer.TransferHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotCartCrafterResult extends SlotBase implements ISpecialItemTransferValidator {
    public SlotCartCrafterResult(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        return false;
    }

    @Override
    public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
    	return false;
    }

	@Override
	public boolean isItemValidForTransfer(ItemStack item, TransferHandler.TransferType type) {
		return false;
	}
	
	@Override
	public int getSlotStackLimit() {
		return 0;
	}

}
