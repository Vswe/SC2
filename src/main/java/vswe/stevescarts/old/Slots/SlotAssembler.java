package vswe.stevescarts.old.Slots;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.old.ModuleData.ModuleData;
import vswe.stevescarts.old.TileEntities.TileEntityCartAssembler;
import net.minecraft.entity.player.EntityPlayer;
public class SlotAssembler extends Slot
{
	
	private int  groupID;
	private int x;
	private int y;
	private TileEntityCartAssembler assembler;
	private int openingAnimation;
	private int id;
    public SlotAssembler(TileEntityCartAssembler assembler, int i, int j, int k, int groupID, boolean useLarge, int id)
    {
        super(assembler, i, j, k);
		this.assembler = assembler;
		this.useLarge = useLarge;
		this. groupID =  groupID;
		x = j;
		y = k;
		isValid = true;
		this.id = id;
    }
	
	
	private boolean isValid;
	private boolean useLarge;
	private boolean reloadOnUpdate;
	
	public boolean useLargeInterface() {
		return useLarge;
	}

    public boolean isItemValid(ItemStack itemstack)
    {
        return itemstack != null && isValid && ModuleData.isValidModuleItem( groupID, itemstack) && (!getHasStack() || (getStack().stackSize > 0 && itemstack.stackSize > 0));
    }
	
	//boolean fullInvalidation = false;
	public void invalidate() {
		isValid = false;
		//fullInvalidation = false;
		invalidationCheck();
	}
	
	public void validate() {
		isValid = true;	
	}
	

	
	public boolean isValid() {
		return isValid;
	}
	
	private void invalidationCheck() {
		//if (!fullInvalidation && !isValid() && !this.getHasStack()) {
			xDisplayPosition = -3000;
			yDisplayPosition = -3000;
			if (openingAnimation > 8) {
				openingAnimation = 8;
			}			
			//fullInvalidation = true;
		//}	
	}
	
	
	
	/*public boolean isPartlyInvalid() {
		return !fullInvalidation && !isValid();
	}*/
	
	public void update()
    {
		if (!assembler.getWorldObj().isRemote) {
	
			if (!isValid() && getHasStack()) {
				
				assembler.puke(getStack());								
				putStack(null);
				
			}
			
		
		}else{
			if (isValid()/*!fullInvalidation*/) {
				if (openingAnimation == 8) {
					xDisplayPosition = x;
					yDisplayPosition = y;
					openingAnimation++;
				}else if (openingAnimation < 8){
					openingAnimation++;
				}
				
			}else if (openingAnimation > 0){
				openingAnimation--;
			}else {
				openingAnimation = id * -3;
			}
		}
	}
	
	public int getAnimationTick() {
		return openingAnimation;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}	
	
	public TileEntityCartAssembler getAssembler() {
		return assembler;
	}
	
	public boolean shouldUpdatePlaceholder() {
		return true;
	}
	
	@Override
	public void onSlotChanged() {
		super.onSlotChanged();
		

		if (shouldUpdatePlaceholder()) {
			assembler.updatePlaceholder();
		}else{
			assembler.isErrorListOutdated = true;
		}

	}
	
	@Override
	public int getSlotStackLimit()
    {
        return 1;
    }
	@Override
    public boolean canTakeStack(EntityPlayer player)
    {
        return this.getStack() != null && this.getStack().stackSize > 0;
    }
	

}
