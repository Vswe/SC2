package vswe.stevesvehicles.old.Modules.Workers.Tools;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.vehicles.entities.EntityModularCart;
import vswe.stevesvehicles.old.Helpers.Localization;

public class ModuleFarmerDiamond extends ModuleFarmer {

	public ModuleFarmerDiamond(EntityModularCart cart) {
		super(cart);
	}
	
	@Override
	public int getMaxDurability() {
		return 300000;
	}
	
	@Override
	public String getRepairItemName() {
		return Localization.MODULES.TOOLS.DIAMONDS.translate();
	}
	
	@Override
	public int getRepairItemUnits(ItemStack item) {
		if (item != null && item.getItem() == Items.diamond) {
			return 150000;
		}
		return 0;
	}
	
	@Override
	public boolean useDurability() {
		return true;
	}
	
	@Override
	public int getRepairSpeed() {
		return 500;
	}

	@Override
	public int getRange() {
		return 1;
	}

}
