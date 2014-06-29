package vswe.stevesvehicles.module.cart;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import vswe.stevesvehicles.module.cart.attachment.ModuleAttachment;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;

public abstract class ModuleWorker extends ModuleAttachment {
	private boolean preWork;
	private boolean shouldDie;
	
	public ModuleWorker(VehicleBase vehicleBase) {
		super(vehicleBase);
		preWork = true;
	}

	//lower numbers are prioritized
	public abstract byte getWorkPriority();

	//return true when the work is done, false allow other modules to continue the work
	public abstract boolean work();

	protected void startWorking(int time) {
		getVehicle().setWorkingTime(time);
		preWork = false;
		getVehicle().setWorker(this);
	}

	public void stopWorking() {
		if (getVehicle().getWorker() == this) {
			preWork = true;
			getVehicle().setWorker(null);
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

    protected EntityModularCart getModularCart() {
        return (EntityModularCart)getVehicle().getEntity();
    }

    public Vec3 getLastBlock(){
        return getNextBlock(false);
    }
    public Vec3 getNextBlock(){
        return getNextBlock(true);
    }
    private Vec3 getNextBlock(boolean flag) {
        //load the integer position of the cart
        int x = getVehicle().x();
        int y = getVehicle().y();
        int z = getVehicle().z();

        //if there's a rail block below the cart, decrease the j value since the cart should therefore be counted as being on that rail
        if (BlockRailBase.func_150049_b_(getVehicle().getWorld(), x, y - 1, z)){
            y--;
        }

        //check if the cart actually is on a piece of rail
        Block b = getVehicle().getWorld().getBlock(x, y, z);
        if (BlockRailBase.func_150051_a(b)) {

            //int meta = worldObj.getBlockMetadata(i, j, k);
            int meta = ((BlockRailBase)b).getBasicRailMetadata(getVehicle().getWorld(), getModularCart(), x, y, z);

            //if the rail block is a slope we need to go up one level.
            if (meta >= 2 && meta <= 5) {
                y++;
            }

            //load the rail logic for the rail
            int logic[][] = EntityModularCart.railDirectionCoordinates[meta];

            double pX = getModularCart().pushX;
            double pZ = getModularCart().pushZ;

            //check if the cart is moving in the same direction as the first direction as the rail goes
            boolean xDir = (pX > 0 && logic[0][0] > 0) || (pX == 0 || logic[0][0] == 0) || (pX < 0 && logic[0][0] < 0);
            boolean zDir = (pZ > 0 && logic[0][2] > 0) || (pZ == 0 || logic[0][2] == 0) || (pZ < 0 && logic[0][2] < 0);
            //if it is for both x and z value then the cart is moving along the first direction(index 0) otherwise it's moving along the second direction(index 1).
            int dir = ((xDir && zDir) == flag) ? 0 : 1;

            //return a vector with the coordinates of where the cart is heading
            return Vec3.createVectorHelper(
                    x + logic[dir][0],
                    y + logic[dir][1],
                    z + logic[dir][2]
                    );
        }else{
            //if the cart is not on a rail block its next block should be where it already is.
            return Vec3.createVectorHelper(x, y, z);
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
    protected boolean isValidForTrack(int x, int y, int z, boolean flag) {
        boolean result = countsAsAir(x, y, z) && (!flag || World.doesBlockHaveSolidTopSurface(getVehicle().getWorld(), x, y - 1, z));

		if (result) {
			int targetX = x - (getVehicle().x() - x);
			int targetY = y;
			int targetZ = z - (getVehicle().z() - z);
            Block block = getVehicle().getWorld().getBlock(targetX, targetY, targetZ);
			boolean isWater = block == Blocks.water || block == Blocks.flowing_water || block == Blocks.ice;
			boolean isLava = block == Blocks.lava || block == Blocks.flowing_lava;
			boolean isOther = block != null && block instanceof IFluidBlock;			
			boolean isLiquid = isWater || isLava || isOther;
			result = !isLiquid;
		}

        return result;
    }
}