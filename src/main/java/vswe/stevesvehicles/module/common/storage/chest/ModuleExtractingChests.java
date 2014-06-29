package vswe.stevesvehicles.module.common.storage.chest;

import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleExtractingChests extends ModuleChest {
	public ModuleExtractingChests(VehicleBase vehicleBase) {
		super(vehicleBase);

	}

	@Override
	protected int getInventoryWidth() {
		return 18;
	}

	@Override
	protected int getInventoryHeight() {
		return 4;
	}

	@Override
	protected float chestFullyOpenAngle() {
		return (float)Math.PI / 2F;
	}
	@Override
	protected void handleChest() {
		if (isChestActive() && lidClosed() && chestOffset > END_OFFSET) {
			chestOffset -= 0.5F;
		}else if(!isChestActive() && lidClosed() && chestOffset < START_OFFSET) {
			chestOffset += 0.5F;
		}else{
			super.handleChest();
		}
	}

	public float getChestOffset() {
		return chestOffset;
	}

	private static final float START_OFFSET = -14F;
	private static final float END_OFFSET = -24.5F;
	private float chestOffset = START_OFFSET;

}