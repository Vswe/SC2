package vswe.stevesvehicles.old.Slots;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.old.Modules.Workers.Tools.ModuleFarmer;

public class SlotSeed extends SlotBase
{
	private ModuleFarmer module;
    public SlotSeed(IInventory iinventory, ModuleFarmer module, int i, int j, int k)
    {
        super(iinventory, i, j, k);
		this.module = module;
    }

    public boolean isItemValid(ItemStack itemstack)
    {
		return module.isSeedValidHandler(itemstack);
    }
	

}
