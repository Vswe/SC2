package vswe.stevescarts.Helpers;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class CreativeTabSC2 extends CreativeTabs {

	public CreativeTabSC2(String label) {
		super(label);
	}

	@SideOnly(Side.CLIENT)
	@Override
    /**
     * the item to be displayed on the tab
     */
    public ItemStack getIconItemStack()
    {
        return item;
    }
	
	private ItemStack item;
	public void setIcon(ItemStack item) {
		this.item = item;
	}

}