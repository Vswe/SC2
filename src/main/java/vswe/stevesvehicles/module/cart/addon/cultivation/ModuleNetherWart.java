package vswe.stevesvehicles.module.cart.addon.cultivation;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.module.cart.ICropModule;
import vswe.stevesvehicles.module.common.addon.ModuleAddon;

public class ModuleNetherWart extends ModuleAddon implements ICropModule {

	public ModuleNetherWart(VehicleBase vehicleBase) {
		super(vehicleBase);
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
        Block b = getVehicle().getWorld().getBlock(x, y, z);
        int m = getVehicle().getWorld().getBlockMetadata(x, y, z);

		return b == Blocks.nether_wart && m == 3;
	}

}