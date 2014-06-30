package vswe.stevesvehicles.container.slots;

import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.old.Helpers.TransferHandler;

public interface ISpecialItemTransferValidator {
	public boolean isItemValidForTransfer(ItemStack item, TransferHandler.TransferType type);
}
