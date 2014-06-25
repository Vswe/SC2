package vswe.stevescarts.old.Modules;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public interface ICropModule {
	public boolean isSeedValid(ItemStack seed);
	public Block getCropFromSeed(ItemStack seed);
	public boolean isReadyToHarvest(int x, int y, int z);		
}