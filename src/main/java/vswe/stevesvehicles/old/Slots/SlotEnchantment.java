package vswe.stevesvehicles.old.Slots;
import java.util.ArrayList;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.old.Helpers.EnchantmentInfo;
import vswe.stevesvehicles.old.Helpers.EnchantmentInfo.ENCHANTMENT_TYPE;

public class SlotEnchantment extends SlotBase
{

	private ArrayList<ENCHANTMENT_TYPE> enabledTypes;
    public SlotEnchantment(IInventory iinventory, ArrayList<ENCHANTMENT_TYPE> enabledTypes, int i, int j, int k)
    {
        super(iinventory, i, j, k);
        this.enabledTypes = enabledTypes;
    }



    public boolean isItemValid(ItemStack itemstack)
    {
        return EnchantmentInfo.isItemValid(enabledTypes, itemstack);
    }
    

}
