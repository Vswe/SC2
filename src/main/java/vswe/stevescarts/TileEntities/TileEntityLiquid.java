package vswe.stevescarts.TileEntities;
import java.util.ArrayList;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Containers.ContainerBase;
import vswe.stevescarts.Containers.ContainerLiquid;
import vswe.stevescarts.Containers.ContainerManager;
import vswe.stevescarts.Helpers.ITankHolder;
import vswe.stevescarts.Helpers.ManagerTransfer;
import vswe.stevescarts.Helpers.Tank;
import vswe.stevescarts.Helpers.TransferHandler;
import vswe.stevescarts.Interfaces.GuiBase;
import vswe.stevescarts.Interfaces.GuiLiquid;
import vswe.stevescarts.Modules.Storages.Tanks.ModuleTank;
import vswe.stevescarts.Slots.SlotLiquidFilter;
import vswe.stevescarts.Slots.SlotLiquidManagerInput;
import vswe.stevescarts.Slots.SlotLiquidOutput;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityLiquid extends TileEntityManager  implements IFluidHandler, ITankHolder, ISidedInventory
{
	
	@SideOnly(Side.CLIENT)
	@Override
	public GuiBase getGui(InventoryPlayer inv) {
		return new GuiLiquid(inv, this);
	}
	
	@Override
	public ContainerBase getContainer(InventoryPlayer inv) {
		return new ContainerLiquid(inv, this);		
	}	
	
	Tank[] tanks;

    public TileEntityLiquid()
    {
		super();
		tanks = new Tank[4];
		for (int i = 0; i < 4; i++) {
			tanks[i] = new Tank(this, 32000, i);
		}
    }

	public Tank[] getTanks() {
		return tanks;
	}

	private int tick;
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		if (tick-- <= 0) {
			tick = 5;
		}else{
			return;
		}
		
		if (!worldObj.isRemote) {
			for (int i = 0; i < 4; i++) {
				tanks[i].containerTransfer();
			}
		}
	}
	
	
    /**
     * Fills fluid into internal tanks, distribution is left to the ITankContainer.
     * @param from Orientation the fluid is pumped in from.
     * @param resource FluidStack representing the maximum amount of fluid filled into the ITankContainer
     * @param doFill If false filling will only be simulated.
     * @return Amount of resource that was filled into internal tanks.
     */
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		
		
		int amount = 0;
		if (resource != null && resource.amount > 0) {
			FluidStack fluid = resource.copy();
			for (int i = 0; i < 4; i++) {
				int tempAmount = tanks[i].fill(fluid, doFill, worldObj.isRemote);
		
				amount += tempAmount;
				fluid.amount -= tempAmount;
				if (fluid.amount <= 0) {
					break;
				}
			}
		}
		return amount;	
	}
    /**
     * Fills fluid into the specified internal tank.
     * @param tankIndex the index of the tank to fill
     * @param resource FluidStack representing the maximum amount of fluid filled into the ITankContainer
     * @param doFill If false filling will only be simulated.
     * @return Amount of resource that was filled into internal tanks.
     */
    public int fill(int tankIndex, FluidStack resource, boolean doFill) {
		if (tankIndex < 0 || tankIndex >= 4) {
			return 0;
		}
	
		return tanks[tankIndex].fill(resource, doFill, worldObj.isRemote);	
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
		for (int i = 0; i < 4; i++) {
			FluidStack temp = tanks[i].drain(maxDrain, false, worldObj.isRemote);
			
			if (temp != null && (ret == null || ret.isFluidEqual(temp))) {
                temp = tanks[i].drain(maxDrain, doDrain, worldObj.isRemote);

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

	
	@Override
    public int getSizeInventory()
    {
        return 12;
    }


	@Override
    public String getInvName()
    {
        return "container.fluidmanager";
    }


	@Override
	public ItemStack getInputContainer(int tankid) {
		return getStackInSlot(tankid * 3);
	}	
	
	@Override
	public void clearInputContainer(int tankid) {
		setInventorySlotContents(tankid * 3, null);	
	}
	
	@Override
	public void addToOutputContainer(int tankid, ItemStack item) {
		TransferHandler.TransferItem(item, this, tankid * 3 + 1, tankid * 3 + 1, new ContainerLiquid(null, this), Slot.class, null, -1);
	}
	
	
	
	@Override
	public void onFluidUpdated(int tankid) {
		onInventoryChanged();
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawImage(int tankid, GuiBase gui, Icon icon, int targetX, int targetY, int srcX, int srcY, int sizeX, int sizeY) {
		gui.drawIcon(icon, gui.getGuiLeft() + targetX, gui.getGuiTop() + targetY, sizeX / 16F, sizeY / 16F, srcX / 16F, srcY / 16F);
	}	
	

	@Override
	protected boolean isTargetValid(ManagerTransfer transfer) {
		return true;
	}
	
	@Override
	protected boolean doTransfer(ManagerTransfer transfer) {
		int maximumToTransfer = hasMaxAmount(transfer.getSetting()) ? Math.min(getMaxAmount(transfer.getSetting()) - transfer.getWorkload(), FluidContainerRegistry.BUCKET_VOLUME) : FluidContainerRegistry.BUCKET_VOLUME;
		
		boolean sucess = false;
	
		if (toCart[transfer.getSetting()]) {
			for (int i = 0; i < tanks.length; i++) {
				int fill = fillTank(transfer.getCart(), i, transfer.getSetting(), maximumToTransfer, false);
				if (fill > 0) {
					fillTank(transfer.getCart(), i, transfer.getSetting(), fill, true);
					sucess = true;
					if (hasMaxAmount(transfer.getSetting())) {
						transfer.setWorkload(transfer.getWorkload() + fill);
					}
					break;
				}
			}			
		}else{
			ArrayList<ModuleTank> cartTanks = transfer.getCart().getTanks();
			for (IFluidTank cartTank : cartTanks) {
				int drain = drainTank(cartTank, transfer.getSetting(), maximumToTransfer, false);
				if (drain > 0) {
					drainTank(cartTank, transfer.getSetting(), drain, true);
					sucess = true;
					if (hasMaxAmount(transfer.getSetting())) {
						transfer.setWorkload(transfer.getWorkload() + drain);
					}
					break;
				}
				
			}
		}
	
		if (sucess && hasMaxAmount(transfer.getSetting()) && transfer.getWorkload() == getMaxAmount(transfer.getSetting())) {
			transfer.setLowestSetting(transfer.getSetting() + 1); //this is not avalible anymore
		}
	
		return sucess;
	}

	private int fillTank(MinecartModular cart, int tankId, int sideId, int fillAmount,  boolean doFill) {
		if (isTankValid(tankId, sideId)) {
		
			FluidStack fluidToFill = tanks[tankId].drain(fillAmount, doFill);
			if (fluidToFill == null) {
				return 0;
			}	
			
			fillAmount = fluidToFill.amount;
			
			if (isFluidValid(sideId, fluidToFill)) {
				ArrayList<ModuleTank> cartTanks = cart.getTanks();
				for (IFluidTank cartTank : cartTanks) {
					
					fluidToFill.amount -= cartTank.fill(fluidToFill, doFill);
					if (fluidToFill.amount <= 0) {
						return fillAmount;
					}
					
				}
				return fillAmount - fluidToFill.amount;
			}	
		
		}
		return 0;
	}
	
	private int drainTank(IFluidTank cartTank, int sideId, int drainAmount,  boolean doDrain) {
		FluidStack drainedFluid = cartTank.drain(drainAmount, doDrain);
		if (drainedFluid == null) {
			return 0;
		}		
		drainAmount = drainedFluid.amount;
		
		if (isFluidValid(sideId, drainedFluid)) {
			for (int i = 0; i < tanks.length; i++) {
				Tank tank = tanks[i];
				if (isTankValid(i, sideId)) {
					drainedFluid.amount -= tank.fill(drainedFluid, doDrain);
					if (drainedFluid.amount <= 0) {
						return drainAmount;
					}
				}
			}
			return drainAmount - drainedFluid.amount;
		}	
		return 0;
	}
	
	private boolean isTankValid(int tankId, int sideId) {
		return !((layoutType == 1 && tankId != sideId) || (layoutType == 2 && color[sideId] != color[tankId]));
	}
	
	private boolean isFluidValid(int sideId, FluidStack fluid) {
		ItemStack filter = getStackInSlot(sideId * 3 + 2);
		FluidStack filterFluid = FluidContainerRegistry.getFluidForFilledItem(filter);
		if (filterFluid != null) {
			if (!filterFluid.isFluidEqual(fluid)) {
				return false;
			}
		}
		return true;		
	}
	
	public int getMaxAmount(int id) {
		return (int)(getMaxAmountBuckets(id) * FluidContainerRegistry.BUCKET_VOLUME);
	}
	
	public float getMaxAmountBuckets(int id) {
		switch(getAmountId(id)) {
			case 1:
				return 0.25F;
			case 2:
				return 0.5F;
			case 3:
				return 0.75F;
			case 4:
				return 1F;
			case 5:
				return 2F;
			case 6:
				return 3F;
			case 7:
				return 5F;
			case 8:
				return 7.5F;
			case 9:
				return 10F;
			case 10:
				return 15F;
			default:
				return 0;
		}
	}
	
	public boolean hasMaxAmount(int id) {
		return getAmountId(id) != 0;
	}
	
	@Override
	public int getAmountCount() {
		return 11;
	}
	
	@Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        for (int i = 0; i < 4; i++) {
			tanks[i].setFluid(FluidStack.loadFluidStackFromNBT(nbttagcompound.getCompoundTag("Fluid" + i)));	
        }
		setWorkload(nbttagcompound.getShort("workload"));
	}
	
	@Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
		super.writeToNBT(nbttagcompound);
        for (int i = 0; i < 4; i++) {	
			if (tanks[i].getFluid() != null) {
				NBTTagCompound compound = new NBTTagCompound();
				tanks[i].getFluid().writeToNBT(compound);
				nbttagcompound.setCompoundTag("Fluid" + i, compound);	
			}
		}	
		nbttagcompound.setShort("workload", (short)getWorkload());
	}	


	
	@Override
	public void checkGuiData(ContainerManager conManager, ICrafting crafting, boolean isNew) {
		super.checkGuiData(conManager, crafting, isNew);
		ContainerLiquid con = (ContainerLiquid)conManager;

		for (int i = 0; i < 4; i++) {
			boolean changed = false;
			int id = 4 + i * 4;
			int amount1 = 4 + i * 4 + 1;
			int amount2 = 4 + i * 4 + 2;
			int meta = 4 + i * 4 + 3;
			if ((isNew || con.oldLiquids[i] != null) && tanks[i].getFluid() == null) {
				updateGuiData(con, crafting, id, (short)-1);
				changed = true;
			}else if(tanks[i].getFluid() != null) {
				if (isNew || con.oldLiquids[i] == null) {
					updateGuiData(con, crafting, id, (short)tanks[i].getFluid().fluidID);	
					updateGuiData(con, crafting, amount1, getShortFromInt(true, tanks[i].getFluid().amount));	
					updateGuiData(con, crafting, amount2, getShortFromInt(false, tanks[i].getFluid().amount));		
					changed = true;
				}else{
				
					if (con.oldLiquids[i].fluidID != tanks[i].getFluid().fluidID) {
						updateGuiData(con, crafting, id, (short)tanks[i].getFluid().fluidID);
						changed = true;
					}					
					if (con.oldLiquids[i].amount != tanks[i].getFluid().amount) {
						updateGuiData(con, crafting, amount1, getShortFromInt(true, tanks[i].getFluid().amount));	
						updateGuiData(con, crafting, amount2, getShortFromInt(false, tanks[i].getFluid().amount));		
						changed = true;
					}					
				
				}
			
			}
			
			if (changed) {
				if (tanks[i].getFluid() == null) {
					con.oldLiquids[i] = null;
				}else{
					con.oldLiquids[i] = tanks[i].getFluid().copy();
				}
			}
		}
	}
	
	//TODO sync the tag somehow :S
	@Override
	public void receiveGuiData(int id, short data) {
		if(id > 3) {
			id -= 4;
			int tankid = id / 4;
			int contentid = id % 4;
			
			if (contentid == 0) {
				if (data == -1) {
					tanks[tankid].setFluid(null);
				}else if (tanks[tankid].getFluid() == null){
					tanks[tankid].setFluid(new FluidStack(data, 0));
				}
			}else if(tanks[tankid].getFluid() != null) {

				tanks[tankid].getFluid().amount = getIntFromShort(contentid == 1,tanks[tankid].getFluid().amount, data);
				
			}
			
		}else{
			super.receiveGuiData(id, data);
		}
	}
	

	
	
	private boolean isInput(int id) {
		return id % 3 == 0;
	}
	
	private boolean isOutput(int id) {
		return id % 3 == 1;
	}
	
	@Override
    public boolean isItemValidForSlot(int slotId, ItemStack item)
    {	
		if (isInput(slotId)) {
			return SlotLiquidManagerInput.isItemStackValid(item, this, -1);
		}else if(isOutput(slotId)) {
			return SlotLiquidOutput.isItemStackValid(item);
		}else{
			return SlotLiquidFilter.isItemStackValid(item);
		}
		
	}	
	
	private static final int[] topSlots = new int[] {0, 3, 6, 9};
	private static final int[] botSlots = new int[] {1, 4, 7, 10};
	private static final int[] sideSlots = new int[] {};
	
    //slots
	@Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        if (side == 1) {
        	return topSlots;
        }else if(side == 0) {
        	return botSlots;
        }else{
        	return sideSlots;
        }
    }

    //in
	@Override
    public boolean canInsertItem(int slot, ItemStack item, int side)
    {
        return side == 1  &&  isInput(slot) && this.isItemValidForSlot(slot, item);
    }

    //out
	@Override
    public boolean canExtractItem(int slot, ItemStack item, int side)
    {
        return side == 0 && isOutput(slot);
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
		FluidTankInfo[] info = new FluidTankInfo[tanks.length];
		for (int i = 0; i < tanks.length; i++) {
			info[i] = new FluidTankInfo(tanks[i].getFluid(),tanks[i].getCapacity());		
		}
		return info;
	} 	
	
}
