package vswe.stevesvehicles.container.slots;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

public interface ISpecialSlotRender {
    @SideOnly(Side.CLIENT)
    boolean renderSlot(ItemStack slotItem, boolean shouldSlotBeRendered, boolean shouldSlotItemBeRendered, boolean shouldSlotUnderlayBeRendered, boolean shouldSlotOverlayBeRendered, String info);
    @SideOnly(Side.CLIENT)
    ItemStack getStackToRender(ItemStack slotItem);
}
