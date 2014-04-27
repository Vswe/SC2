package vswe.stevescarts.Modules.Addons;

import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;

public class ModuleCreativeIncinerator extends ModuleIncinerator {

	public ModuleCreativeIncinerator(MinecartModular cart) {
		super(cart);
	}

	protected int getIncinerationCost() {
		return 0;
	}
	
	protected boolean isItemValid(ItemStack item) {
		return item != null;
	}	
	
	@Override
	public boolean hasGui() {
		return false;
	}		
	
}
