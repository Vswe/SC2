package vswe.stevesvehicles.container.slots;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotCake extends SlotBase {
    public SlotCake(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        return itemstack != null && itemstack.getItem() == Items.cake;
    }

}
