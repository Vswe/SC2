package vswe.stevescarts.Modules;
import net.minecraft.item.ItemStack;

public interface ICropModule {
	public boolean isSeedValid(ItemStack seed);
	public int getCropFromSeed(ItemStack seed);
	public boolean isReadyToHarvest(int x, int y, int z);		
}