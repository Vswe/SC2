package vswe.stevescarts.Slots;
import vswe.stevescarts.Helpers.TransferHandler.TRANSFER_TYPE;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class SlotFake extends SlotBase implements ISpecialItemTransferValidator
{
    public SlotFake(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }


    @Override
    public int getSlotStackLimit() {
    	return 0;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack) {
    	super.onPickupFromSlot(par1EntityPlayer, par2ItemStack);
    	if (par2ItemStack != null && par1EntityPlayer != null && par1EntityPlayer.inventory != null) {
    		par1EntityPlayer.inventory.setItemStack(null);
    	}
    }

	@Override
	public boolean isItemValidForTransfer(ItemStack item, TRANSFER_TYPE type) {
		return false;
	}
	
}
