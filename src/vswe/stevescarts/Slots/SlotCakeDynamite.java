package vswe.stevescarts.Slots;
import vswe.stevescarts.Items.Items;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Helpers.TransferHandler.TRANSFER_TYPE;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotCakeDynamite extends SlotCake implements ISlotExplosions
{
    public SlotCakeDynamite(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return super.isItemValid(itemstack) || (itemstack != null && itemstack.getItem().itemID == Items.component.itemID && itemstack.getItemDamage() == 6);
    }


}
