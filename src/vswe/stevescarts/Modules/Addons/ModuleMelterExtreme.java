package vswe.stevescarts.Modules.Addons;
import net.minecraft.block.Block;
import vswe.stevescarts.Carts.MinecartModular;

public class ModuleMelterExtreme extends ModuleMelter {
	public ModuleMelterExtreme(MinecartModular cart) {
		super(cart);
	}

	@Override
	protected boolean melt(int id, int x, int y, int z) {
		if (!super.melt(id,x,y,z)) {
			if (id == Block.blockSnow.blockID) {
				getCart().worldObj.setBlockToAir(x,y,z);
				return true;
			}else if (id == Block.ice.blockID) {
				getCart().worldObj.setBlock(x,y,z, Block.waterStill.blockID);
				return true;
			}
		}

		return false;
	}
}