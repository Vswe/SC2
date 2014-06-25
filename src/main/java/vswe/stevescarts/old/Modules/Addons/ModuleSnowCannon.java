package vswe.stevescarts.old.Modules.Addons;
import net.minecraft.init.Blocks;
import vswe.stevescarts.vehicles.entities.EntityModularCart;

public class ModuleSnowCannon extends ModuleAddon {
	public ModuleSnowCannon(EntityModularCart cart) {
		super(cart);
	}

	//called to update the module's actions. Called by the cart's update code.
	@Override
	public void update() {
		super.update();

		if (getCart().worldObj.isRemote) {
			return;
		}

		if (getCart().hasFuel()) {
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
		for (int x = -getBlocksOnSide(); x <= getBlocksOnSide(); x++) {
			for (int z = -getBlocksOnSide(); z <= getBlocksOnSide(); z++) {
				for (int y = -getBlocksFromLevel(); y <= getBlocksFromLevel(); y++) {
					int x1 = getCart().x() + x;
					int y1 = getCart().y() + y;
					int z1 = getCart().z() + z;
					if (countsAsAir(x1, y1, z1) && getCart().worldObj.getBiomeGenForCoords(x1, z1).getFloatTemperature(x1, y1, z1) <= 1.0F /* snow golems won't be hurt */ && Blocks.snow.canPlaceBlockAt(getCart().worldObj, x1, y1, z1))
					{
						getCart().worldObj.setBlock(x1, y1, z1, Blocks.snow);
					}
				}
			}
		}
	}

}