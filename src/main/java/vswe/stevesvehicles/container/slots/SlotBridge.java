package vswe.stevesvehicles.container.slots;
import net.minecraft.init.Blocks;
import vswe.stevesvehicles.old.Helpers.TransferHandler;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotBridge extends SlotBase implements ISpecialItemTransferValidator {
    public SlotBridge(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

    public boolean isItemValid(ItemStack itemstack) {
        return isBridgeMaterial(itemstack);
    }

    public static boolean isBridgeMaterial(ItemStack itemstack) {
        Block b = Block.getBlockFromItem(itemstack.getItem());
        return  b == Blocks.planks ||
                b == Blocks.brick_block ||
                b == Blocks.stone ||
                (b == Blocks.stonebrick && itemstack.getItemDamage() == 0);
    }
    
    
    //don't allow the bridge builder to use picked up materials
	@Override
	public boolean isItemValidForTransfer(ItemStack item, TransferHandler.TransferType type) {
		return isItemValid(item) && type != TransferHandler.TransferType.OTHER;
	}
	
}
