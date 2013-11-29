package vswe.stevescarts.Helpers;

import net.minecraft.item.ItemStack;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Items.ItemCartComponent;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class WoodFuelHandler implements IFuelHandler {

	
	public WoodFuelHandler() {
		GameRegistry.registerFuelHandler(this);
	}

	@Override
	public int getBurnTime(ItemStack fuel) {
		if (fuel != null && fuel.getItem() != null && fuel.getItem() == StevesCarts.component) {
			if (ItemCartComponent.isWoodLog(fuel)) {
				return 150;
			}else if(ItemCartComponent.isWoodTwig(fuel)) {
				return 50;
			}
		}
		
		return 0;
	}
	
	
	
}
