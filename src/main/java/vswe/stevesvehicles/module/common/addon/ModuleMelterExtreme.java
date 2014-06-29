package vswe.stevesvehicles.module.common.addon;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleMelterExtreme extends ModuleMelter {
	public ModuleMelterExtreme(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	@Override
	protected boolean melt(Block b, int x, int y, int z) {
		if (!super.melt(b,x,y,z)) {
			if (b == Blocks.snow) {
				getVehicle().getWorld().setBlockToAir(x, y, z);
				return true;
			}else if (b == Blocks.ice) {
				getVehicle().getWorld().setBlock(x, y, z, Blocks.water);
				return true;
			}
		}

		return false;
	}
}