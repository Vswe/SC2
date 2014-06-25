package vswe.stevescarts.old.Modules.Workers.Tools;

import net.minecraft.item.ItemStack;
import vswe.stevescarts.vehicles.entities.EntityModularCart;

public class ModuleWoodcutterGalgadorian extends ModuleWoodcutter {

	public ModuleWoodcutterGalgadorian(EntityModularCart cart) {
		super(cart);
	}

	/*@Override
	public int getApplePercentageDropChance() {
		return 125;
	}

	@Override
	public int getSaplingPercentageDropChance() {
		return 125;
	}

	@Override
	public int getWoodPercentageDropChance() {
		return 100;
	}

	@Override
	public int getLogPercentageDropChance() {
		return 0;
	}

	@Override
	public int getTwigPercentageDropChance() {
		return 0;
	}*/
	
	@Override
	public int getPercentageDropChance() {
		return 125;
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

}
