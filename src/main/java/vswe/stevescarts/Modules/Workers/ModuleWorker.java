package vswe.stevescarts.Modules.Workers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.ModuleBase;
public abstract class ModuleWorker extends ModuleBase {
	private boolean preWork;
	private boolean shouldDie;
	
	public ModuleWorker(MinecartModular cart) {
		super(cart);
		preWork = true;
	}

	//lower numbers are prioritized
	public abstract byte getWorkPriority();

	//return true when the work is done, false allow other modules to continue the work
	public abstract boolean work();

	protected void startWorking(int time) {
		getCart().setWorkingTime(time);
		preWork = false;
		getCart().setWorker(this);
	}

	public void stopWorking() {
		if (getCart().getWorker() == this) {
			preWork = true;
			getCart().setWorker(null);
		}
	}
	
	public boolean preventAutoShutdown() {
		return false;
	}
	
	public void kill() {
		shouldDie = true;
	}
	
	public boolean isDead() {
		return shouldDie;
	}
	
	public void revive() {
		shouldDie = false;
	}

	protected boolean doPreWork() {
		return preWork;
	}

    public Vec3 getLastblock()
    {
        return getNextblock(false);
    }
    public Vec3 getNextblock()
    {
        return getNextblock(true);
    }
    private Vec3 getNextblock(boolean flag)
    {
        //load the integer position of the cart
        int i = getCart().x();
        int j = getCart().y();
        int k = getCart().z();

        //if there's a rail block below the cart, decrease the j value since the cart should therefore be counted as being on that rail
        if (BlockRailBase.func_150049_b_(getCart().worldObj, i, j - 1, k))
        {
            j--;
        }

        //check if the cart actually is on a piece of rail
        Block b = getCart().worldObj.getBlock(i, j, k);
        if (BlockRailBase.func_150051_a(b))
        {
            //int meta = worldObj.getBlockMetadata(i, j, k);
            int meta = ((BlockRailBase)b).getBasicRailMetadata(getCart().worldObj, getCart(), i, j, k);

            //if the rail block is a slope we need to go up one level.
            if (meta >= 2 && meta <= 5)
            {
                j++;
            }

            //load the rail logic for the rail
            int logic[][] = MinecartModular.railDirectionCoordinates[meta];

            double pX = getCart().pushX;
            double pZ = getCart().pushZ;

            //check if the cart is moving in the same direction as the first direction as the rail goes
            boolean xDir = (pX > 0 && logic[0][0] > 0) || (pX == 0 || logic[0][0] == 0) || (pX < 0 && logic[0][0] < 0);
            boolean zDir = (pZ > 0 && logic[0][2] > 0) || (pZ == 0 || logic[0][2] == 0) || (pZ < 0 && logic[0][2] < 0);
            //if it is for both x and z value then the cart is moving along the first direction(index 0) otherwise it's moving along the second direction(index 1).
            int dir = ((xDir && zDir) == flag) ? 0 : 1;

            //return a vector with the coordinates of where the cart is heading
            return Vec3.createVectorHelper(
                    i + logic[dir][0],
                    j + logic[dir][1],
                    k + logic[dir][2]
                    );
        }
        else
        {
            //if the cart is not on a rail block its next block should be where it already is.
            return Vec3.createVectorHelper(i, j, k);
        }
    }

	@Override
	public float getMaxSpeed() {
		if (!doPreWork()) {
			return 0F;
		}else{
			return super.getMaxSpeed();
		}
	}



	//flag is false if it don't need a valid block to be built on(i.e assumes a bridge block will be there later)
    protected boolean isValidForTrack(int i, int j, int k, boolean flag)
    {
        boolean result = countsAsAir(i, j, k) && (!flag || World.doesBlockHaveSolidTopSurface(getCart().worldObj, i, j - 1, k));

		if (result) {
			int coordX = i - (getCart().x() - i);
			int coordY = j;
			int coordZ = k - (getCart().z() - k);
            Block block = getCart().worldObj.getBlock(coordX, coordY, coordZ);
			boolean isWater = block == Blocks.water || block == Blocks.flowing_water || block == Blocks.ice;
			boolean isLava = block == Blocks.lava || block == Blocks.flowing_lava;
			boolean isOther = block != null && block instanceof IFluidBlock;			
			boolean isLiquid = isWater || isLava || isOther;
			result = !isLiquid;
		}

        return result;
    }
}