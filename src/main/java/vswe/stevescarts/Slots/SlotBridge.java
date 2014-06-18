package vswe.stevescarts.Slots;
import net.minecraft.init.Blocks;
import vswe.stevescarts.Helpers.TransferHandler.TRANSFER_TYPE;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotBridge extends SlotBase  implements ISpecialItemTransferValidator
{
    public SlotBridge(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return isBridgeMaterial(itemstack);
    }

    public static boolean isBridgeMaterial(ItemStack itemstack)
    {
        Block b = Block.getBlockFromItem(itemstack.getItem());
        return  b == Blocks.planks ||
                b == Blocks.brick_block ||
                b == Blocks.stone ||
                (b == Blocks.stonebrick && itemstack.getItemDamage() == 0);
    }
    
    
    //don't allow the bridge builder to use picked up materials
	@Override
	public boolean isItemValidForTransfer(ItemStack item, TRANSFER_TYPE type) {
		return isItemValid(item) && type != TRANSFER_TYPE.OTHER;
	}
	
}
