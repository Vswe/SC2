package vswe.stevescarts.old.Slots;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.old.Helpers.TransferHandler.TRANSFER_TYPE;
import vswe.stevescarts.old.Modules.Workers.Tools.ModuleTool;
public class SlotRepair extends SlotBase implements ISpecialItemTransferValidator
{
	private ModuleTool tool;
    public SlotRepair(ModuleTool tool, IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
        
        this.tool = tool;
    }

    @Override
    public boolean isItemValidForTransfer(ItemStack item, TRANSFER_TYPE type) {
    	return false;
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return tool.isValidRepairMaterial(itemstack);
    }
    

}
