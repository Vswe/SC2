package vswe.stevesvehicles.container.slots;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.FluidContainerRegistry;
import vswe.stevesvehicles.module.common.engine.ModuleCoalBase;
public class SlotFuel extends SlotBase {
    public SlotFuel(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        return getItemBurnTime(itemstack) > 0;
    }
	
	private boolean isValid(ItemStack itemstack) {
		return !FluidContainerRegistry.isFilledContainer(itemstack);
	}

    private int getItemBurnTime(ItemStack itemstack) {
        return isValid(itemstack) ? TileEntityFurnace.getItemBurnTime(itemstack) : 0;
    }
    public static int getItemBurnTime(ModuleCoalBase engine, ItemStack itemstack) {
        return (int)(TileEntityFurnace.getItemBurnTime(itemstack) * engine.getFuelMultiplier());
    }	
	
}
