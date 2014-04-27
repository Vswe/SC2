package vswe.stevescarts.Modules.Workers;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRail;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Models.Cart.ModelCartbase;
import vswe.stevescarts.Models.Cart.ModelTrackRemover;

public class ModuleRemover extends ModuleWorker {
	public ModuleRemover(MinecartModular cart) {
		super(cart);
	}

	//lower numbers are prioritized
	@Override
	public byte getWorkPriority() {
		return 120;
	}

	protected boolean preventTurnback() {
		return true;
	}

	private int removeX;
    private int removeY = -1;
    private int removeZ;

	//return true when the work is done, false allow other modules to continue the work
	@Override
	public boolean work()
    {
        if (removeY != -1 && !(removeX == getCart().x() && removeZ == getCart().z()))
        {
            if (removeRail(removeX, removeY, removeZ, true))
            {
                return false;
            }
        }

        Vec3 next = getNextblock();
        Vec3 last = getLastblock();
        boolean front = isRailAtCoords(next);
        boolean back = isRailAtCoords(last);

        if (!front)
        {
            if (back)
            {
				 turnback();

                if (removeRail(getCart().x(), getCart().y(), getCart().z(), false)) {
					return true;
				}
            }else{
				//out of rails to remove
			}
        }
        else if (!back)
        {
            if (removeRail(getCart().x(), getCart().y(), getCart().z(), false)) {
				return true;
			}
        }

		return false;
    }

	private boolean isRailAtCoords(Vec3 coords) {
		int x = (int)coords.xCoord;
		int y = (int)coords.yCoord;
		int z = (int)coords.zCoord;
		return BlockRail.isRailBlockAt(getCart().worldObj, x, y + 1, z) || BlockRail.isRailBlockAt(getCart().worldObj, x, y, z) || BlockRail.isRailBlockAt(getCart().worldObj, x, y - 1, z);
	}

    private boolean removeRail(int x, int y, int z, boolean flag)
    {
        if (flag)
        {
            if (BlockRail.isRailBlockAt(getCart().worldObj, x, y, z) && getCart().worldObj.getBlockId(x, y, z) == 66)
            {
                if (!doPreWork())
                {
                    ItemStack iStack;
                    int rInt = getCart().rand.nextInt(100);

                    /*if (rInt <= 1)
                    {
                        iStack = new ItemStack(StevesCarts.component, 1, 14);
                    }
                    else if (rInt <= 5)
                    {
                        iStack = new ItemStack(StevesCarts.component, 1, 15);
                    }
                    else if (rInt <= 11)
                    {
                        iStack = new ItemStack(StevesCarts.component, 1, 16);
                    }
                    else
                    {*/
                        iStack = new ItemStack(Block.rail, 1, 0);
                    //}
								
                    getCart().addItemToChest(iStack);
					
                    if (iStack.stackSize == 0)
                    {
                        getCart().worldObj.setBlockToAir(x, y, z);
                    }

                    removeY = -1;
                }
                else
                {
                    startWorking(12);
                    return true;
                }
            }
            else
            {
                removeY = -1;
            }
        }
        else
        {
            if (BlockRail.isRailBlockAt(getCart().worldObj, x, y - 1, z))
            {
                removeX = x;
                removeY = y - 1;
                removeZ = z;
            }
            else if (BlockRail.isRailBlockAt(getCart().worldObj, x, y, z))
            {
                removeX = x;
                removeY = y;
                removeZ = z;
            }
            else if (BlockRail.isRailBlockAt(getCart().worldObj, x, y + 1, z))
            {
                removeX = x;
                removeY = y + 1;
                removeZ = z;
            }
        }

		stopWorking();
        return false;
    }


}