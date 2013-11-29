package vswe.stevescarts.Modules.Workers.Tools;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Items.Items;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Items.ItemCartComponent;

public class ModuleWoodcutterHardened extends ModuleWoodcutter {

	public ModuleWoodcutterHardened(MinecartModular cart) {
		super(cart);
	}

	/*@Override
	public int getApplePercentageDropChance() {
		return 100;
	}

	@Override
	public int getSaplingPercentageDropChance() {
		return 100;
	}

	@Override
	public int getWoodPercentageDropChance() {
		return 90;
	}

	@Override
	public int getLogPercentageDropChance() {
		return 5;
	}

	@Override
	public int getTwigPercentageDropChance() {
		return 0;
	}*/
	
	@Override
	public int getPercentageDropChance() {
		return 100;
	}		

	@Override
	public int getMaxDurability() {
		return 640000;
	}
	
	@Override
	public String getRepairItemName() {
		return "Reinforced Metal";
	}
	
	@Override
	public int getRepairItemUnits(ItemStack item) {
		if (item != null && item.getItem() == Items.component && item.getItemDamage() == 22) {
			return 320000;
		}
		return 0;
	}
	@Override
	public int getRepairSpeed() {
		return 400;
	}		
	
}
