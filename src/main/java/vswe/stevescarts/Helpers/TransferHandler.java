package vswe.stevescarts.Helpers;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Slots.ISpecialItemTransferValidator;
import vswe.stevescarts.Slots.ISpecialSlotValidator;

public class TransferHandler {
	
	public static enum TRANSFER_TYPE {
		SHIFT,
		MANAGER,
		OTHER
	}
	
	public static boolean isSlotOfType(Slot slot, java.lang.Class slotType) {
		if (slot instanceof ISpecialSlotValidator) {
			ISpecialSlotValidator specSlot = (ISpecialSlotValidator)slot;
			return specSlot.isSlotValid();
		}
	
		return slotType.isInstance(slot);
	}	
	
	public static boolean isItemValidForTransfer(Slot slot, ItemStack item, TRANSFER_TYPE type) {
		if (slot instanceof ISpecialItemTransferValidator) {
			ISpecialItemTransferValidator specSlot = (ISpecialItemTransferValidator)slot;
			return specSlot.isItemValidForTransfer(item, type);			
		}
		
		return slot.isItemValid(item);
	}
	
	public static void TransferItem(ItemStack iStack, IInventory inv, Container cont, int maxItems)
    {
		TransferItem(iStack, inv, cont, Slot.class, null, maxItems);
	}	
	public static void TransferItem(ItemStack iStack, IInventory inv, Container cont, java.lang.Class validSlot, int maxItems, TRANSFER_TYPE type)
    {
		TransferItem(iStack, inv, 0, inv.getSizeInventory() -1, cont, validSlot, null, maxItems, type, false);
	}
    public static void TransferItem(ItemStack iStack, IInventory inv, Container cont, java.lang.Class validSlot,java.lang.Class invalidSlot, int maxItems)
    {	
    	TransferItem(iStack, inv, 0, inv.getSizeInventory() -1, cont, validSlot,invalidSlot, maxItems);
	}
    public static void TransferItem(ItemStack iStack, IInventory inv, int start, int end, Container cont, java.lang.Class validSlot,java.lang.Class invalidSlot, int maxItems) {
    	TransferItem(iStack, inv, start, end, cont, validSlot, invalidSlot, maxItems, TRANSFER_TYPE.OTHER, false);
    }  
    public static void TransferItem(ItemStack iStack, IInventory inv, int start, int end, Container cont, java.lang.Class validSlot,java.lang.Class invalidSlot, int maxItems, TRANSFER_TYPE type, boolean fake)
    {
		start = Math.max(0, start);
		end = Math.min(inv.getSizeInventory() -1, end);
	
		int startEmpty = start;
		int startOccupied = start;
		//pos will hold possible positions to place the item in
        int pos;

        //this loop will loop as long as there's more items to add to the inventory and it's still possible to add those
        do
        {
        	
        	
            //pos = -1 means there's no valid pos(we have to start at that)
            pos = -1;

            //first loop through the inventory once, this will try to find already used stacks where we could put the items
            for (int i = startEmpty; i <= end; i++)
            {
                if (isSlotOfType(cont.getSlot(i),validSlot) && (invalidSlot == null || !isSlotOfType(cont.getSlot(i),invalidSlot)))
                {
                    //a super heavy if-statement. This will just compare the item in the slot and the item we want to store, if they are the same in various different properties they can be stored together.
                    if (
                            inv.getStackInSlot(i) != null &&
                            inv.getStackInSlot(i).itemID == iStack.itemID &&
                            inv.getStackInSlot(i).isStackable() &&
                            inv.getStackInSlot(i).stackSize < inv.getStackInSlot(i).getMaxStackSize() &&
                            inv.getStackInSlot(i).stackSize < cont.getSlot(i).getSlotStackLimit() &&
							(
								inv.getStackInSlot(i).stackSize > 0 &&
								iStack.stackSize > 0 
							) &&							
                            (
                                    !inv.getStackInSlot(i).getHasSubtypes() ||
                                    inv.getStackInSlot(i).getItemDamage() == iStack.getItemDamage()
                            ) &&
							(
								inv.getStackInSlot(i).getTagCompound() == null ||
								inv.getStackInSlot(i).getTagCompound().equals(iStack.getTagCompound())								
							)
							
							
                    )
                    {
                        //mark this slot as a good one, break the loop since we're done here.
                        pos = i;
                        startEmpty = pos + 1;
                        break;
                    }
                }
            }

            //if there wasn't any occupied slot with room for our items it's time to search for an empty slot instead
            if (pos == -1)
            {
                //loop through the inventory again
                for (int i = startOccupied; i <= end; i++)
                {
                    if (isSlotOfType(cont.getSlot(i),validSlot) && (invalidSlot == null || !isSlotOfType(cont.getSlot(i),invalidSlot)))
                    {
                        //if this slot is valid for our item and also is empty
                        Slot slot = cont.getSlot(i);

                        if (isItemValidForTransfer(slot, iStack, type) && inv.getStackInSlot(i) == null)
                        {
                            //mark this slot as a good one, break the loop since we're done here.
                            pos = i;
                            startOccupied = pos + 1;
                            break;
                        }
                    }
                }
            }

            //as long as we've found a slot add the items there
            if (pos != -1)
            {
                ItemStack existingItem = null;
            	
            	//first of all, if the slot is empty create an empty ItemStack there with our item. In this way we can use the same code to add to an existing ItemStack and a new one.
                if (inv.getStackInSlot(pos) == null)
                {
					ItemStack clone = iStack.copy();
					clone.stackSize = 0;
					if (!fake) {
						inv.setInventorySlotContents(pos, clone);
					}
                    existingItem = clone;
                }else{
                	existingItem = inv.getStackInSlot(pos);
                }

                //now it's time to calculate how many items we can transfer. This is done by comparing the stack size with all stack limits.
                int stackSize = iStack.stackSize;

                if (stackSize > existingItem.getMaxStackSize() - existingItem.stackSize)
                {
                    stackSize = existingItem.getMaxStackSize() - existingItem.stackSize;
                }

                if (stackSize > cont.getSlot(pos).getSlotStackLimit() - existingItem.stackSize)
                {
                    stackSize = cont.getSlot(pos).getSlotStackLimit() - existingItem.stackSize;
                }

                boolean killMe = false;
				if (maxItems != -1) {
					if (stackSize > maxItems)
					{
						stackSize = maxItems;
						killMe = true;
					}
					
					maxItems -= stackSize;
				}
				

                //if there's no items to be moved there won't be any difference and therefore we have to exit(no infinite loops here)
                if (stackSize <= 0)
                {
                    //mark the slot position as invalid to exit the loop
                    pos = -1;
                }
                else
                {
                    //decrease the items in the stack we want to store and increase the items in the stack in the cart
                    iStack.stackSize -= stackSize;
                    if (!fake) {
                    	inv.getStackInSlot(pos).stackSize += stackSize;
                    }

                    //if the stack we want to store is empty we're done and therefore mark the slot position as invalid to exit the loop. If it isn't marked as invalid the loop will loop again to try to store the rest of the items.
                    if (iStack.stackSize == 0 || killMe || maxItems == 0)
                    {
                        pos = -1;
                    }
                }
            }
        }
        while (pos != -1);  //loop as long as there's more work to be done.
		
        if (!fake) {
        	inv.onInventoryChanged();
        }
    }
}
