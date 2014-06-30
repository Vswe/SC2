package vswe.stevesvehicles.container.slots;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.module.cart.tool.ModuleFarmer;

public class SlotSeed extends SlotBase {
	private ModuleFarmer module;
    public SlotSeed(IInventory inventory, ModuleFarmer module, int id, int x, int y) {
        super(inventory, id, x, y);
		this.module = module;
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
		return module.isSeedValidHandler(itemstack);
    }
	

}
