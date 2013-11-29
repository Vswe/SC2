package vswe.stevescarts.Slots;
import vswe.stevescarts.StevesCarts;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;
import vswe.stevescarts.Upgrades.BaseEffect;
import vswe.stevescarts.Upgrades.Disassemble;
public class SlotCartDisassemble extends SlotCart
{
    public SlotCartDisassemble(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

	@Override
    public boolean isItemValid(ItemStack itemstack)
    {
		if (this.inventory instanceof TileEntityUpgrade) {
			TileEntityUpgrade upgrade = (TileEntityUpgrade)this.inventory;
			if (upgrade.getUpgrade() != null) {
				for (BaseEffect effect : upgrade.getUpgrade().getEffects()) {
					if (effect instanceof Disassemble) {
						return ((Disassemble)effect).canDisassemble(upgrade) == 2 && super.isItemValid(itemstack);
					}
				}
			}
			
		}
		
		return false;
	
        
    }
}