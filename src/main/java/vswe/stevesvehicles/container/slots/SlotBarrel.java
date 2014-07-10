package vswe.stevesvehicles.container.slots;


import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.module.common.storage.barrel.ModuleBarrel;

public class SlotBarrel extends SlotChest {
    private ModuleBarrel barrel;
    private boolean input;
    public SlotBarrel(IInventory inventory, ModuleBarrel barrel, int id, int x, int y, boolean input) {
        super(inventory, id, x, y);
        this.barrel = barrel;
        this.input = input;
    }


    @Override
    public boolean isItemValid(ItemStack item) {
        return input && barrel.isItemValid(item);
    }
}
