package vswe.stevescarts.Slots;
import java.util.ArrayList;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Helpers.EnchantmentInfo;
import vswe.stevescarts.Helpers.EnchantmentInfo.ENCHANTMENT_TYPE;
import vswe.stevescarts.Helpers.TransferHandler.TRANSFER_TYPE;
import vswe.stevescarts.Modules.Workers.Tools.ModuleTool;
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
