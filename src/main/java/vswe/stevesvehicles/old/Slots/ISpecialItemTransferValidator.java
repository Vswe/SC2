package vswe.stevesvehicles.old.Slots;

import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.old.Helpers.TransferHandler.TRANSFER_TYPE;

public interface ISpecialItemTransferValidator {

	public boolean isItemValidForTransfer(ItemStack item, TRANSFER_TYPE type);
	
	
}
