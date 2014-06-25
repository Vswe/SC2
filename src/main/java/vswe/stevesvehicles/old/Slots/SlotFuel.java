package vswe.stevesvehicles.old.Slots;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.FluidContainerRegistry;
import vswe.stevesvehicles.old.Modules.Engines.ModuleCoalBase;
public class SlotFuel extends SlotBase
{
    public SlotFuel(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return getItemBurnTime(itemstack) > 0;
    }
	
	private boolean isValid(ItemStack itemstack) {
		return !FluidContainerRegistry.isFilledContainer(itemstack);
	}

    private int getItemBurnTime(ItemStack itemstack)
    {
        return isValid(itemstack) ? (int)(TileEntityFurnace.getItemBurnTime(itemstack)) : 0;
    }
    public static int getItemBurnTime(ModuleCoalBase engine, ItemStack itemstack)
    {
        return (int)(TileEntityFurnace.getItemBurnTime(itemstack) * engine.getFuelMultiplier());
    }	
	
}
