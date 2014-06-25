package vswe.stevescarts.old.TileEntities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import vswe.stevescarts.old.Helpers.NBTHelper;
import vswe.stevescarts.old.PacketHandler;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.Containers.ContainerManager;
import vswe.stevescarts.old.Helpers.ManagerTransfer;

public abstract class TileEntityManager extends TileEntityBase
    implements IInventory
{
    public TileEntityManager()
    {
        cargoItemStacks = new ItemStack[getSizeInventory()];
        moveTime = 0;
        standardTransferHandler = new ManagerTransfer();
    }

	
	
	@Override
    public ItemStack getStackInSlot(int i)
    {
        return cargoItemStacks[i];
    }

	@Override
    public ItemStack decrStackSize(int i, int j)
    {
        if (cargoItemStacks[i] != null)
        {
            if (cargoItemStacks[i].stackSize <= j)
            {
                ItemStack itemstack = cargoItemStacks[i];
                cargoItemStacks[i] = null;
                markDirty();
                return itemstack;
            }

            ItemStack itemstack1 = cargoItemStacks[i].splitStack(j);

            if (cargoItemStacks[i].stackSize == 0)
            {
                cargoItemStacks[i] = null;
            }

            markDirty();
            return itemstack1;
        }
        else
        {
            return null;
        }
    }

	@Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        cargoItemStacks[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }

        markDirty();
    }

	@Override
    public String getInventoryName()
    {
        return "container.cargomanager";
    }

	@Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }		
	
	@Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items", NBTHelper.COMPOUND.getId());
        cargoItemStacks = new ItemStack[getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
            byte byte0 = nbttagcompound1.getByte("Slot");

            if (byte0 >= 0 && byte0 < cargoItemStacks.length)
            {
                cargoItemStacks[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        moveTime = nbttagcompound.getByte("movetime");
        setLowestSetting(nbttagcompound.getByte("lowestNumber"));
        layoutType = nbttagcompound.getByte("layout");
        byte temp = nbttagcompound.getByte("tocart");
		byte temp2 = nbttagcompound.getByte("doReturn");

        for (int i = 0; i < 4; i++)
        {
            amount[i] = nbttagcompound.getByte("amount" + i);
            color[i] = nbttagcompound.getByte("color" + i);

            if (color[i] == 0)
            {
                color[i] = i + 1;
            }

            toCart[i] = (temp & (1 << i)) != 0;
			doReturn[i] = (temp2 & (1 << i)) != 0;
        }
    }

	@Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setByte("movetime", (byte)moveTime);
        nbttagcompound.setByte("lowestNumber", (byte)getLowestSetting());
        nbttagcompound.setByte("layout", (byte)layoutType);
        byte temp = 0;
		byte temp2 = 0;
        for (int i = 0; i < 4; i++)
        {
            nbttagcompound.setByte("amount" + i, (byte)amount[i]);
            nbttagcompound.setByte("color" + i, (byte)color[i]);

            if (toCart[i])
            {
                temp |= (1 << i);
            }

			if (doReturn[i])
            {
                temp2 |= (1 << i);
            }
        }

        nbttagcompound.setByte("tocart", temp);
		nbttagcompound.setByte("doReturn", temp2);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < cargoItemStacks.length; i++)
        {
            if (cargoItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                cargoItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

	@Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    private ManagerTransfer standardTransferHandler;
    
    public EntityModularCart getCart() {
    	return standardTransferHandler.getCart();
    }   
    
    public void setCart(EntityModularCart cart) {
    	standardTransferHandler.setCart(cart);
    }
	
    public int getSetting() {
    	return standardTransferHandler.getSetting();
    }
    
    public void setSetting(int val) {
    	standardTransferHandler.setSetting(val);
    }   

    public int getSide() {
    	return standardTransferHandler.getSide();
    }
    
    public void setSide(int val) {
    	standardTransferHandler.setSide(val);
   	}    
    
    public int getLastSetting() {
    	return standardTransferHandler.getLastSetting();
    }    
    
    public void setLastSetting(int val) {
    	standardTransferHandler.setLastSetting(val);
    } 
    
    public int getLowestSetting() {
    	return standardTransferHandler.getLowestSetting();
    }    
    
    public void setLowestSetting(int val) {
    	standardTransferHandler.setLowestSetting(val);
    }    
    
    public int getWorkload() {
    	return standardTransferHandler.getWorkload();
    }    
    
    public void setWorkload(int val) {
    	standardTransferHandler.setWorkload(val);
    }    
	
	@Override
    public void updateEntity()
    {	
	
		if (worldObj.isRemote) {
			updateLayout();
			return;
		}
	
        if (getCart() == null || getCart().isDead ||  getSide() < 0 || getSide() > 3 || !getCart().isDisabled())
        {
        	standardTransferHandler.reset();
            return;
        }

        moveTime++;

        if (moveTime >= 24)
        {
            moveTime = 0;

            if (!exchangeItems(standardTransferHandler)) {
           
	            getCart().releaseCart();
				if (doReturn[getSide()]) {
					getCart().turnback();
				}
				standardTransferHandler.reset();
          
            }
        }
    }


	

	public boolean exchangeItems(ManagerTransfer transfer) {
		
        for (transfer.setSetting(transfer.getLowestSetting()); transfer.getSetting() < 4; transfer.setSetting(transfer.getSetting() + 1)) {
            if (color[transfer.getSetting()] - 1 != transfer.getSide()) {
                continue;
            }

            transfer.setLowestSetting(transfer.getSetting());
			if (transfer.getLastSetting() != transfer.getSetting()) {
				transfer.setWorkload(0);
				transfer.setLastSetting(transfer.getSetting());
				return true;
			}
			
			if (!(toCart[transfer.getSetting()] ? transfer.getToCartEnabled() : transfer.getFromCartEnabled()) || !isTargetValid(transfer)) {
				transfer.setLowestSetting(transfer.getSetting() + 1);
				return true;					
			}
			
            if (doTransfer(transfer)) {
				return true;
			}
        }	
        
        return false;
	}
	

	public void sendPacket(int id) {
		sendPacket(id, new byte[0]);
	}
	public void sendPacket(int id, byte data) {
		sendPacket(id, new byte[] {data});
	}
	public void sendPacket(int id, byte[] data) {
		PacketHandler.sendPacket(id,data);
	}

	public void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id == 0) {
			//switching direction of the transfer

			int railID = data[0];
			toCart[railID] = !toCart[railID];

			if (color[railID] - 1 == getSide())
			{
				reset();
			}
		}else if(id == 4) {
			int railID = data[0];
			if (color[railID] != 5) {
				doReturn[color[railID]-1] = !doReturn[color[railID]-1] ;
			}
		}else if(id == 5) {
			int difference = data[0];

			layoutType += difference;

			if (layoutType > 2)
			{
				layoutType = 0;
			}
			else if (layoutType < 0)
			{
				layoutType = 2;
			}

			reset();
		}else{
			byte railsAndDifferenceCombined = data[0];
			int railID = railsAndDifferenceCombined & 3;

			int k = (railsAndDifferenceCombined & 4) >> 2;
			int difference;
            if (k == 0)
            {
                difference = 1;
            }
            else
            {
                difference = -1;
            }


			if(id == 2) {
				amount[railID] += difference;

				if (amount[railID] >= getAmountCount())
				{
					amount[railID] = 0;
				}
				else if (amount[railID] < 0)
				{
					amount[railID] = getAmountCount() - 1;
				}

				if (color[railID] - 1 == getSide())
				{
					reset();
				}
			}else if(id == 3) {
				if (color[railID] != 5) {
					boolean willStillExist = false;
					for (int side=0;side<4;side++) {
						if (side != railID && color[railID] == color[side]) {
							willStillExist = true;
							break;
						}
					}
					if (!willStillExist) {
						doReturn[color[railID]-1] = false;
					}
				}
				color[railID] += difference;

				if (color[railID] > 5)
				{
					color[railID] = 1;
				}
				else if (color[railID] < 1)
				{
					color[railID] = 5;
				}

				if (color[railID] - 1 == getSide())
				{
					reset();
				}
			}else{
				receiveClickData(id, railID, difference);
			}
		}
	}

	
	@Override
	public void initGuiData(Container con, ICrafting crafting) {
		checkGuiData((ContainerManager)con, crafting, true);
	}	

	@Override
	public void checkGuiData(Container con, ICrafting crafting) {
		checkGuiData((ContainerManager)con, crafting, false);
	}

	public void checkGuiData(ContainerManager con, ICrafting crafting, boolean isNew) {
		short header = (short)(moveTime & 31);
		header |= (layoutType & 3) << 5;
		
		for (int i = 0; i < 4 ; i++) {
			header |= (toCart[i] ? 1 : 0) << (7+i);		
		}
		for (int i = 0; i < 4 ; i++) {
			header |= (doReturn[i] ? 1 : 0) << (11+i);		
		}	
		
		if (isNew || con.lastHeader != header) {
			updateGuiData(con, crafting, 0,header);
			con.lastHeader = header;
		}
		
		short colorShort = (short)0;
		for (int i = 0; i < 4; i++) {
			colorShort |= (color[i] & 7) << (i*3);
		}
		colorShort |= ((getLastSetting() & 7) << 12);
		if (isNew || con.lastColor != colorShort) {
			updateGuiData(con, crafting, 1, colorShort);
			con.lastColor = colorShort;
		}
		
		short amountShort = (short)0;
		for (int i = 0; i < 4; i++) {
			amountShort |= (amount[i] & 15) << (i*4);
		}
		if (isNew || con.lastAmount != amountShort) {
			updateGuiData(con, crafting, 3, amountShort);
			con.lastAmount = amountShort;
		}			
	}
	
	@Override
	public void receiveGuiData(int id, short data) {	
		if (id == 0) {
			moveTime = (data & 31);
			layoutType = (data & 96) >> 5;
			updateLayout();
			for (int i = 0; i < 4; i++) {
				toCart[i] = (data & (1 << 7+i)) != 0;
			}
			for (int i = 0; i < 4; i++) {
				doReturn[i] = (data & (1 << 11+i)) != 0;
			}				
		}else if(id == 1) {
			for (int i = 0; i < 4; i++) {
				color[i] = (data & (7 << (i*3))) >> (i*3);
			}
			setLastSetting((data & (7 << 12)) >> 12);
		}else if(id == 3) {
			for (int i = 0; i < 4; i++) {
				amount[i] = (data & (15 << (i*4))) >> (i*4);
			}
		}
		
	}


    public int moveProgressScaled(int i)
    {
        return (moveTime * i) / 24;
    }

	@Override
    public void closeInventory()
    {
    }
	@Override
    public void openInventory()
    {
    }
	@Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }

        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    private ItemStack cargoItemStacks[];
    public int layoutType;
    //public int workload;
    public int moveTime;
    public boolean toCart[] =  new boolean[] {true, true, true, true};
	public boolean doReturn[] =  new boolean[] {false, false, false, false};

    public int amount[] = new int[] {0, 0, 0, 0};
    public int color[] = new int[] {1, 2, 3, 4};
    //public int lowestNumber = 0;



    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.cargoItemStacks[par1] != null)
        {
            ItemStack var2 = this.cargoItemStacks[par1];
            this.cargoItemStacks[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }
	
	
	protected void updateLayout() {}
	protected void receiveClickData(int packetid, int id, int dif) {}
	protected abstract boolean isTargetValid(ManagerTransfer transfer);
	protected abstract boolean doTransfer(ManagerTransfer transfer);
	public abstract int getAmountCount();
	
	
	protected void reset() {
		moveTime = 0;
		setWorkload(0);
	}
	
	protected int getAmountId(int id) {
		return amount[id];
	}
	

	
}
