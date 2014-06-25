package vswe.stevescarts.old.Modules.Addons.Plants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.Modules.ITreeModule;
import vswe.stevescarts.old.Modules.Addons.ModuleAddon;

public class ModuleModTrees extends ModuleAddon implements ITreeModule {

	public ModuleModTrees(EntityModularCart cart) {
		super(cart);
	}

	public boolean isLeaves(Block b, int x, int y, int z) {
		return b.isLeaves(getCart().worldObj, x, y, z);
	}
	
	public boolean isWood(Block b, int x, int y, int z) {
		return b.isWood(getCart().worldObj, x, y, z);
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