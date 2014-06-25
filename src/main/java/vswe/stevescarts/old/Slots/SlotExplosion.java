package vswe.stevescarts.old.Slots;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.old.Helpers.ComponentTypes;
import vswe.stevescarts.old.StevesCarts;

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
