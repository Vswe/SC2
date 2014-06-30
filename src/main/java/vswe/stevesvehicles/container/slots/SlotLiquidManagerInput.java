package vswe.stevesvehicles.container.slots;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import vswe.stevesvehicles.old.Helpers.Tank;
import vswe.stevesvehicles.old.TileEntities.TileEntityLiquid;
public class SlotLiquidManagerInput extends SlotBase
{
	private TileEntityLiquid manager;
	private int tankId;
    public SlotLiquidManagerInput(TileEntityLiquid manager, int tankId, int id, int x, int y)
    {
        super(manager, id, x, y);
		this.manager = manager;
		this.tankId = tankId;
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
    	return isItemStackValid(itemstack, this.manager, this.tankId);
    }
    
    public static boolean isItemStackValid(ItemStack itemstack, TileEntityLiquid manager, int tankId) {
    	if (tankId < 0 || tankId >= 4) {
    		return FluidContainerRegistry.isContainer(itemstack);
    	}
    	
		Tank tank = manager.getTanks()[tankId];
	
        return 
		(FluidContainerRegistry.isEmptyContainer(itemstack) && tank.getFluid() != null) ||
		(FluidContainerRegistry.isFilledContainer(itemstack) && (
			tank.getFluid() == null ||
			tank.getFluid().isFluidEqual(FluidContainerRegistry.getFluidForFilledItem(itemstack))
		));
		

    }   
}
