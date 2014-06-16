package vswe.stevescarts.Modules.Workers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockRailBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ISuppliesModule;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotBridge;

public class ModuleBridge extends ModuleWorker implements ISuppliesModule {
	public ModuleBridge(MinecartModular cart) {
		super(cart);
	}

	@Override
	public boolean hasGui(){
		return true;
	}

	@Override
	protected SlotBase getSlot(int slotId, int x, int y) {
		return new SlotBridge(getCart(),slotId,8+x*18,23+y*18);
	}
	@Override
	public void drawForeground(GuiMinecart gui) {
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
        Vec3 next = getNextblock();
        //save the next block's coordinates for easy access
        int x = (int) next.xCoord;
        int y = (int) next.yCoord;
        int z = (int) next.zCoord;
        int yLocation;

        if (getCart().getYTarget() > y)
        {
            yLocation = y;
        }
        else if (getCart().getYTarget() < y)
        {
            yLocation = y - 2;
        }
        else
        {
            yLocation = y - 1;
        }

        if (!BlockRailBase.func_150049_b_(getCart().worldObj, x, y, z) && !BlockRailBase.func_150049_b_(getCart().worldObj, x, y - 1, z))
        {
            if (doPreWork())
            {
                if (tryBuildBridge(x, yLocation, z, false))
                {
                    startWorking(22);
					setBridge(true);
                    return true;
                }
            }
            else
            {
                if (tryBuildBridge(x, yLocation, z, true))
                {
                   stopWorking();
                }
            }
        }

		setBridge(false);
        return false;
	}

    private boolean tryBuildBridge(int i, int j, int k, boolean flag)
    {
        Block b = getCart().worldObj.getBlock(i, j, k);

        if ((countsAsAir(i, j, k) || b instanceof BlockLiquid) && isValidForTrack(i, j + 1, k, false))
        {
			//OPTIONAL: add code that makes the cart turn around if it can't place any tracks afterwards

            for (int m = 0; m < getInventorySize(); m++)
            {
                if (getStack(m) != null)
                {
                    if (SlotBridge.isBridgeMaterial(getStack(m)))
                    {
                        if (flag)
                        {
							//EntityPlayer play = ModLoader.getMinecraftInstance().thePlayer;
							//getStack(m).getItem().tryPlaceIntoWorld(getStack(m), play, getCart().worldObj, i, j, k, -1,0F,0F,0F);
							getCart().worldObj.setBlock(i,j,k, Block.getBlockFromItem(getStack(m).getItem()), ((ItemBlock)(getStack(m).getItem())).getMetadata(getStack(m).getItemDamage()), 3);
							
							if (!getCart().hasCreativeSupplies()) {
								getStack(m).stackSize--;
	                            if (getStack(m).stackSize == 0)
	                            {
	                                setStack(m,null);
	                            }
	
								getCart().markDirty();
							}
                        }

                        return true;
                    }
                }
            }

			if (!isValidForTrack(i,j,k,true) && !isValidForTrack(i,j+1,k,true) && !isValidForTrack(i,j+2,k,true)) {
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