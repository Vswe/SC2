package vswe.stevescarts.Slots;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Helpers.ComponentTypes;
import vswe.stevescarts.Items.Items;
import vswe.stevescarts.StevesCarts;

public class SlotExplosion extends SlotBase implements ISlotExplosions
{
    public SlotExplosion(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    public int getSlotStackLimit()
    {
        return StevesCarts.instance.maxDynamites;
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return ComponentTypes.DYNAMITE.isStackOfType(itemstack);
    }
}
