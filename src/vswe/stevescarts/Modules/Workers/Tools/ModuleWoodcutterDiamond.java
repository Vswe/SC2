package vswe.stevescarts.Modules.Workers.Tools;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Items.ItemCartComponent;

public class ModuleWoodcutterDiamond extends ModuleWoodcutter {

	public ModuleWoodcutterDiamond(MinecartModular cart) {
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
		return "Diamonds";
	}
	@Override
	public int getRepairItemUnits(ItemStack item) {
		if (item != null && item.getItem() == Item.diamond) {
			return 160000;
		}
		return 0;
	}
	@Override
	public int getRepairSpeed() {
		return 150;
	}	
	
}
