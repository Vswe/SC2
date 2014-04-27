package vswe.stevescarts.Containers;
import java.util.Iterator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Helpers.TransferHandler;
import vswe.stevescarts.Helpers.TransferHandler.TRANSFER_TYPE;
import vswe.stevescarts.TileEntities.TileEntityBase;

public abstract class ContainerBase extends Container
{
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
	public ItemStack transferStackInSlot(EntityPlayer player, int i)
    {
		if (getMyInventory() == null) {
			return null;
		}
		
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);
        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(i < getMyInventory().getSizeInventory())
            {
                if(!mergeItemStack(itemstack1, getMyInventory().getSizeInventory()+28, getMyInventory().getSizeInventory()+36, false))
                {
					if(!mergeItemStack(itemstack1, getMyInventory().getSizeInventory(), getMyInventory().getSizeInventory()+28, false))
					{
						return null;
					}
                }
            }else if(!mergeItemStack(itemstack1, 0, getMyInventory().getSizeInventory(), false)){
                return null;
            }
            if(itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            } else
            {
                slot.onSlotChanged();
            }
            if(itemstack1.stackSize != itemstack.stackSize)
            {			
               slot.onPickupFromSlot(player,itemstack1);
            } else
            {
                return null;
            }
        }
        return itemstack;
    }

	@Override
    protected boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4)
    {
		if (getMyInventory() == null) {
			return false;
		}		
		
        boolean var5 = false;
        int var6 = par2;

        if (par4)
        {
            var6 = par3 - 1;
        }

        Slot var7;
        ItemStack var8;

        if (par1ItemStack.isStackable())
        {
            while (par1ItemStack.stackSize > 0 && (!par4 && var6 < par3 || par4 && var6 >= par2))
            {
                var7 = (Slot)this.inventorySlots.get(var6);
                var8 = var7.getStack();

                if (var8 != null && var8.stackSize > 0 && var8.itemID == par1ItemStack.itemID && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == var8.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, var8))
                {
                    int var9 = var8.stackSize + par1ItemStack.stackSize;

					int maxLimit = Math.min(par1ItemStack.getMaxStackSize(),var7.getSlotStackLimit());
                    if (var9 <= maxLimit)
                    {
                        par1ItemStack.stackSize = 0;
                        var8.stackSize = var9;
                        var7.onSlotChanged();
                        var5 = true;
                    }
                    else if (var8.stackSize < maxLimit)
                    {
                        par1ItemStack.stackSize -= maxLimit - var8.stackSize;
                        var8.stackSize = maxLimit;
                        var7.onSlotChanged();
                        var5 = true;
                    }
                }

                if (par4)
                {
                    --var6;
                }
                else
                {
                    ++var6;
                }
            }
        }

        if (par1ItemStack.stackSize > 0)
        {
            if (par4)
            {
                var6 = par3 - 1;
            }
            else
            {
                var6 = par2;
            }

            while (!par4 && var6 < par3 || par4 && var6 >= par2)
            {
                var7 = (Slot)this.inventorySlots.get(var6);
                var8 = var7.getStack();

                if (var8 == null && TransferHandler.isItemValidForTransfer(var7, par1ItemStack, TRANSFER_TYPE.SHIFT))
                {
					int stackSize = Math.min(var7.getSlotStackLimit(), par1ItemStack.stackSize);
					ItemStack newItem = par1ItemStack.copy();
					newItem.stackSize = stackSize;
					par1ItemStack.stackSize -= stackSize;					
                    var7.putStack(newItem);
                    var7.onSlotChanged();
                    
                    var5 = par1ItemStack.stackSize == 0;;
                    break;
                }

                if (par4)
                {
                    --var6;
                }
                else
                {
                    ++var6;
                }
            }
        }

        return var5;
    }
	

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
    {
       return getTileEntity() != null && getTileEntity().isUseableByPlayer(entityplayer);
    }
	
	@Override
    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);

        if (getTileEntity() != null) {
        	getTileEntity().initGuiData(this,par1ICrafting);
        }
    }

	@Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2)
    {
		par2 &= 65535;

		if (getTileEntity() != null) {
			getTileEntity().receiveGuiData(par1, (short)par2);
		}
    }

	@Override
	public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        if (getTileEntity() != null) {
			Iterator var1 = this.crafters.iterator();
	
			while (var1.hasNext())
			{
				ICrafting var2 = (ICrafting)var1.next();
	
				
				getTileEntity().checkGuiData(this,var2);
			}
		}
    }	
	
	
}
