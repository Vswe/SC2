package vswe.stevescarts.Upgrades;

import vswe.stevescarts.TileEntities.TileEntityUpgrade;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.ModuleData.ModuleData;
import java.util.ArrayList;
import net.minecraft.inventory.Slot;
import vswe.stevescarts.Slots.SlotCart;

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
		return "Enable the use of Blueprint carts.";
	}
	
	public boolean isValidForBluePrint(TileEntityUpgrade upgrade, ArrayList<ModuleData> modules, ModuleData module) {
		ItemStack blueprint = upgrade.getStackInSlot(0);
		if (blueprint != null) {
			NBTTagCompound info = blueprint.getTagCompound();
			if (info == null) {
				return false;
			}
			NBTTagByteArray moduleIDTag = (NBTTagByteArray)info.getTag("Modules");
			if (moduleIDTag == null) {
				return false;
			}
			byte[] IDs = moduleIDTag.byteArray;
			ArrayList<ModuleData> missing = new ArrayList<ModuleData>();
			for (byte id : IDs) {
				ModuleData blueprintModule = ModuleData.getList().get(id);
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