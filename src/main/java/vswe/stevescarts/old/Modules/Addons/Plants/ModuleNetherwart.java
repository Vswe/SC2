package vswe.stevescarts.old.Modules.Addons.Plants;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.Modules.ICropModule;
import vswe.stevescarts.old.Modules.Addons.ModuleAddon;

public class ModuleNetherwart extends ModuleAddon implements ICropModule {

	public ModuleNetherwart(EntityModularCart cart) {
		super(cart);
	}

	@Override
	public boolean isSeedValid(ItemStack seed) {
        return seed.getItem() == Items.nether_wart;
    }
	
	@Override
	public Block getCropFromSeed(ItemStack seed) {
		return Blocks.nether_wart;
	}
	
	@Override
	public boolean isReadyToHarvest(int x, int y, int z) {
        Block b = getCart().worldObj.getBlock(x, y, z);
        int m = getCart().worldObj.getBlockMetadata(x, y, z);

		return b == Blocks.nether_wart && m == 3;
	}

}