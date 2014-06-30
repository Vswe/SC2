package vswe.stevesvehicles.container.slots;
import vswe.stevesvehicles.old.Items.ModItems;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;
public class SlotCart extends Slot {
    public SlotCart(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

	@Override
    public boolean isItemValid(ItemStack itemstack) {
        return itemstack != null && itemstack.getItem() == ModItems.carts && itemstack.getTagCompound() != null && !itemstack.getTagCompound().hasKey("maxTime");
    }
}