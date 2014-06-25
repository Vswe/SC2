package vswe.stevesvehicles.old.Modules.Addons;

import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.vehicles.entities.EntityModularCart;

public class ModuleCreativeIncinerator extends ModuleIncinerator {

	public ModuleCreativeIncinerator(EntityModularCart cart) {
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
