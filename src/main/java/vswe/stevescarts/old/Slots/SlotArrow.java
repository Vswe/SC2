package vswe.stevescarts.old.Slots;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.old.Modules.Realtimers.ModuleShooter;

public class SlotArrow extends SlotBase
{
	private ModuleShooter shooter;
    public SlotArrow(IInventory iinventory, ModuleShooter shooter, int i, int j, int k)
    {
        super(iinventory, i, j, k);
		this.shooter = shooter;
    }

    public boolean isItemValid(ItemStack itemstack)
    {   	
        return shooter.isValidProjectileItem(itemstack);
    }
}