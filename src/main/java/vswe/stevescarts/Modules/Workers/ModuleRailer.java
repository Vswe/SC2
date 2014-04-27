package vswe.stevescarts.Modules.Workers;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ISuppliesModule;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotBuilder;

public class ModuleRailer extends ModuleWorker implements ISuppliesModule {
	public ModuleRailer(MinecartModular cart) {
		super(cart);
	}

	@Override
	public boolean hasGui(){
		return true;
	}

	@Override
	protected SlotBase getSlot(int slotId, int x, int y) {
		return new SlotBuilder(getCart(),slotId,8+x*18,23+y*18);
	}
	@Override
	public void drawForeground(GuiMinecart gui) {
	    drawString(gui, Localization.MODULES.ATTACHMENTS.RAILER.translate(), 8, 6, 0x404040);
	}

	//lower numbers are prioritized
	@Override
	public byte getWorkPriority() {
		return 100;
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

		ArrayList<Integer[]> pos = getValidRailPositions(x, y, z);
		
        //if this cart hasn't started working
        if (doPreWork())
        {
            //check if it's possible to place a rail, if so start the working delay
			boolean valid = false;
			for (int i = 0; i < pos.size(); i++) {
				if (tryPlaceTrack(pos.get(i)[0], pos.get(i)[1], pos.get(i)[2], false)) {
					valid = true;
					break;
				}
			}
			
            if (valid)
            {
				startWorking(12);
            }else {
				boolean front = false;
				for (int i = 0; i < pos.size(); i++) {
					
					if (BlockRailBase.isRailBlockAt(getCart().worldObj, pos.get(i)[0], pos.get(i)[1], pos.get(i)[2])) {
						front = true;
						break;
					}
				}	
				if(!front) {
					turnback();
				}
			}
			return true;
        }
        else
        {
            //if the cart is working it's now time for it to place its rail, try to find a spot for it
			stopWorking();

			for (int i = 0; i < pos.size(); i++) {
				if (tryPlaceTrack(pos.get(i)[0], pos.get(i)[1], pos.get(i)[2], true)) {
					break;
				}
			}			

			return false;
        }
	}
	
	protected ArrayList<Integer[]> getValidRailPositions(int x, int y, int z) {
		ArrayList<Integer[]> lst = new ArrayList<Integer[]>();
		
		/*if (!BlockRail.isRailBlockAt(getCart().worldObj, x, y, z)) {
			lst.add(new Integer[] {x + 1, y , z});
		}*/
		
		if (y >= getCart().y()) {
			lst.add(new Integer[] {x, y +1, z});
		}
		lst.add(new Integer[] {x, y , z});
		lst.add(new Integer[] {x, y -1, z});

		return lst;
	}

	protected boolean validRail(int id) {
		return id == Block.rail.blockID;
	}

    /**
     Code for placing a rail block, this method is used by placeTrack()

     the flag parameter is telling if it should build anything, if false it will only test the posibillities.
     **/
    private boolean tryPlaceTrack(int i, int j, int k, boolean flag)
    {
        //test if this block is free to use
        if (isValidForTrack(i, j, k, true))
        {
            //loop through the slots to search for rails
            for (int l = 0; l < getInventorySize(); l++)
            {
                //check if it has found a standard rail block
                if (getStack(l) != null)
                {
                    if (validRail(getStack(l).getItem().itemID))
                    {
                        //if so this is a valid action to do, if the flag parameter is true this action should also be done
                        if (flag)
                        {
                            //place the rail
                            getCart().worldObj.setBlock(i, j, k, getStack(l).getItem().itemID);
                            
							if (!getCart().hasCreativeSupplies()) {
	                            //remove the placed rail from the cart's inventory
	                            getStack(l).stackSize--;
	
	                            //remove the stack if it's empty
	                            if (getStack(l).stackSize == 0)
	                            {
	                                setStack(l,null);
	                            }
	
								getCart().onInventoryChanged();
							}
                        }

                        //return true to tell this went allright and that no other blocks should be checked
                        return true;
                    }
                }
            }

            //if there's no rails left, return true so it won't test other blocks(there won't be any rails no matter which block it tries to build on). Also tell the cart to turn back.
            turnback();
            return true;
        }

        //if the block wasn't free to use return false so BuildTrack() will try the next block(if any)
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
	@Override
	public void onInventoryChanged() {
		super.onInventoryChanged();

		calculateRails();
	}

	private void calculateRails() {
		if (getCart().worldObj.isRemote) {
			return;
		}
	
		byte valid = 0;
		//loop through the slots to search for rails
		for (int i = 0; i < getInventorySize(); i++)
		{
			//check if it has found a standard rail block
			if (getStack(i) != null)
			{
				if (validRail(getStack(i).getItem().itemID))
				{
					valid++;
				}
			}
		}

		updateDw(0,valid);
	}

	public int getRails() {
		if (isPlaceholder()) {
			return getSimInfo().getRailCount();
		}else{
			return getDw(0);
		}
	}
	
	private boolean hasGeneratedAngles = false;
	private float [] railAngles;
	public float getRailAngle(int i) {
		if (!hasGeneratedAngles) {
			railAngles = new float[getInventorySize()];
			for (int j = 0; j < getInventorySize(); j++) {
				railAngles[j] = (getCart().rand.nextFloat() / 2) - 0.25F;			
			}
			hasGeneratedAngles = true;
		}
	
		return railAngles[i];
	}
	
/*	@Override
	public void getModels(HashMap<String,modMCBase> models) {
		for (baseModule module : getCart().getModules()) {
			ModuleData data = ModuleData.getList().get(module.getModuleId());
			if (data != null && data.getRenderingSides() != null) {
				if (data.getRenderingSides().contains(ModuleData.SIDE.TOP)) {
					return;
				}
			}
		}
	

	}*/
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		calculateRails();
	}	
	
	
	@Override
	public boolean haveSupplies() {
		for (int i = 0; i < getInventorySize(); i++) {
			ItemStack item = getStack(i);
			if (item != null && validRail(item.getItem().itemID)) {
				return true;
			}
		}
		return false;
	}

}