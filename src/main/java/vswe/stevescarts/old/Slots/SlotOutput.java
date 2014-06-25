package vswe.stevescarts.old.Slots;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.old.Items.ModItems;
import vswe.stevescarts.old.TileEntities.TileEntityCartAssembler;

public class SlotOutput extends SlotAssembler
{
	

    public SlotOutput(TileEntityCartAssembler assembler, int i, int j, int k)
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
