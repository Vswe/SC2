package vswe.stevesvehicles.container;
import java.util.Iterator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.old.Helpers.TransferHandler;
import vswe.stevesvehicles.tileentity.TileEntityBase;

public abstract class ContainerBase extends Container {
	/**
	 * The inventory associated with this container
	 * @return The IInventory or null if no inventory exists.
	 */
	public abstract IInventory getMyInventory();
	
	/**
	 * The tile entity this container is associated with
	 * @return The Tile Entity or null if none exits.
	 */
	public abstract TileEntityBase getTileEntity();
	
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int i) {
		if (getMyInventory() == null) {
			return null;
		}
		
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);
        if(slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(i < getMyInventory().getSizeInventory()) {
                if(!mergeItemStack(itemstack1, getMyInventory().getSizeInventory()+28, getMyInventory().getSizeInventory()+36, false)) {
					if(!mergeItemStack(itemstack1, getMyInventory().getSizeInventory(), getMyInventory().getSizeInventory()+28, false)) {
						return null;
					}
                }
            }else if(!mergeItemStack(itemstack1, 0, getMyInventory().getSizeInventory(), false)){
                return null;
            }
            if(itemstack1.stackSize == 0){
                slot.putStack(null);
            }else{
                slot.onSlotChanged();
            }
            if(itemstack1.stackSize != itemstack.stackSize){
               slot.onPickupFromSlot(player,itemstack1);
            }else{
                return null;
            }
        }
        return itemstack;
    }

	@Override
    protected boolean mergeItemStack(ItemStack item, int start, int end, boolean invert){
		if (getMyInventory() == null) {
			return false;
		}		
		
        boolean result = false;
        int id = start;

        if (invert) {
            id = end - 1;
        }

        Slot slot;
        ItemStack slotItem;

        if (item.isStackable()) {
            while (item.stackSize > 0 && (!invert && id < end || invert && id >= start)) {
                slot = (Slot)this.inventorySlots.get(id);
                slotItem = slot.getStack();

                if (slotItem != null && slotItem.stackSize > 0 && slotItem.getItem() == item.getItem() && (!item.getHasSubtypes() || item.getItemDamage() == slotItem.getItemDamage()) && ItemStack.areItemStackTagsEqual(item, slotItem)) {
                    int size = slotItem.stackSize + item.stackSize;

					int maxLimit = Math.min(item.getMaxStackSize(), slot.getSlotStackLimit());
                    if (size <= maxLimit) {
                        item.stackSize = 0;
                        slotItem.stackSize = size;
                        slot.onSlotChanged();
                        result = true;
                    }else if (slotItem.stackSize < maxLimit) {
                        item.stackSize -= maxLimit - slotItem.stackSize;
                        slotItem.stackSize = maxLimit;
                        slot.onSlotChanged();
                        result = true;
                    }
                }

                if (invert) {
                    --id;
                }else{
                    ++id;
                }
            }
        }

        if (item.stackSize > 0){
            if (invert){
                id = end - 1;
            }else{
                id = start;
            }

            while (!invert && id < end || invert && id >= start){
                slot = (Slot)this.inventorySlots.get(id);
                slotItem = slot.getStack();

                if (slotItem == null && TransferHandler.isItemValidForTransfer(slot, item, TransferHandler.TransferType.SHIFT)) {
					int stackSize = Math.min(slot.getSlotStackLimit(), item.stackSize);
					ItemStack newItem = item.copy();
					newItem.stackSize = stackSize;
					item.stackSize -= stackSize;
                    slot.putStack(newItem);
                    slot.onSlotChanged();
                    
                    result = item.stackSize == 0;
                    break;
                }

                if (invert){
                    --id;
                }else{
                    ++id;
                }
            }
        }

        return result;
    }
	

	@Override
	public boolean canInteractWith(EntityPlayer player) {
       return getTileEntity() != null && getTileEntity().isUseableByPlayer(player);
    }
	
	@Override
    public void addCraftingToCrafters(ICrafting player) {
        super.addCraftingToCrafters(player);

        if (getTileEntity() != null) {
        	getTileEntity().initGuiData(this,player);
        }
    }

	@Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int val){
		val &= 65535;

		if (getTileEntity() != null) {
			getTileEntity().receiveGuiData(id, (short)val);
		}
    }

	@Override
	public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (getTileEntity() != null) {
			Iterator playerIterator = this.crafters.iterator();
	
			while (playerIterator.hasNext()){
				ICrafting player = (ICrafting)playerIterator.next();

				getTileEntity().checkGuiData(this,player);
			}
		}
    }	
	
	
}
