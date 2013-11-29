package vswe.stevescarts.Slots;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotFirework extends SlotBase
{
    public SlotFirework(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    public boolean isItemValid(ItemStack itemstack)
    {
		int id = itemstack.getItem().itemID;
	
        return 
			id == Item.firework.itemID ||		
		
			id == Item.gunpowder.itemID ||
			id == Item.fireworkCharge.itemID ||
			id == Item.dyePowder.itemID ||
			id == Item.paper.itemID ||
			id == Item.glowstone.itemID ||
			id == Item.diamond.itemID ||
			id == Item.fireballCharge.itemID ||
			id == Item.feather.itemID ||
			id == Item.goldNugget.itemID ||
			id == Item.skull.itemID;			
    }
}
