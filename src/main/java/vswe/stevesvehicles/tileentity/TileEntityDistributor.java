package vswe.stevesvehicles.tileentity;
import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.FluidTank;
import vswe.stevesvehicles.localization.entry.block.LocalizationDistributor;
import vswe.stevesvehicles.network.DataReader;
import vswe.stevesvehicles.network.PacketHandler;
import vswe.stevesvehicles.container.ContainerBase;
import vswe.stevesvehicles.container.ContainerDistributor;
import vswe.stevesvehicles.old.Helpers.DistributorSetting;
import vswe.stevesvehicles.old.Helpers.DistributorSide;
import vswe.stevesvehicles.old.Helpers.Tank;
import vswe.stevesvehicles.client.gui.screen.GuiBase;
import vswe.stevesvehicles.client.gui.screen.GuiDistributor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
public class TileEntityDistributor extends TileEntityBase
    implements IInventory, ISidedInventory, IFluidHandler {

	@SideOnly(Side.CLIENT)
	@Override
	public GuiBase getGui(InventoryPlayer inv) {
		return new GuiDistributor(this);
	}
	
	@Override
	public ContainerBase getContainer(InventoryPlayer inv) {
		return new ContainerDistributor(this);
	}
	
	private ArrayList<DistributorSide> sides;

	public ArrayList<DistributorSide> getSides() {
		return sides;
	}
	
    public TileEntityDistributor()
    {
		sides = new ArrayList<DistributorSide>();
		sides.add(new DistributorSide(0, LocalizationDistributor.ORANGE, ForgeDirection.UP));
		sides.add(new DistributorSide(1, LocalizationDistributor.PURPLE, ForgeDirection.DOWN));
		sides.add(new DistributorSide(2, LocalizationDistributor.YELLOW, ForgeDirection.NORTH));
		sides.add(new DistributorSide(3, LocalizationDistributor.GREEN, ForgeDirection.WEST));
		sides.add(new DistributorSide(4, LocalizationDistributor.BLUE, ForgeDirection.SOUTH));
		sides.add(new DistributorSide(5, LocalizationDistributor.RED, ForgeDirection.EAST));
    }


	@Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);

		for (DistributorSide side : getSides()) {
			side.setData(nbttagcompound.getInteger("Side" + side.getId()));
		}
    }

	@Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
		
		for (DistributorSide side : getSides()) {
			nbttagcompound.setInteger("Side" + side.getId(), side.getData());
		}
    }


	private boolean dirty = true;

	@Override
    public void updateEntity() {
		dirty = true;
    }


	@Override
	public void receivePacket(DataReader dr, EntityPlayer player) {
        int settingId = dr.readByte();
        int sideId = dr.readByte();
        if (settingId >= 0 && settingId < DistributorSetting.settings.size() && sideId >= 0 && sideId < getSides().size()) {
            if (dr.readBoolean()) {
                getSides().get(sideId).set(settingId);
            }else{
                getSides().get(sideId).reset(settingId);
            }
        }
	}
	

	
	@Override
	public void initGuiData(Container con, ICrafting crafting) {


	}
	@Override
	public void checkGuiData(Container con, ICrafting crafting) {
		ContainerDistributor distributor = (ContainerDistributor)con;
		for (int i = 0; i < getSides().size(); i++) {
			DistributorSide side  = getSides().get(i);
			
			if (side.getLowShortData() != distributor.cachedValues.get(i * 2)) {
				updateGuiData(con, crafting, i * 2, side.getLowShortData());
				distributor.cachedValues.set(i * 2, side.getLowShortData());
			}
			if (side.getHighShortData() != distributor.cachedValues.get(i * 2 + 1)) {
				updateGuiData(con, crafting, i * 2 + 1, side.getHighShortData());
				distributor.cachedValues.set(i * 2 + 1, side.getHighShortData());
			}			
		}
	}
	@Override
	public void receiveGuiData(int id, short data) {	

		int sideId = id / 2;
		boolean isHigh = id % 2 == 1;
		
		DistributorSide side  = getSides().get(sideId);
		if (isHigh) {
			side.setHighShortData(data);
		}else{
			side.setLowShortData(data);			
		}
		
	}
	

	private TileEntityManager[] inventories;
	public boolean hasTop;
	public boolean hasBot;
	public TileEntityManager[] getInventories() {
		if (dirty) {
			generateInventories();
			dirty = false;
		}
	
		return inventories;
	}
	
	private void generateInventories() {	
		TileEntityManager bot = generateManager(-1);
		TileEntityManager top = generateManager(+1);
		 
		hasTop = top != null;
		hasBot = bot != null;
		
		inventories = populateManagers(top, bot, hasTop, hasBot);
	}
	
	private TileEntityManager[] populateManagers(TileEntityManager topElement, TileEntityManager botElement, boolean hasTopElement, boolean hasBotElement) {
		if (!hasTopElement && !hasBotElement) {
			return new TileEntityManager[] {};
		}else if(!hasBotElement) {
			return new TileEntityManager[] {topElement};
		}else if(!hasTopElement) {
			return new TileEntityManager[] {botElement};
		}else{
			return new TileEntityManager[] {botElement, topElement};	
		}	
	}
	
	private TileEntityManager generateManager(int y) {
		TileEntity TE = worldObj.getTileEntity(xCoord, yCoord + y, zCoord);
		if (TE != null && TE instanceof TileEntityManager) {
			return (TileEntityManager)TE;
		}
		return null;
	}
	
	
  
	@Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && entityPlayer.getDistanceSq((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D;
    }

	
	private int translateSlotId(int slot) {
		return slot % 60;
	}
	
	private TileEntityManager getManagerFromSlotId(int slot) {
		TileEntityManager[] inventories = getInventories();
		int id = slot / 60;
		
		if (!hasTop || !hasBot) {
			id = 0;
		}
		
		if (id < 0 || id >= inventories.length) {
			return null;			
		}else{
			return inventories[id];
		}
	}
 
 	@Override
    public int getSizeInventory() {
 		return 120;
    }

	@Override
    public ItemStack getStackInSlot(int slot) {
		TileEntityManager manager = getManagerFromSlotId(slot);
		if (manager != null) {	
			return manager.getStackInSlot(translateSlotId(slot));
		}else{
			return null;			
		}
    }	
	
	@Override
    public ItemStack decrStackSize(int slot, int count) {
		TileEntityManager manager = getManagerFromSlotId(slot);
		if (manager != null) {	
			return manager.decrStackSize(translateSlotId(slot), count);
		}else {
			return null;
		}
    }	
	
	@Override
    public void setInventorySlotContents(int slot, ItemStack itemstack) {
		TileEntityManager manager = getManagerFromSlotId(slot);
		if (manager != null) {	
			manager.setInventorySlotContents(translateSlotId(slot), itemstack);
		}
    }

	@Override
    public String getInventoryName() {
        return "container.external_distributor";
    }
	
	@Override
    public boolean hasCustomInventoryName() {
        return false;
    }	
	
	@Override
    public int getInventoryStackLimit() {
        return 64;
    }	
	
	@Override
    public void closeInventory() {
    }

	@Override
    public void openInventory() {
    }
	
	@Override
 	public ItemStack getStackInSlotOnClosing(int slot) {
		TileEntityManager manager = getManagerFromSlotId(slot);
		if (manager != null) {	
			return manager.getStackInSlotOnClosing(translateSlotId(slot));
		}else {
			return null;
		}		
    }
 
 

	private boolean isChunkValid(DistributorSide side, TileEntityManager manager, int chunkId, boolean top) {
		for (DistributorSetting setting : DistributorSetting.settings) {
			if (setting.isEnabled(this)) {
				if (side.isSet(setting.getId())) {
					if (setting.isValid(manager, chunkId, top)) {
						return true;
					}
				}					
			}
		}
		return false;
	}
	
	
	
   /**
     * Fills fluid into internal tanks, distribution is left to the ITankContainer.
     * @param from Orientation the fluid is pumped in from.
     * @param resource FluidStack representing the maximum amount of fluid filled into the ITankContainer
     * @param doFill If false filling will only be simulated.
     * @return Amount of resource that was filled into internal tanks.
     */
	@Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		IFluidTank[] tanks = getTanks(from);
	
		int amount = 0;
		for (IFluidTank tank : tanks) {
			amount += tank.fill(resource, doFill);
		}
		return amount;	
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return drain(from, null, maxDrain, doDrain);
	}
	
	
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return drain(from, resource, resource == null ? 0 : resource.amount, doDrain);
	}
	
	private FluidStack drain(ForgeDirection from, FluidStack resource, int maxDrain, boolean doDrain) {
		FluidStack ret = resource;
		if (ret != null) {
			ret = ret.copy();
			ret.amount = 0;
		}
		
		IFluidTank[] tanks = getTanks(from);
		for (IFluidTank tank : tanks) {
			FluidStack temp;
			temp = tank.drain(maxDrain, doDrain);
			
			if (temp != null && (ret == null || ret.isFluidEqual(temp))) {
				if (ret == null) {
					ret = temp;
				}else{
					ret.amount += temp.amount;
				}
			
				maxDrain -= temp.amount;
				if (maxDrain <= 0) {
					break;
				}
			}
		}
		if (ret != null && ret.amount == 0) {
			return null;
		}
		return ret;	
	}

    /**
     * @param direction tank side: UNKNOWN for default tank set
     * @return Array of {@link FluidTank}s contained in this ITankContainer for this direction
     */

	private IFluidTank[] getTanks(ForgeDirection direction) {
		TileEntityManager[] inventories = getInventories();
	
		if (inventories.length > 0) {
			for (DistributorSide side : getSides()) {
				if (side.getSide() == direction) {
					ArrayList<IFluidTank> tanks = new ArrayList<IFluidTank>();
					
					if (hasTop && hasBot) {
						populateTanks(tanks, side, inventories[0], false);
						populateTanks(tanks, side, inventories[1], true);
					}else if(hasTop) {
						populateTanks(tanks, side, inventories[0], true);
					}else if(hasBot) {
						populateTanks(tanks, side, inventories[0], false);
					}		
					
					return tanks.toArray(new IFluidTank[tanks.size()]);
				}
			}
		}
		return new IFluidTank[] {};
	}



	
	private void populateTanks(ArrayList<IFluidTank> tanks, DistributorSide side, TileEntityManager manager, boolean top) {
		if (manager != null && manager instanceof TileEntityLiquid) {
			TileEntityLiquid fluid = (TileEntityLiquid)manager;
			Tank[] managerTanks = fluid.getTanks();
			for (int i = 0; i < 4; i++) {
				if (isChunkValid(side, manager, i, top)) {
					if (!tanks.contains(managerTanks[i])) {
						tanks.add(managerTanks[i]);
					}
				}
			}
		}
	}
	
	private void populateSlots(ArrayList<Integer> slotChunks, DistributorSide side, TileEntityManager manager, boolean top) {
		if (manager != null && manager instanceof TileEntityCargo) {
			for (int i = 0; i < 4; i++) {
				if (isChunkValid(side, manager, i, top)) {					
					int chunkId = i + (top ? 4 : 0);
					if (!slotChunks.contains(chunkId)) {
						slotChunks.add(chunkId);
					}
				}
			}
		}
	}
	

	

    //slots
	@Override
    public int[] getAccessibleSlotsFromSide(int direction) {
		TileEntityManager[] inventories = getInventories();
		
		if (inventories.length > 0) {
			for (DistributorSide side : getSides()) {
				if (side.getIntSide() == direction) {
					ArrayList<Integer> slotChunks = new ArrayList<Integer>();
					
					if (hasTop && hasBot) {
						populateSlots(slotChunks, side, inventories[0], false);
						populateSlots(slotChunks, side, inventories[1], true);
					}else if(hasTop) {
						populateSlots(slotChunks, side, inventories[0], true);
					}else if(hasBot) {
						populateSlots(slotChunks, side, inventories[0], false);
					}		
					
					int[] ret = new int[slotChunks.size() * 15];
					int id = 0;
					for (int chunkId : slotChunks) {
						for (int i = 0; i < 15; i++) {
							ret[id] = chunkId * 15 + i;
							id++;
						}
					}
					
					return ret;
				}
				
			}
		}
		return new int[] {};
    }

    //in
	@Override
    public boolean canInsertItem(int slot, ItemStack item, int side) {
        return true;
    }

    //out
	@Override
    public boolean canExtractItem(int slot, ItemStack item, int side) {
        return true;
    }   
	
	@Override
    public boolean isItemValidForSlot(int slotId, ItemStack item) {
		return true;
	}



	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		IFluidTank[] tanks =  getTanks(from);
		FluidTankInfo[] info = new FluidTankInfo[tanks.length];
		for (int i = 0; i < info.length; i++) {
			info[i] = new FluidTankInfo(tanks[i].getFluid(), tanks[i].getCapacity());
		}
		return info;
	}

	
	
}
