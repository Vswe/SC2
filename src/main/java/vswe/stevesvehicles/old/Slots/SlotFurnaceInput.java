package vswe.stevesvehicles.old.Slots;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class SlotFurnaceInput extends SlotFake {
    public SlotFurnaceInput(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return FurnaceRecipes.smelting().getSmeltingResult(itemstack) != null;
    }

}
