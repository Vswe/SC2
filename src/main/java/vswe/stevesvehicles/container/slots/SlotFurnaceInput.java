package vswe.stevesvehicles.container.slots;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class SlotFurnaceInput extends SlotFake {
    public SlotFurnaceInput(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        return FurnaceRecipes.smelting().getSmeltingResult(itemstack) != null;
    }

}
