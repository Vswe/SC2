package vswe.stevesvehicles.container.slots;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.item.ComponentTypes;
import vswe.stevesvehicles.StevesVehicles;

public class SlotExplosion extends SlotBase implements ISlotExplosions {
    public SlotExplosion(IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
    }

    @Override
    public int getSlotStackLimit()
    {
        return StevesVehicles.instance.maxDynamites;
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        return ComponentTypes.DYNAMITE.isStackOfType(itemstack);
    }
}
