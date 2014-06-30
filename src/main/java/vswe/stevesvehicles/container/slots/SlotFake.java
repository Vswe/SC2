package vswe.stevesvehicles.container.slots;
import vswe.stevesvehicles.old.Helpers.TransferHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public abstract class SlotFake extends SlotBase implements ISpecialItemTransferValidator {
    public SlotFake(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }


    @Override
    public int getSlotStackLimit() {
    	return 0;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack item) {
    	super.onPickupFromSlot(player, item);
    	if (item != null && player != null && player.inventory != null) {
    		player.inventory.setItemStack(null);
    	}
    }

	@Override
	public boolean isItemValidForTransfer(ItemStack item, TransferHandler.TransferType type) {
		return false;
	}
	
}
