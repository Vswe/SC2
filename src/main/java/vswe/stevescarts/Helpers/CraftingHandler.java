package vswe.stevescarts.Helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Items.ModItems;
import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class CraftingHandler implements ICraftingHandler
{
	public CraftingHandler() {
		GameRegistry.registerCraftingHandler(this);
	}

    /**
     * The object array contains these three arguments
     *
     * @param player
     * @param item
     * @param craftMatrix
     */
    public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix) {
		if (item.getItem() == ModItems.component || item.getItem() == ModItems.modules) {
			for (int i = 0; i < craftMatrix.getSizeInventory();i++) {
				ItemStack sItem = craftMatrix.getStackInSlot(i);
				if (sItem != null && sItem.getItem().getContainerItem() != null) {
					craftMatrix.setInventorySlotContents(i, null);
				}
			}
		}
	}

    /**
     * The object array contains these two arguments
     * @param player
     * @param item
     */
    public void onSmelting(EntityPlayer player, ItemStack item) {
	
	}
	

	
}