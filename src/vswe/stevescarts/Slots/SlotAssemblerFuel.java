package vswe.stevescarts.Slots;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.FluidContainerRegistry;
import vswe.stevescarts.TileEntities.TileEntityCartAssembler;

public class SlotAssemblerFuel extends SlotAssembler
{
	

    public SlotAssemblerFuel(TileEntityCartAssembler assembler, int i, int j, int k)
    {
        super(assembler, i, j, k, 0, true,0);

    }

	@Override
	public void validate() {

	}	
	
	@Override
	public void invalidate() {

	}
	
	@Override
    public boolean isItemValid(ItemStack itemstack)
    {
		
        if (itemstack.getItem().itemID == Item.coal.itemID) {
			return true;
		}else if(getAssembler().isCombustionFuelValid()) {
			return !FluidContainerRegistry.isFilledContainer(itemstack) && TileEntityFurnace.getItemBurnTime(itemstack) > 0;
		}else{
			return false;
		}
		
    }	
	
	public int getFuelLevel(ItemStack itemstack) {
		if (isItemValid(itemstack)) {
			return (int)(TileEntityFurnace.getItemBurnTime(itemstack) * 0.25F);
		}else{
			return 0;
		}
	}
		
	public boolean shouldUpdatePlaceholder() {
		return false;
	}
	
	public int getSlotStackLimit()
    {
        return 64;
    }
}
