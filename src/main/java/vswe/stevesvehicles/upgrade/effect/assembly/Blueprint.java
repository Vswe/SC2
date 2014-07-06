package vswe.stevesvehicles.upgrade.effect.assembly;

import vswe.stevesvehicles.module.data.ModuleDataItemHandler;
import vswe.stevesvehicles.old.TileEntities.TileEntityUpgrade;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.module.data.ModuleData;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.Slot;
import vswe.stevesvehicles.container.slots.SlotCart;
import vswe.stevesvehicles.upgrade.effect.util.SimpleInventoryEffect;

public class Blueprint extends SimpleInventoryEffect {

	public Blueprint(TileEntityUpgrade upgrade) {
		super(upgrade, 1, 1);
	}
	
	@Override
	public Class<? extends Slot> getSlot(int i) {
		return SlotCart.class;
	}

	public boolean isValidForBluePrint(ArrayList<ModuleData> modules, ModuleData module) {
		ItemStack blueprint = upgrade.getStackInSlot(0);
		if (blueprint != null) {
            List<ModuleData> blueprintModules = ModuleDataItemHandler.getModulesFromItem(blueprint);

			if (blueprintModules == null) {
				return false;
			}

			ArrayList<ModuleData> missing = new ArrayList<ModuleData>();

            for (ModuleData blueprintModule : blueprintModules) {
                int index = modules.indexOf(blueprintModule);
                if (index != -1) {
                    modules.remove(index);
                }else{
                    missing.add(blueprintModule);
                }
            }

			return missing.contains(module);
		}else{
			//depends on setting, will return false for now
			return false;
		}
	}
	
}