package vswe.stevescarts.Slots;

import net.minecraft.item.ItemStack;
import vswe.stevescarts.Helpers.TransferHandler.TRANSFER_TYPE;

public interface ISpecialItemTransferValidator {

	public boolean isItemValidForTransfer(ItemStack item, TRANSFER_TYPE type);
	
	
}
