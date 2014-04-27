package vswe.stevescarts.Modules.Addons.Plants;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.ICropModule;
import vswe.stevescarts.Modules.Addons.ModuleAddon;

public class ModuleNetherwart extends ModuleAddon implements ICropModule {

	public ModuleNetherwart(MinecartModular cart) {
		super(cart);
	}

	@Override
	public boolean isSeedValid(ItemStack seed) {
        return seed.getItem().itemID == Item.netherStalkSeeds.itemID;
	}	
	
	@Override
	public int getCropFromSeed(ItemStack seed) {
		return Block.netherStalk.blockID;
	}
	
	@Override
	public boolean isReadyToHarvest(int x, int y, int z) {
        int id = getCart().worldObj.getBlockId(x, y , z);
        int m = getCart().worldObj.getBlockMetadata(x, y, z);

		return id == Block.netherStalk.blockID && m == 3;
	}

}