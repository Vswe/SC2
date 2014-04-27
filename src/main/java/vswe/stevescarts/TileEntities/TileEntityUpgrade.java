package vswe.stevescarts.TileEntities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import vswe.stevescarts.Containers.ContainerBase;
import vswe.stevescarts.Containers.ContainerUpgrade;
import vswe.stevescarts.Helpers.ITankHolder;
import vswe.stevescarts.Helpers.Tank;
import vswe.stevescarts.Helpers.TransferHandler;
import vswe.stevescarts.Interfaces.GuiBase;
import vswe.stevescarts.Interfaces.GuiUpgrade;
import vswe.stevescarts.Upgrades.AssemblerUpgrade;
import vswe.stevescarts.Upgrades.InterfaceEffect;
import vswe.stevescarts.Upgrades.InventoryEffect;
import vswe.stevescarts.Upgrades.TankEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
public class TileEntityUpgrade extends TileEntityBase 
	implements IInventory, ISidedInventory, IFluidHandler, IFluidTank, ITankHolder
{

	@SideOnly(Side.CLIENT)
	@Override
	public GuiBase getGui(InventoryPlayer inv) {
		return new GuiUpgrade(inv, this);
	}
	
	@Override
	public ContainerBase getContainer(InventoryPlayer inv) {
		return new ContainerUpgrade(inv, this);		
	}
	
	public Tank tank;
	private TileEntityCartAssembler master;
	private int type;
	public void setMaster(TileEntityCartAssembler master) {
		if (worldObj.isRemote && this.master != master) {
			worldObj.markBlockForUpdate(this.xCoord,this.yCoord,this.zCoord);
		}
		this.master = master;
		
	}
	
	public TileEntityCartAssembler getMaster() {
		return master;
	}
	
	private boolean initialized;
	public void setType(int type) {
		this.type = type;
		
		if (!initialized) {
			initialized = true;
			AssemblerUpgrade upgrade = getUpgrade();
			if (upgrade != null) {
				comp = new NBTTagCompound();
				slotsForSide = new int[upgrade.getInventorySize()];
				upgrade.init(this);
				if (upgrade.getInventorySize() > 0) {
					inventoryStacks = new ItemStack[upgrade.getInventorySize()];
					for (int i = 0; i < slotsForSide.length; i++) {
						slotsForSide[i] = i;
					}
				}
			}else{
				inventoryStacks = null;
			}
		}
	}
	
	public int getType() {
		return type;
	}
	
	private NBTTagCompound comp;
	public NBTTagCompound getCompound() {
		return comp;
	}
	

	@Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, var1);
    }	
	
	@Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
		this.readFromNBT(pkt.data);
    }
	
	public AssemblerUpgrade getUpgrade() {
		return AssemblerUpgrade.getUpgrade(type);
	}

	public TileEntityUpgrade() {
	
	}
	
	@SideOnly(Side.CLIENT)
	public Icon getTexture(boolean outside) {
		if (getUpgrade() == null) {
			return null;
		}
	
		return outside ? getUpgrade().getMainTexture() : getUpgrade().getSideTexture();
	}
	
		
	public boolean hasInventory() {
		return inventoryStacks != null;
	}
	
	
   /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound tagCompound)
    {
		super.readFromNBT(tagCompound);
		
		setType(tagCompound.getByte("Type"));
		
		NBTTagList items = tagCompound.getTagList("Items");

		for (int i = 0; i < items.tagCount(); ++i)
		{
			NBTTagCompound item = (NBTTagCompound)items.tagAt(i);
			int slot = item.getByte("Slot") & 255;

			ItemStack iStack = ItemStack.loadItemStackFromNBT(item);
			
			if (slot >= 0 && slot < getSizeInventory())
			{
				setInventorySlotContents(slot, iStack);
			}
		}		
		
		AssemblerUpgrade upgrade = getUpgrade();
		if (upgrade != null) {
			upgrade.load(this, tagCompound);
		}
    }


    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound tagCompound)
    {
		super.writeToNBT(tagCompound);
		
		NBTTagList items = new NBTTagList();
		
		if (inventoryStacks != null) {
			for (int i = 0; i < inventoryStacks.length; ++i)
			{
				ItemStack iStack = inventoryStacks[i];
			
				if (iStack != null)
				{
					NBTTagCompound item = new NBTTagCompound();
					item.setByte("Slot", (byte)i);
					iStack.writeToNBT(item);
					items.appendTag(item);
				}
			}
		}
		
		tagCompound.setTag("Items", items);
		
		tagCompound.setByte("Type", (byte)type);
		
		AssemblerUpgrade upgrade = getUpgrade();
		if (upgrade != null) {
			upgrade.save(this, tagCompound);
		}		
    }	
	
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }

        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }	
	
	@Override
	public void updateEntity() {
		if (getUpgrade() != null && getMaster() != null) {
			getUpgrade().update(this);
		}
	}	
	
	@Override
	public void initGuiData(Container con, ICrafting crafting) {
		if (getUpgrade() != null) {
			InterfaceEffect gui = getUpgrade().getInterfaceEffect();
			if (gui != null) {
				gui.checkGuiData(this, (ContainerUpgrade)con, crafting, true);
			}
		}
	}
	
	@Override
	public void checkGuiData(Container con, ICrafting crafting) {
		if (getUpgrade() != null) {
			InterfaceEffect gui = getUpgrade().getInterfaceEffect();
			if (gui != null) {
				gui.checkGuiData(this, (ContainerUpgrade)con, crafting, false);
			}
		}
	}
	
	@Override
	public void receiveGuiData(int id, short data) {
		if (getUpgrade() != null) {
			InterfaceEffect gui = getUpgrade().getInterfaceEffect();
			if (gui != null) {
				gui.receiveGuiData(this, id, data);
			}
		}		
	}
	
	//INVENTORY STUFF BELOW
	
	ItemStack[] inventoryStacks;
	private int[] slotsForSide;
	
	@Override
    public int getSizeInventory()
    {
		if (inventoryStacks == null) {
			if (master == null) {
				return 0;
			}else{
				return master.getSizeInventory();
			}
		}else{
			return inventoryStacks.length;
		}
    }

	@Override
    public ItemStack getStackInSlot(int i)
    {
		if (inventoryStacks == null) {
			if (master == null) {
				return null;
			}else{
				return master.getStackInSlot(i);
			}
		}else if (i < 0 || i >= getSizeInventory()) {
			return null;		
		}else{
			return inventoryStacks[i];
		}
    }	
	
	@Override
    public ItemStack decrStackSize(int i, int j)
    {
		if (inventoryStacks == null) {
			if (master == null) {
				return null;
			}else{
				return master.decrStackSize( i, j);
			}
		}else if (i < 0 || i >= getSizeInventory()) {
			return null;				
		}else{	
			if (inventoryStacks[i] != null)
			{
				if (inventoryStacks[i].stackSize <= j)
				{
					ItemStack itemstack = inventoryStacks[i];
					inventoryStacks[i] = null;
					onInventoryChanged();
					return itemstack;
				}

				ItemStack itemstack1 = inventoryStacks[i].splitStack(j);

				if (inventoryStacks[i].stackSize == 0)
				{
					inventoryStacks[i] = null;
				}

				onInventoryChanged();
				return itemstack1;
			}
			else
			{
				return null;
			}
		}
    }	
	
	@Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
		if (inventoryStacks == null) {
			if (master != null) {
				master.setInventorySlotContents(i, itemstack);
			}
		}else if (i < 0 || i >= getSizeInventory()) {
			return;				
		}else{
			inventoryStacks[i] = itemstack;

			if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
			{
				itemstack.stackSize = getInventoryStackLimit();
			}

			onInventoryChanged();
		}
    }


	@Override
    public String getInvName()
    {
        return "container.assemblerupgrade";
    }
	
	@Override
    public boolean isInvNameLocalized()
    {
        return false;
    }	
	
	@Override
    public int getInventoryStackLimit()
    {
        return 64;
    }	
	
	@Override
    public void closeChest()
    {
    }
	@Override
    public void openChest()
    {
    }
		
	@Override
	public ItemStack getStackInSlotOnClosing(int i)
    {
		if (inventoryStacks == null) {
			if (master == null) {
				return null;
			}else{
				return master.getStackInSlotOnClosing(i);
			}
		}else{
			ItemStack item = getStackInSlot(i);
		
			if (item != null)
			{
				setInventorySlotContents(i, null);
				return item;
			}
			else
			{
				return null;
			}
		}	
    }		
	

	@Override
	public void onInventoryChanged() {
		if (getUpgrade() != null) {
			InventoryEffect inv = getUpgrade().getInventoryEffect();
			if (inv != null) {
				inv.onInventoryChanged(this);
			}
		}
	}
	
	@Override
    public boolean isItemValidForSlot(int slot, ItemStack item)
    {	
		if (getUpgrade() != null) {
			InventoryEffect inv = getUpgrade().getInventoryEffect();
			if (inv != null) {
				return inv.isItemValid(slot, item);
			}
		}
		
		if (getMaster() != null) {
			return getMaster().isItemValidForSlot(slot, item);
		}		
		
		return false;
	}
	
	
    //slots
	@Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
		
		if (getUpgrade() != null) {
			InventoryEffect inv = getUpgrade().getInventoryEffect();
			if (inv != null) {
				return slotsForSide;
			}
		}	
		
		if (getMaster() != null) {
			return getMaster().getAccessibleSlotsFromSide(side);
		}
		return new int[] {};
    }

    //in
	@Override
    public boolean canInsertItem(int slot, ItemStack item, int side)
    {
		if (getUpgrade() != null) {
			InventoryEffect inv = getUpgrade().getInventoryEffect();
			if (inv != null) {
				return isItemValidForSlot(slot, item);
			}
		}	
		
		if (getMaster() != null) {
			return getMaster().canInsertItem(slot, item, side);
		}
		
		return false;
    }

    //out
	@Override
    public boolean canExtractItem(int slot, ItemStack item, int side)
    {
		if (getUpgrade() != null) {
			InventoryEffect inv = getUpgrade().getInventoryEffect();
			if (inv != null) {
				return true;
			}
		}	
		
		if (getMaster() != null) {
			return getMaster().canExtractItem(slot, item, side);
		}
		
		return false;
    }    	
	
	
	//tank stuff
	
    /**
     * Fills fluid into internal tanks, distribution is left to the ITankContainer.
     * @param from Orientation the fluid is pumped in from.
     * @param resource FluidStack representing the maximum amount of fluid filled into the ITankContainer
     * @param doFill If false filling will only be simulated.
     * @return Amount of resource that was filled into internal tanks.
     */
	@Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return this.fill(resource, doFill);		
	}
 

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		if (resource != null && resource.isFluidEqual(getFluid())) {
			return drain(from, resource.amount, doDrain);			
		}else{
			return null;
		}
	}		
	
    /**
     * Drains fluid out of internal tanks, distribution is left to the ITankContainer.
     * @param from Orientation the fluid is drained to.
     * @param maxDrain Maximum amount of fluid to drain.
     * @param doDrain If false draining will only be simulated.
     * @return FluidStack representing the fluid and amount actually drained from the ITankContainer
     */
	@Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return this.drain(maxDrain, doDrain);
	}




	@Override
	public FluidStack getFluid() {
		if (tank == null) {
			return null;
		}else{
			return tank.getFluid();
		}
	}

	@Override
	public int getCapacity() {
		if (tank == null) {
			return 0;
		}else{
			return tank.getCapacity();
		}
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (tank == null) {
			return 0;
		}else{
			int result = tank.fill(resource, doFill);
			return result;
		}		
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (tank == null) {
			return null;
		}else{
			FluidStack result = tank.drain(maxDrain, doDrain);
			return result;
		}	
	}


	
	@Override
	public ItemStack getInputContainer(int tankid) {
		return getStackInSlot(0);
	}	
	
	@Override
	public void clearInputContainer(int tankid) {
		setInventorySlotContents(0, null);
	}
	
	@Override
	public void addToOutputContainer(int tankid, ItemStack item) {
		TransferHandler.TransferItem(item, this, 1, 1, new ContainerUpgrade(null, this), Slot.class, null, -1);
	}
	
	@Override
	public void onFluidUpdated(int tankid) {

	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawImage(int tankid, GuiBase gui, Icon icon, int targetX, int targetY, int srcX, int srcY, int sizeX, int sizeY) {
		gui.drawIcon(icon, gui.getGuiLeft() + targetX, gui.getGuiTop() + targetY, sizeX / 16F, sizeY / 16F, srcX / 16F, srcY / 16F);
	}

	@Override
	public int getFluidAmount() {
		return tank == null ? 0 : tank.getFluidAmount();
	}

	@Override
	public FluidTankInfo getInfo() {
		return tank == null ? null : tank.getInfo();
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
		return new FluidTankInfo[] {getInfo()};
	}


	
	
    
}