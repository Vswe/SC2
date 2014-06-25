package vswe.stevesvehicles.old.Slots;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import vswe.stevesvehicles.old.Helpers.Tank;
public class SlotLiquidInput extends SlotBase
{
	private Tank tank;
	private int maxsize;
    public SlotLiquidInput(IInventory iinventory, Tank tank, int maxsize, int i, int j, int k)
    {
        super(iinventory, i, j, k);
		this.tank = tank;
		this.maxsize = maxsize;
    }

    
    public int getSlotStackLimit()
    {
    	if (maxsize != -1) {
    		return maxsize;
    	}else{
    		return Math.min(8, tank.getCapacity() / FluidContainerRegistry.BUCKET_VOLUME);
    		
	    	/*LiquidStack liquid = tank.getLiquid();
	    	int space = tank.getCapacity();
	    	if (liquid != null) {
	    		space -= liquid.amount;
	    	}
	    	
	        return Math.min(16, 1 + space / LiquidContainerRegistry.BUCKET_VOLUME);*/
    	}
    }
    
    
    public boolean isItemValid(ItemStack itemstack)
    {
        return 
		(FluidContainerRegistry.isEmptyContainer(itemstack) && tank.getFluid() != null) ||
		(FluidContainerRegistry.isFilledContainer(itemstack) && (
			tank.getFluid() == null ||
			tank.getFluid().isFluidEqual(FluidContainerRegistry.getFluidForFilledItem(itemstack))
		));
		

    }
}
