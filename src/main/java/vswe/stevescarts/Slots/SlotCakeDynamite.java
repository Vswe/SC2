package vswe.stevescarts.Slots;
import vswe.stevescarts.Items.ModItems;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotCakeDynamite extends SlotCake implements ISlotExplosions
{
    public SlotCakeDynamite(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return super.isItemValid(itemstack) || (itemstack != null && itemstack.getItem().itemID == ModItems.component.itemID && itemstack.getItemDamage() == 6);
    }


}
