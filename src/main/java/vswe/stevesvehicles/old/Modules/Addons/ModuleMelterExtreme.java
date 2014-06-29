package vswe.stevesvehicles.old.Modules.Addons;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;

public class ModuleMelterExtreme extends ModuleMelter {
	public ModuleMelterExtreme(EntityModularCart cart) {
		super(cart);
	}

	@Override
	protected boolean melt(Block b, int x, int y, int z) {
		if (!super.melt(b,x,y,z)) {
			if (b == Blocks.snow) {
				getCart().worldObj.setBlockToAir(x, y, z);
				return true;
			}else if (b == Blocks.ice) {
				getCart().worldObj.setBlock(x, y, z, Blocks.water);
				return true;
			}
		}

		return false;
	}
}