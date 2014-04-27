package vswe.stevescarts.Slots;
import vswe.stevescarts.Helpers.TransferHandler.TRANSFER_TYPE;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class SlotFurnaceInput extends SlotFake {
    public SlotFurnaceInput(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return FurnaceRecipes.smelting().getSmeltingResult(itemstack) != null;
    }

}
