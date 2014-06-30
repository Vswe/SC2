package vswe.stevesvehicles.container.slots;
import vswe.stevesvehicles.old.Items.ModItems;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;
public class SlotModule extends Slot {
    public SlotModule(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

	@Override
    public boolean isItemValid(ItemStack itemstack) {
        return itemstack.getItem() == ModItems.modules;
    }
}