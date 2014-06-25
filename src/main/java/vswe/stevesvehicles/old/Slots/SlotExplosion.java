package vswe.stevesvehicles.old.Slots;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.old.Helpers.ComponentTypes;
import vswe.stevesvehicles.old.StevesVehicles;

public class SlotExplosion extends SlotBase implements ISlotExplosions
{
    public SlotExplosion(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    public int getSlotStackLimit()
    {
        return StevesVehicles.instance.maxDynamites;
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return ComponentTypes.DYNAMITE.isStackOfType(itemstack);
    }
}
