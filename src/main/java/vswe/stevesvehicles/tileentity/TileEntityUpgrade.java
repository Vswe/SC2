package vswe.stevesvehicles.tileentity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import vswe.stevesvehicles.container.ContainerBase;
import vswe.stevesvehicles.container.ContainerUpgrade;
import vswe.stevesvehicles.nbt.NBTHelper;
import vswe.stevesvehicles.tanks.Tank;
import vswe.stevesvehicles.client.gui.screen.GuiBase;
import vswe.stevesvehicles.client.gui.screen.GuiUpgrade;
import vswe.stevesvehicles.upgrade.Upgrade;
import vswe.stevesvehicles.upgrade.effect.BaseEffect;
import vswe.stevesvehicles.upgrade.EffectType;
import vswe.stevesvehicles.upgrade.effect.util.InterfaceEffect;
import vswe.stevesvehicles.upgrade.effect.util.InventoryEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.upgrade.effect.util.TankEffect;
import vswe.stevesvehicles.upgrade.registry.UpgradeRegistry;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class TileEntityUpgrade extends TileEntityBase
	implements IInventory, ISidedInventory, IFluidHandler, IFluidTank {

	@SideOnly(Side.CLIENT)
	@Override
	public GuiBase getGui(InventoryPlayer inv) {
		return new GuiUpgrade(inv, this);
	}
	
	@Override
	public ContainerBase getContainer(InventoryPlayer inv) {
		return new ContainerUpgrade(inv, this);		
	}

	private TileEntityCartAssembler master;
	private int type;
    private List<BaseEffect> effects;
    private InterfaceEffect interfaceEffect;
    private InventoryEffect inventoryEffect;
    private TankEffect tankEffect;



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
			Upgrade upgrade = getUpgrade();
			if (upgrade != null) {
                createEffects(upgrade);

                for (BaseEffect effect : effects) {
                    if (effect instanceof InterfaceEffect) {
                        interfaceEffect = (InterfaceEffect)effect;
                    }
                    if (effect instanceof InventoryEffect) {
                        inventoryEffect = (InventoryEffect)effect;
                    }
                    if (effect instanceof TankEffect) {
                        tankEffect = (TankEffect)effect;
                    }
                }
                int inventorySize = inventoryEffect != null ? inventoryEffect.getInventorySize() : 0;
                slotsForSide = new int[inventorySize];
				init();
				if (inventorySize > 0) {
					inventoryStacks = new ItemStack[inventorySize];
					for (int i = 0; i < slotsForSide.length; i++) {
						slotsForSide[i] = i;
					}
				}
			}else{
                effects = null;
				inventoryStacks = null;
			}
		}
	}

    private void createEffects(Upgrade upgrade) {
        effects = new ArrayList<BaseEffect>();
        for (EffectType effectType : upgrade.getEffectTypes()) {
            try {
                Object[] params = new Object[effectType.getParams().length + 1];
                params[0] = this;
                for (int i = 0; i < effectType.getParams().length; i++) {
                    params[i + 1] = effectType.getParams()[i];
                }

                Class[] paramClasses = new Class[params.length];
                for (int i = 0; i < params.length; i++) {
                    paramClasses[i] = params[i].getClass();
                }

                Constructor<? extends BaseEffect> constructor = effectType.getClazz().getConstructor(paramClasses);
                Object obj = constructor.newInstance(params);
                effects.add((BaseEffect)obj);
            }catch (Exception ex) {
                System.err.println("Failed to create a new instance of " + effectType.getClazz().getName());
                ex.printStackTrace();
            }
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, var1);
    }	
	
	@Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.func_148857_g());
    }
	
	public Upgrade getUpgrade() {
		return UpgradeRegistry.getUpgradeFromId(type);
	}

	public TileEntityUpgrade() {
	
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getTexture(boolean outside) {
		if (getUpgrade() == null) {
			return null;
		}
	
		return outside ? getUpgrade().getMainTexture() : getUpgrade().getSideTexture();
	}
	
		
	public boolean hasInventory() {
		return inventoryStacks != null;
	}
	
	
    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		
		setType(tagCompound.getByte("Type"));
		
		NBTTagList items = tagCompound.getTagList("Items", NBTHelper.COMPOUND.getId());

		for (int i = 0; i < items.tagCount(); ++i) {
			NBTTagCompound item = items.getCompoundTagAt(i);
			int slot = item.getByte("Slot") & 255;

			ItemStack iStack = ItemStack.loadItemStackFromNBT(item);
			
			if (slot >= 0 && slot < getSizeInventory()) {
				setInventorySlotContents(slot, iStack);
			}
		}		

		if (effects != null) {
			load(tagCompound);
		}
    }


    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
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

		if (effects != null) {
			save(tagCompound);
		}		
    }	
	
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && entityPlayer.getDistanceSq((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D;
    }
	
	@Override
	public void updateEntity() {
		if (effects != null && getMaster() != null) {
			update();
		}
	}	
	
	@Override
	public void initGuiData(Container con, ICrafting crafting) {
		if (effects != null && interfaceEffect != null) {
            interfaceEffect.checkGuiData((ContainerUpgrade)con, crafting, true);
		}
	}
	
	@Override
	public void checkGuiData(Container con, ICrafting crafting) {
		if (effects != null && interfaceEffect != null) {
            interfaceEffect.checkGuiData((ContainerUpgrade)con, crafting, false);
		}
	}
	
	@Override
	public void receiveGuiData(int id, short data) {
		if (effects != null && interfaceEffect != null) {
		    interfaceEffect.receiveGuiData(id, data);
		}		
	}
	
	//INVENTORY STUFF BELOW
	
	ItemStack[] inventoryStacks;
	private int[] slotsForSide;
	
	@Override
    public int getSizeInventory() {
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
    public ItemStack getStackInSlot(int i) {
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
    public ItemStack decrStackSize(int i, int j) {
		if (inventoryStacks == null) {
			if (master == null) {
				return null;
			}else{
				return master.decrStackSize( i, j);
			}
		}else if (i < 0 || i >= getSizeInventory()) {
			return null;				
		}else if (inventoryStacks[i] != null) {
            if (inventoryStacks[i].stackSize <= j) {
                ItemStack itemstack = inventoryStacks[i];
                inventoryStacks[i] = null;
                markDirty();
                return itemstack;
            }

            ItemStack ret = inventoryStacks[i].splitStack(j);

            if (inventoryStacks[i].stackSize == 0){
                inventoryStacks[i] = null;
            }

            markDirty();
            return ret;
        } else {
            return null;
        }

    }	
	
	@Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
		if (inventoryStacks == null) {
			if (master != null) {
				master.setInventorySlotContents(i, itemstack);
			}
		}else if (i >= 0 && i < getSizeInventory()) {
			inventoryStacks[i] = itemstack;

			if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
				itemstack.stackSize = getInventoryStackLimit();
			}

			markDirty();
		}
    }


	@Override
    public String getInventoryName() {
        return "container.assembler_upgrade";
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
	public ItemStack getStackInSlotOnClosing(int i) {
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
	public void markDirty() {
		if (effects != null && inventoryEffect != null) {
            inventoryEffect.onInventoryChanged();
		}
	}
	
	@Override
    public boolean isItemValidForSlot(int slot, ItemStack item) {
		if (effects != null && inventoryEffect != null) {
		    return inventoryEffect.isItemValid(slot, item);
		}

        return getMaster() != null && getMaster().isItemValidForSlot(slot, item);
    }
	
	
    //slots
	@Override
    public int[] getAccessibleSlotsFromSide(int side) {
		
		if (effects != null && inventoryEffect != null) {
		    return slotsForSide;
		}else if (getMaster() != null) {
			return getMaster().getAccessibleSlotsFromSide(side);
		}else{
		    return new int[0];
        }
    }

    //in
	@Override
    public boolean canInsertItem(int slot, ItemStack item, int side) {
		if (effects != null && inventoryEffect != null) {
		    return isItemValidForSlot(slot, item);
		}

        return getMaster() != null && getMaster().canInsertItem(slot, item, side);
    }

    //out
	@Override
    public boolean canExtractItem(int slot, ItemStack item, int side) {
        return effects != null && inventoryEffect != null || getMaster() != null && getMaster().canExtractItem(slot, item, side);
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
		if (getTank() == null) {
			return null;
		}else{
			return getTank().getFluid();
		}
	}

	@Override
	public int getCapacity() {
		if (getTank() == null) {
			return 0;
		}else{
			return getTank().getCapacity();
		}
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (getTank() == null) {
			return 0;
		}else{
            return getTank().fill(resource, doFill);
		}		
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (getTank() == null) {
			return null;
		}else{
            return getTank().drain(maxDrain, doDrain);
		}	
	}

	@Override
	public int getFluidAmount() {
		return getTank() == null ? 0 : getTank().getFluidAmount();
	}

	@Override
	public FluidTankInfo getInfo() {
		return getTank() == null ? null : getTank().getInfo();
	}

    private Tank getTank() {
        return tankEffect != null ? tankEffect.getTank() : null;
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



    private void init() {
        for (BaseEffect effect : effects) {
            effect.init();
        }
    }


    private static final String NBT_EFFECT = "Effects";
    private void load(NBTTagCompound compound) {

        NBTTagList list = compound.getTagList(NBT_EFFECT, NBTHelper.COMPOUND.getId());
        int len = Math.min(list.tagCount(), effects.size());
        for (int i = 0; i < len; i++){
            effects.get(i).load(compound);
        }
    }

    private void save(NBTTagCompound compound) {
        NBTTagList list = new NBTTagList();
        for (BaseEffect effect : effects) {
            NBTTagCompound effectCompound = new NBTTagCompound();
            effect.save(effectCompound);
            list.appendTag(effectCompound);
        }
        compound.setTag(NBT_EFFECT, list);
    }

    private void update() {
        for (BaseEffect effect : effects) {
            effect.update();
        }
    }

    public void removed() {
        if (effects != null) {
            for (BaseEffect effect : effects) {
                effect.removed();
            }
        }
    }

    public boolean useStandardInterface() {
        return effects == null || interfaceEffect == null;
    }

    public List<BaseEffect> getEffects() {
        return effects;
    }

    public InterfaceEffect getInterfaceEffect() {
        return interfaceEffect;
    }

    public InventoryEffect getInventoryEffect() {
        return inventoryEffect;
    }
}