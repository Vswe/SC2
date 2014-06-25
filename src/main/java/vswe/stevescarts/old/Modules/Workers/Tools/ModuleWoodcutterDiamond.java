package vswe.stevescarts.old.Modules.Workers.Tools;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.old.Helpers.Localization;
import vswe.stevescarts.vehicles.entities.EntityModularCart;

public class ModuleWoodcutterDiamond extends ModuleWoodcutter {

	public ModuleWoodcutterDiamond(EntityModularCart cart) {
		super(cart);
	}

	/*@Override
	public int getApplePercentageDropChance() {
		return 90;
	}

	@Override
	public int getSaplingPercentageDropChance() {
		return 85;
	}

	@Override
	public int getWoodPercentageDropChance() {
		return 75;
	}

	@Override
	public int getLogPercentageDropChance() {
		return 10;
	}

	@Override
	public int getTwigPercentageDropChance() {
		return 5;
	}*/

	@Override
	public int getPercentageDropChance() {
		return 80;
	}	
	
	@Override
	public int getMaxDurability() {
		return 320000;
	}
	@Override
	public String getRepairItemName() {
		return Localization.MODULES.TOOLS.DIAMONDS.translate();
	}
	@Override
	public int getRepairItemUnits(ItemStack item) {
		if (item != null && item.getItem() == Items.diamond) {
			return 160000;
		}
		return 0;
	}
	@Override
	public int getRepairSpeed() {
		return 150;
	}	
	
}
