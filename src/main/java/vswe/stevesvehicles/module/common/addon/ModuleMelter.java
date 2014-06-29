package vswe.stevesvehicles.module.common.addon;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleMelter extends ModuleAddon {
	public ModuleMelter(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	//called to update the module's actions. Called by the cart's update code.
	@Override
	public void update() {
		super.update();

		if (getVehicle().getWorld().isRemote) {
			return;
		}

		if (getVehicle().hasFuel()) {
			if (tick >= getInterval()) {
				tick = 0;
				melt();
			}else{
				tick++;
			}
		}
	}

	private int tick;
	protected int getInterval() {
		return 70;
	}

	protected int getBlocksOnSide() {
		return 7;
	}

	protected int getBlocksFromLevel() {
		return 1;
	}

	private void melt() {
		for (int x = -getBlocksOnSide(); x <= getBlocksOnSide(); x++) {
			for (int z = -getBlocksOnSide(); z <= getBlocksOnSide(); z++) {
				for (int y = -getBlocksFromLevel(); y <= getBlocksFromLevel(); y++) {
                    Block b = getVehicle().getWorld().getBlock(x + getVehicle().x(), y + getVehicle().y(), z + getVehicle().z());
					melt(b,x + getVehicle().x(), y + getVehicle().y(), z + getVehicle().z());
				}
			}
		}
	}

	protected boolean melt(Block b, int x, int y, int z) {
		if (b == Blocks.snow) {
			getVehicle().getWorld().setBlockToAir(x, y, z);
			return true;
		}

		return false;
	}
}