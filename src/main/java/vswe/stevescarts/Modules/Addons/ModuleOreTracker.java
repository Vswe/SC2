package vswe.stevescarts.Modules.Addons;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.BlockCoord;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrill;

public class ModuleOreTracker extends ModuleAddon {

	public ModuleOreTracker(MinecartModular cart) {
		super(cart);
	}

	public BlockCoord findBlockToMine(ModuleDrill drill, BlockCoord start) {
		return findBlockToMine(drill, new ArrayList<BlockCoord>(), start, true);
	}
	
	
	private BlockCoord findBlockToMine(ModuleDrill drill, ArrayList<BlockCoord> checked, BlockCoord current, boolean first) {
		if (current == null || checked.contains(current) || (!first && !isOre(current))) {
			return null;
		}
		checked.add(current);
		
		if (checked.size() < 200) {
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					for (int z = -1; z <= 1; z++) {
						if (Math.abs(x) + Math.abs(y) + Math.abs(z) == 1) {
							BlockCoord ret = findBlockToMine(drill, checked, new BlockCoord(current.getX() + x, current.getY() + y, current.getZ() + z), false);
							
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
	
	private boolean isOre(BlockCoord coord) {
        int id = getCart().worldObj.getBlockId(coord.getX(), coord.getY(), coord.getZ());
        Block b = Block.blocksList[id];		
        
        if (b != null) {
        	if (b instanceof BlockOre) {
        		return true;
	        }else{       	
	        	int oreId = OreDictionary.getOreID(new ItemStack(b));
	        	if (id == - 1) {
	        		return false;
	        	}else{
	        		String oreName = OreDictionary.getOreName(oreId);
	        		return oreName.startsWith("ore");
	        	}
	        }
        }

        return false;
	}
	
}
