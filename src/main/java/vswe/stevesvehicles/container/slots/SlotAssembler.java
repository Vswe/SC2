package vswe.stevesvehicles.container.slots;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.module.data.ModuleDataItemHandler;
import vswe.stevesvehicles.module.data.ModuleType;
import vswe.stevesvehicles.old.TileEntities.TileEntityCartAssembler;
import net.minecraft.entity.player.EntityPlayer;
public class SlotAssembler extends Slot {
	
	private ModuleType type;
	private int x;
	private int y;
	private TileEntityCartAssembler assembler;
	private int openingAnimation;
	private int internalId;
    public SlotAssembler(TileEntityCartAssembler assembler, int id, int x, int y, ModuleType type, boolean useLarge, int internalId) {
        super(assembler, id, x, y);

		this.assembler = assembler;
		this.useLarge = useLarge;
		this.type =  type;
		this.x = x;
		this.y = y;
		isValid = true;
		this.internalId = internalId;
    }

	private boolean isValid;
	private boolean useLarge;
	
	public boolean useLargeInterface() {
		return useLarge;
	}

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        return itemstack != null && isValid && ModuleDataItemHandler.isValidModuleItem(type, itemstack) && (!getHasStack() || (getStack().stackSize > 0 && itemstack.stackSize > 0));
    }

	public void invalidate() {
		isValid = false;
		invalidationCheck();
	}
	
	public void validate() {
		isValid = true;	
	}
	

	
	public boolean isValid() {
		return isValid;
	}
	
	private void invalidationCheck() {
        xDisplayPosition = -3000;
        yDisplayPosition = -3000;
        if (openingAnimation > 8) {
            openingAnimation = 8;
        }
	}
	

	public void update() {
		if (!assembler.getWorldObj().isRemote) {
	
			if (!isValid() && getHasStack()) {
				
				assembler.puke(getStack());								
				putStack(null);
				
			}

		}else{
			if (isValid()) {
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
				openingAnimation = internalId * -3;
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
            assembler.isFreeModulesOutdated = true;
		}else{
			assembler.isErrorListOutdated = true;
		}
	}
	
	@Override
	public int getSlotStackLimit() {
        return 1;
    }

	@Override
    public boolean canTakeStack(EntityPlayer player) {
        return this.getStack() != null && this.getStack().stackSize > 0 && !assembler.isInFreeMode();
    }
	

}
