package vswe.stevesvehicles.old.Slots;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.module.data.ModuleType;
import vswe.stevesvehicles.old.Items.ModItems;
import vswe.stevesvehicles.old.TileEntities.TileEntityCartAssembler;

public class SlotOutput extends SlotAssembler
{
	

    public SlotOutput(TileEntityCartAssembler assembler, int i, int j, int k)
    {
        super(assembler, i, j, k, ModuleType.INVALID, true,0);

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
		if (!getAssembler().getIsAssembling() && itemstack.getItem() == ModItems.carts) {
			NBTTagCompound info = itemstack.getTagCompound();
			if (info != null && info.hasKey("maxTime")) {	
				return true;
			}
			
		}
        return false;
    }	
	
	public boolean shouldUpdatePlaceholder() {
		return false;
	}
	
}
