package vswe.stevesvehicles.container.slots;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import vswe.stevesvehicles.old.Helpers.Tank;
public class SlotLiquidInput extends SlotBase {
	private Tank tank;
	private int maxsize;
    public SlotLiquidInput(IInventory inventory, Tank tank, int maxsize, int id, int x, int y) {
        super(inventory, id, x, y);
		this.tank = tank;
		this.maxsize = maxsize;
    }

    @Override
    public int getSlotStackLimit() {
    	if (maxsize != -1) {
    		return maxsize;
    	}else{
    		return Math.min(8, tank.getCapacity() / FluidContainerRegistry.BUCKET_VOLUME);
    	}
    }
    
    @Override
    public boolean isItemValid(ItemStack itemstack) {
        return 
		(FluidContainerRegistry.isEmptyContainer(itemstack) && tank.getFluid() != null) ||
		(FluidContainerRegistry.isFilledContainer(itemstack) && (
			tank.getFluid() == null ||
			tank.getFluid().isFluidEqual(FluidContainerRegistry.getFluidForFilledItem(itemstack))
		));
		

    }
}
