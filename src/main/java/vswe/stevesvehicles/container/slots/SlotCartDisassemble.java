package vswe.stevesvehicles.container.slots;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.tileentity.TileEntityUpgrade;
import vswe.stevesvehicles.upgrade.effect.BaseEffect;
import vswe.stevesvehicles.upgrade.effect.assembly.Disassemble;
public class SlotCartDisassemble extends SlotCart {
    public SlotCartDisassemble(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

	@Override
    public boolean isItemValid(ItemStack itemstack) {
		if (this.inventory instanceof TileEntityUpgrade) {
			TileEntityUpgrade upgrade = (TileEntityUpgrade)this.inventory;

            for (BaseEffect effect : upgrade.getEffects()) {
                if (effect instanceof Disassemble) {
                    return ((Disassemble)effect).canDisassemble(upgrade) == 2 && super.isItemValid(itemstack);
                }
            }

			
		}
		
		return false;
    }
}