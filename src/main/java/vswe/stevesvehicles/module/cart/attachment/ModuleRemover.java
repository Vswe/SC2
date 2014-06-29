package vswe.stevesvehicles.module.cart.attachment;

import net.minecraft.block.BlockRailBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import vswe.stevesvehicles.module.cart.ModuleWorker;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleRemover extends ModuleWorker {
	public ModuleRemover(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	//lower numbers are prioritized
	@Override
	public byte getWorkPriority() {
		return 120;
	}

	protected boolean preventTurnBack() {
		return true;
	}

	private int removeX;
    private int removeY = -1;
    private int removeZ;

	//return true when the work is done, false allow other modules to continue the work
	@Override
	public boolean work() {
        if (removeY != -1 && !(removeX == getVehicle().x() && removeZ == getVehicle().z())) {
            if (removeRail(removeX, removeY, removeZ, true)) {
                return false;
            }
        }

        Vec3 next = getNextBlock();
        Vec3 last = getLastBlock();
        boolean front = isRailAtLocation(next);
        boolean back = isRailAtLocation(last);

        if (!front) {
            if (back){
				 turnback();

                if (removeRail(getVehicle().x(), getVehicle().y(), getVehicle().z(), false)) {
					return true;
				}
            }else{
				//out of rails to remove
			}
        }else if (!back){
            if (removeRail(getVehicle().x(), getVehicle().y(), getVehicle().z(), false)) {
				return true;
			}
        }

		return false;
    }

	private boolean isRailAtLocation(Vec3 coordinates) {
		int x = (int)coordinates.xCoord;
		int y = (int)coordinates.yCoord;
		int z = (int)coordinates.zCoord;
		return BlockRailBase.func_150049_b_(getVehicle().getWorld(), x, y + 1, z) || BlockRailBase.func_150049_b_(getVehicle().getWorld(), x, y, z) || BlockRailBase.func_150049_b_(getVehicle().getWorld(), x, y - 1, z);
	}

    private boolean removeRail(int x, int y, int z, boolean flag) {
        if (flag) {
            if (BlockRailBase.func_150049_b_(getVehicle().getWorld(), x, y, z) && getVehicle().getWorld().getBlock(x, y, z) == Blocks.rail) {
                if (!doPreWork()) {
                    ItemStack item = new ItemStack(Blocks.rail, 1);

                    getVehicle().addItemToChest(item);
					
                    if (item.stackSize == 0) {
                        getVehicle().getWorld().setBlockToAir(x, y, z);
                    }

                    removeY = -1;
                }else{
                    startWorking(12);
                    return true;
                }
            }else{
                removeY = -1;
            }
        }else {
            if (BlockRailBase.func_150049_b_(getVehicle().getWorld(), x, y - 1, z)) {
                removeX = x;
                removeY = y - 1;
                removeZ = z;
            }else if (BlockRailBase.func_150049_b_(getVehicle().getWorld(), x, y, z)) {
                removeX = x;
                removeY = y;
                removeZ = z;
            }else if (BlockRailBase.func_150049_b_(getVehicle().getWorld(), x, y + 1, z)) {
                removeX = x;
                removeY = y + 1;
                removeZ = z;
            }
        }

		stopWorking();
        return false;
    }


}