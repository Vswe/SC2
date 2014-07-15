package vswe.stevesvehicles.container.slots;
import java.util.ArrayList;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.module.common.addon.enchanter.EnchantmentInfo;

public class SlotEnchantment extends SlotBase {

	private ArrayList<EnchantmentInfo.Enchantment_Type> enabledTypes;
    public SlotEnchantment(IInventory inventory, ArrayList<EnchantmentInfo.Enchantment_Type> enabledTypes, int id, int x, int y) {
        super(inventory, id, x, y);
        this.enabledTypes = enabledTypes;
    }


    @Override
    public boolean isItemValid(ItemStack itemstack) {
        return EnchantmentInfo.isItemValid(enabledTypes, itemstack);
    }
    

}
