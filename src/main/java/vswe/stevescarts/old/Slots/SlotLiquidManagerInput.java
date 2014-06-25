package vswe.stevescarts.old.Slots;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import vswe.stevescarts.old.Helpers.Tank;
import vswe.stevescarts.old.TileEntities.TileEntityLiquid;
public class SlotLiquidManagerInput extends SlotBase
{
	private TileEntityLiquid manager;
	private int tankid;
    public SlotLiquidManagerInput(TileEntityLiquid manager, int tankid, int i, int j, int k)
    {
        super(manager, i, j, k);
		this.manager = manager;
		this.tankid = tankid;
    }


    public boolean isItemValid(ItemStack itemstack)
    {
    	return isItemStackValid(itemstack, this.manager, this.tankid);
    }
    
    public static boolean isItemStackValid(ItemStack itemstack, TileEntityLiquid manager, int tankid)
    {
    	if (tankid < 0 || tankid >= 4) {
    		return FluidContainerRegistry.isContainer(itemstack);
    	}
    	
		Tank tank = manager.getTanks()[tankid];
	
        return 
		(FluidContainerRegistry.isEmptyContainer(itemstack) && tank.getFluid() != null) ||
		(FluidContainerRegistry.isFilledContainer(itemstack) && (
			tank.getFluid() == null ||
			tank.getFluid().isFluidEqual(FluidContainerRegistry.getFluidForFilledItem(itemstack))
		));
		

    }   
}
