package vswe.stevesvehicles.module.cart.addon.cultivation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Modules.ITreeModule;
import vswe.stevesvehicles.module.common.addon.ModuleAddon;

public class ModuleModTrees extends ModuleAddon implements ITreeModule {

	public ModuleModTrees(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	public boolean isLeaves(Block b, int x, int y, int z) {
		return b.isLeaves(getVehicle().getWorld(), x, y, z);
	}
	
	public boolean isWood(Block b, int x, int y, int z) {
		return b.isWood(getVehicle().getWorld(), x, y, z);
	}
	public boolean isSapling(ItemStack sapling) {
		if (sapling != null /*&& sapling.getItem() instanceof ItemBlock*/) {
			
			if (isStackSapling(sapling)) {
				return true;
			}else if (sapling.getItem() instanceof ItemBlock){
				Block b = Block.getBlockFromItem(sapling.getItem());
				
				if (b instanceof BlockSapling) {
					return true;
				}
				
				return b != null && isStackSapling(new ItemStack(b, 1, OreDictionary.WILDCARD_VALUE));
			}
		}
			
		return false;
		
	}	
	
	private boolean isStackSapling(ItemStack sapling) {
		int id = OreDictionary.getOreID(sapling);
		String name = OreDictionary.getOreName(id);
		return name != null && name.startsWith("treeSapling");		
	}
	
	
}