package vswe.stevesvehicles.old.Modules;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public interface ITreeModule {
	public boolean isLeaves(Block b, int x, int y, int z);
	public boolean isWood(Block b, int x, int y, int z);
	public boolean isSapling(ItemStack sapling);	
}