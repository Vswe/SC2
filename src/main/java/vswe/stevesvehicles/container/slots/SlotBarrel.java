package vswe.stevesvehicles.container.slots;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.module.common.storage.barrel.ModuleBarrel;

public class SlotBarrel extends SlotChest implements ISpecialSlotRender {
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

    @SideOnly(Side.CLIENT)
    @Override
    public boolean renderSlot(ItemStack slotItem, boolean shouldSlotBeRendered, boolean shouldSlotItemBeRendered, boolean shouldSlotUnderlayBeRendered, boolean shouldSlotOverlayBeRendered, String info) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack getStackToRender(ItemStack slotItem) {
        if (slotItem != null && slotItem.stackSize == 0) {
            return null;
        }else{
            return slotItem;
        }
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        return getStack() == null || getStack().stackSize > 0;
    }
}
