package vswe.stevesvehicles.container.slots;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.module.common.attachment.ModuleShooter;

public class SlotArrow extends SlotBase {
	private ModuleShooter shooter;
    public SlotArrow(IInventory inventory, ModuleShooter shooter, int id, int x, int y) {
        super(inventory, id, x, y);
		this.shooter = shooter;
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        return shooter.isValidProjectileItem(itemstack);
    }
}