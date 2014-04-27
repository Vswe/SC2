package vswe.stevescarts.Modules.Addons;
import net.minecraft.block.Block;
import vswe.stevescarts.Carts.MinecartModular;

public class ModuleMelter extends ModuleAddon {
	public ModuleMelter(MinecartModular cart) {
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
					int id = getCart().worldObj.getBlockId(x + getCart().x(), y + getCart().y(), z + getCart().z());
					melt(id,x + getCart().x(), y + getCart().y(), z + getCart().z());
				}
			}
		}
	}

	protected boolean melt(int id, int x, int y, int z) {
		if (id == Block.snow.blockID) {
			getCart().worldObj.setBlockToAir(x,y,z);
			return true;
		}

		return false;
	}
}