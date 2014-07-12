package vswe.stevesvehicles.container.slots;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.FluidContainerRegistry;
import vswe.stevesvehicles.module.data.ModuleType;
import vswe.stevesvehicles.tileentity.TileEntityCartAssembler;

public class SlotAssemblerFuel extends SlotAssembler {
	

    public SlotAssemblerFuel(TileEntityCartAssembler assembler, int id, int x, int y) {
        super(assembler, id, x, y, ModuleType.INVALID, true,0);
    }

	@Override
	public void validate() {

	}	
	
	@Override
	public void invalidate() {

	}
	
	@Override
    public boolean isItemValid(ItemStack itemstack) {
        return itemstack.getItem() == Items.coal || (getAssembler().isCombustionFuelValid() && !FluidContainerRegistry.isFilledContainer(itemstack) && TileEntityFurnace.getItemBurnTime(itemstack) > 0);
    }	

    public static final float FUEL_MULTIPLIER = 0.25F;

	public int getFuelLevel(ItemStack itemstack) {
		if (isItemValid(itemstack)) {
			return (int)(TileEntityFurnace.getItemBurnTime(itemstack) * FUEL_MULTIPLIER);
		}else{
			return 0;
		}
	}

    @Override
	public boolean shouldUpdatePlaceholder() {
		return false;
	}

    @Override
	public int getSlotStackLimit() {
        return 64;
    }
}
