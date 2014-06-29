package vswe.stevesvehicles.module.common.addon;
import net.minecraft.init.Blocks;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleSnowCannon extends ModuleAddon {
	public ModuleSnowCannon(VehicleBase vehicleBase) {
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
				generateSnow();
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

	private void generateSnow() {
		for (int offsetX = -getBlocksOnSide(); offsetX <= getBlocksOnSide(); offsetX++) {
			for (int offsetZ = -getBlocksOnSide(); offsetZ <= getBlocksOnSide(); offsetZ++) {
				for (int offsetY = -getBlocksFromLevel(); offsetY <= getBlocksFromLevel(); offsetY++) {
					int targetX = getVehicle().x() + offsetX;
					int targetY = getVehicle().y() + offsetY;
					int targetZ = getVehicle().z() + offsetZ;
					if (countsAsAir(targetX, targetY, targetZ) && getVehicle().getWorld().getBiomeGenForCoords(targetX, targetZ).getFloatTemperature(targetX, targetY, targetZ) <= 1.0F /* snow golems won't be hurt */ && Blocks.snow.canPlaceBlockAt(getVehicle().getWorld(), targetX, targetY, targetZ)) {
						getVehicle().getWorld().setBlock(targetX, targetY, targetZ, Blocks.snow);
					}
				}
			}
		}
	}

}