package vswe.stevesvehicles.container.slots;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.old.TileEntities.TileEntityUpgrade;
import vswe.stevesvehicles.old.Upgrades.BaseEffect;
import vswe.stevesvehicles.old.Upgrades.Disassemble;
public class SlotCartDisassemble extends SlotCart {
    public SlotCartDisassemble(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

	@Override
    public boolean isItemValid(ItemStack itemstack) {
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