package vswe.stevesvehicles.module.cart.attachment;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockRailBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.module.cart.ModuleWorker;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.module.ISuppliesModule;
import vswe.stevesvehicles.container.slots.SlotBase;
import vswe.stevesvehicles.container.slots.SlotBridge;

public class ModuleBridge extends ModuleWorker implements ISuppliesModule {
	public ModuleBridge(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	@Override
	public boolean hasGui(){
		return true;
	}

	@Override
	public int guiWidth() {
		return 80;
	}

	@Override
	protected SlotBase getSlot(int slotId, int x, int y) {
		return new SlotBridge(getVehicle().getVehicleEntity(), slotId, 8 + x * 18, 23 + y * 18);
	}
	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, getModuleName(), 8, 6, 0x404040);
	}

	//lower numbers are prioritized
	@Override
	public byte getWorkPriority() {
		return 98;
	}

	//return true when the work is done, false allow other modules to continue the work
	@Override
	public boolean work() {
        //get the next block
        Vec3 next = getNextBlock();
        //save the next block's coordinates for easy access
        int x = (int) next.xCoord;
        int y = (int) next.yCoord;
        int z = (int) next.zCoord;
        int yLocation;

        if (getModularCart().getYTarget() > y) {
            yLocation = y;
        }else if (getModularCart().getYTarget() < y) {
            yLocation = y - 2;
        }else {
            yLocation = y - 1;
        }

        if (!BlockRailBase.func_150049_b_(getVehicle().getWorld(), x, y, z) && !BlockRailBase.func_150049_b_(getVehicle().getWorld(), x, y - 1, z)) {
            if (doPreWork()) {
                if (tryBuildBridge(x, yLocation, z, false)) {
                    startWorking(22);
					setBridge(true);
                    return true;
                }
            }else {
                if (tryBuildBridge(x, yLocation, z, true)) {
                   stopWorking();
                }
            }
        }

		setBridge(false);
        return false;
	}

    private boolean tryBuildBridge(int x, int y, int z, boolean flag) {
        Block b = getVehicle().getWorld().getBlock(x, y, z);

        if ((countsAsAir(x, y, z) || b instanceof BlockLiquid) && isValidForTrack(x, y + 1, z, false)) {

            for (int m = 0; m < getInventorySize(); m++) {
                if (getStack(m) != null) {

                    if (SlotBridge.isBridgeMaterial(getStack(m))) {
                        if (flag) {

							getVehicle().getWorld().setBlock(x, y, z, Block.getBlockFromItem(getStack(m).getItem()), ((ItemBlock) (getStack(m).getItem())).getMetadata(getStack(m).getItemDamage()), 3);
							
							if (!getVehicle().hasCreativeSupplies()) {
								getStack(m).stackSize--;
	                            if (getStack(m).stackSize == 0)
	                            {
	                                setStack(m,null);
	                            }
	
								getVehicle().getVehicleEntity().markDirty();
							}
                        }

                        return true;
                    }
                }
            }

			if (!isValidForTrack(x,y,z,true) && !isValidForTrack(x,y+1,z,true) && !isValidForTrack(x,y+2,z,true)) {
				//turnback();
			}
        }

        return false;
    }

	@Override
	public void initDw() {
		addDw(0,0);
	}
	@Override
	public int numberOfDataWatchers() {
		return 1;
	}

	private void setBridge(boolean val) {
		updateDw(0,val ? 1 : 0);
	}

	public boolean needBridge() {
		if (isPlaceholder()) {
			return getSimInfo().getNeedBridge();
		}else{
			return getDw(0) != 0;
		}
	}
	
	@Override
	public boolean haveSupplies() {
		for (int i = 0; i < getInventorySize(); i++) {
			ItemStack item = getStack(i);
			if (item != null && SlotBridge.isBridgeMaterial(item)) {
				return true;
			}
		}
		return false;
	}	
}