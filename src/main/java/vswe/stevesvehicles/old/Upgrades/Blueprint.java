package vswe.stevesvehicles.old.Upgrades;

import vswe.stevesvehicles.modules.data.ModuleDataItemHandler;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.TileEntities.TileEntityUpgrade;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.modules.data.ModuleData;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.Slot;
import vswe.stevesvehicles.old.Slots.SlotCart;

public class Blueprint extends SimpleInventoryEffect {


	public Blueprint() {
		super(1, 1);
	}
	
	@Override
	public Class<? extends Slot> getSlot(int i) {
		return SlotCart.class;
	}
	
	@Override
	public String getName() {
		return Localization.UPGRADES.BLUEPRINT.translate();
	}
	
	public boolean isValidForBluePrint(TileEntityUpgrade upgrade, ArrayList<ModuleData> modules, ModuleData module) {
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