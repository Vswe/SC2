package vswe.stevesvehicles.old.Modules.Workers.Tools;

import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.vehicles.entities.EntityModularCart;

public class ModuleFarmerGalgadorian extends ModuleFarmer {

	public ModuleFarmerGalgadorian(EntityModularCart cart) {
		super(cart);
	}
	
	@Override
	public int getMaxDurability() {
		return 1;
	}
	
	@Override
	public String getRepairItemName() {
		return null;
	}
	
	@Override
	public int getRepairItemUnits(ItemStack item) {
		return 0;
	}
	
	@Override
	public boolean useDurability() {
		return false;
	}
	
	@Override
	public int getRepairSpeed() {
		return 1;
	}
	
	@Override
	public int getRange() {
		return 2;
	}	

}
