package vswe.stevesvehicles.old.Modules.Workers;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import vswe.stevesvehicles.vehicles.entities.EntityModularCart;
import vswe.stevesvehicles.old.Helpers.BlockCoord;
import vswe.stevesvehicles.old.Modules.Workers.Tools.ModuleDrill;
public class ModuleLiquidDrainer extends ModuleWorker {
	public ModuleLiquidDrainer(EntityModularCart cart) {
		super(cart);
	}

	//lower numbers are prioritized
	public byte getWorkPriority() {
		return (byte)0;
	}

	//return true when the work is done, false allow other modules to continue the work
	public boolean work() {
		return false;
	}
	
	public void handleLiquid(ModuleDrill drill, int x, int y, int z) {
		
		BlockCoord here = new BlockCoord(x,y,z);
		ArrayList<BlockCoord> checked = new ArrayList<BlockCoord>();
		int result = drainAt(drill, checked, here, 0);
		if (result > 0 && doPreWork()) {
			drill.kill();
			startWorking((int)(2.5F * result));
		}else{
			stopWorking();
		}
	}	
	

	@Override
	public boolean preventAutoShutdown() {
		return true;
	}
	
	private int drainAt(ModuleDrill drill, ArrayList<BlockCoord> checked, BlockCoord here, int buckets) {		
		int drained = 0;
		Block b = getCart().worldObj.getBlock(here.getX(), here.getY(), here.getZ());
		if (!isLiquid(b)) {
			return 0;
		}
		int meta = getCart().worldObj.getBlockMetadata(here.getX(), here.getY(), here.getZ());
		
		FluidStack liquid = getFluidStack(b, here.getX(), here.getY(), here.getZ(), !doPreWork());
		if (liquid != null) {
			if (doPreWork()) {
				liquid.amount += buckets * FluidContainerRegistry.BUCKET_VOLUME;
			}
			int amount = getCart().fill(liquid, false);
			if (amount == liquid.amount) {
				boolean isDrainable = meta == 0;
				if (!doPreWork()) {
					if (isDrainable) {
						getCart().fill(liquid, true);
					}
					getCart().worldObj.setBlockToAir(here.getX(), here.getY(), here.getZ());
				}
				drained += isDrainable ? 40 : 3;
				buckets += isDrainable ? 1 : 0;
			}
		}
		
		checked.add(here);


		if (checked.size() < 100 && here.getHorizontalDistToCartSquared(getCart()) < 200) {
			for (int y = 1; y >= 0; y--) {
				for (int x = -1; x <= 1; x++) {
					for (int z = -1; z <= 1; z++) {	
						if (Math.abs(x) + Math.abs(y) + Math.abs(z) == 1) {
	
							BlockCoord next = new BlockCoord(here.getX()+x,here.getY()+y,here.getZ()+z);
							if (!checked.contains(next)) {
								drained += drainAt(drill, checked, next, buckets);
							}
							
						}
					}
				}
			}
		}
		
		return drained;
	}		
	
	private boolean isLiquid(Block b) {

		boolean isWater = b == Blocks.water || b == Blocks.flowing_water || b == Blocks.ice;
		boolean isLava = b == Blocks.lava || b == Blocks.flowing_lava;
		boolean isOther = b != null && b instanceof IFluidBlock;
		return isWater || isLava || isOther;
	}
	
	private FluidStack getFluidStack(Block b, int x, int y, int z, boolean doDrain) {
		if (b == Blocks.water || b == Blocks.flowing_water) {
			return new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.BUCKET_VOLUME);
			
		}else if (b == Blocks.lava || b == Blocks.flowing_lava) {
			return new FluidStack(FluidRegistry.LAVA, FluidContainerRegistry.BUCKET_VOLUME);
			
		}else if (b instanceof IFluidBlock) {
			IFluidBlock liquid = (IFluidBlock)b;

			return liquid.drain(getCart().worldObj, x, y, z, doDrain);
		}else {
			return null;
		}
	}	
	

}