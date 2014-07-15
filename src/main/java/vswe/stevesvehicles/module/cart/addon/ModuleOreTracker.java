package vswe.stevesvehicles.module.cart.addon;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vswe.stevesvehicles.module.common.addon.ModuleAddon;
import vswe.stevesvehicles.block.BlockCoordinate;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.module.cart.tool.ModuleDrill;

public class ModuleOreTracker extends ModuleAddon {

	public ModuleOreTracker(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	public BlockCoordinate findBlockToMine(ModuleDrill drill, BlockCoordinate start) {
		return findBlockToMine(drill, new ArrayList<BlockCoordinate>(), start, true);
	}
	
	
	private BlockCoordinate findBlockToMine(ModuleDrill drill, ArrayList<BlockCoordinate> checked, BlockCoordinate current, boolean first) {
		if (current == null || checked.contains(current) || (!first && !isOre(current))) {
			return null;
		}
		checked.add(current);
		
		if (checked.size() < 200) {
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					for (int z = -1; z <= 1; z++) {
						if (Math.abs(x) + Math.abs(y) + Math.abs(z) == 1) {
							BlockCoordinate ret = findBlockToMine(drill, checked, new BlockCoordinate(current.getX() + x, current.getY() + y, current.getZ() + z), false);
							
							if (ret != null) {
								return ret;
							}
						}
					}				
				}			
			}
		}
		
		if (first && !isOre(current)) {
			return null;
		}
		
		if (drill.isValidBlock(current.getX(), current.getY(), current.getZ(), 0, 1, true) == null) {
			return null;
		}
		
		return current;
	}
	
	private boolean isOre(BlockCoordinate coordinate) {
        Block b = getVehicle().getWorld().getBlock(coordinate.getX(), coordinate.getY(), coordinate.getZ());
        
        if (b != null) {
        	if (b instanceof BlockOre) {
        		return true;
	        }else{       	
	        	int oreId = OreDictionary.getOreID(new ItemStack(b));
	        	if (oreId == - 1) {
	        		return false;
	        	}else{
	        		String oreName = OreDictionary.getOreName(oreId);
	        		return oreName.toLowerCase().startsWith("ore");
	        	}
	        }
        }

        return false;
	}
	
}
