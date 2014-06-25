package vswe.stevesvehicles.old.Slots;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.old.Helpers.Tank;
import vswe.stevesvehicles.old.TileEntities.TileEntityUpgrade;
public class SlotLiquidUpgradeInput extends SlotLiquidInput
{

	private TileEntityUpgrade upgrade;
    public SlotLiquidUpgradeInput(TileEntityUpgrade upgrade, Tank tank, int maxsize, int i, int j, int k)
    {
        super(upgrade, tank, maxsize, i, j, k);
        this.upgrade = upgrade;
    }

    
    public int getSlotStackLimit()
    {
    	return super.getSlotStackLimit();
    }
    
    
    public boolean isItemValid(ItemStack itemstack)
    {
        return super.isItemValid(itemstack);
    }
    

}
