package vswe.stevesvehicles.old.TileEntities;
import java.util.ArrayList;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.old.Helpers.*;
import vswe.stevesvehicles.old.Items.ModItems;
import vswe.stevesvehicles.container.ContainerBase;
import vswe.stevesvehicles.old.Containers.ContainerCargo;
import vswe.stevesvehicles.old.Containers.ContainerManager;
import vswe.stevesvehicles.old.Helpers.TransferHandler.TRANSFER_TYPE;
import vswe.stevesvehicles.client.interfaces.GuiBase;
import vswe.stevesvehicles.old.Interfaces.GuiCargo;
import vswe.stevesvehicles.old.Slots.ISlotExplosions;
import vswe.stevesvehicles.old.Slots.SlotArrow;
import vswe.stevesvehicles.old.Slots.SlotBridge;
import vswe.stevesvehicles.old.Slots.SlotBuilder;
import vswe.stevesvehicles.old.Slots.SlotCake;
import vswe.stevesvehicles.old.Slots.SlotCargo;
import vswe.stevesvehicles.old.Slots.SlotChest;
import vswe.stevesvehicles.old.Slots.SlotFertilizer;
import vswe.stevesvehicles.old.Slots.SlotFirework;
import vswe.stevesvehicles.old.Slots.SlotFuel;
import vswe.stevesvehicles.old.Slots.SlotMilker;
import vswe.stevesvehicles.old.Slots.SlotSapling;
import vswe.stevesvehicles.old.Slots.SlotSeed;
import vswe.stevesvehicles.old.Slots.SlotTorch;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
public class TileEntityCargo extends TileEntityManager
{
    
	@SideOnly(Side.CLIENT)
	@Override
	public GuiBase getGui(InventoryPlayer inv) {
		return new GuiCargo(inv, this);
	}
	
	@Override
	public ContainerBase getContainer(InventoryPlayer inv) {
		return new ContainerCargo(inv, this);		
	}
	public TileEntityCargo()
    {
		super();
    }

	public static ArrayList<CargoItemSelection> itemSelections;

	
	public static void loadSelectionSettings() {
		itemSelections = new ArrayList<CargoItemSelection>();
		itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_ALL, Slot.class, new ItemStack(ModItems.carts, 1, 0)));
		itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_ENGINE, SlotFuel.class, new ItemStack(ModItems.modules, 1, 0)));
		itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_RAILER, SlotBuilder.class, new ItemStack(ModItems.modules, 1, 10)));
		itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_STORAGE, SlotChest.class, new ItemStack(Blocks.chest, 1)));
		itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_TORCHES, SlotTorch.class, new ItemStack(Blocks.torch, 1)));
		itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_EXPLOSIVES, ISlotExplosions.class, ComponentTypes.DYNAMITE.getItemStack()));
		itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_ARROWS, SlotArrow.class, new ItemStack(Items.arrow, 1)));
		itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_BRIDGE, SlotBridge.class, new ItemStack(Blocks.brick_block, 1)));
		itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_SEEDS, SlotSeed.class, new ItemStack(Items.wheat_seeds, 1)));
		itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_FERTILIZER, SlotFertilizer.class, new ItemStack(Items.dye, 1, 15)));
		itemSelections.add(new CargoItemSelection(null, null, null));
		itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_SAPLINGS, SlotSapling.class, new ItemStack(Blocks.sapling, 1)));
		itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_FIREWORK, SlotFirework.class, new ItemStack(Items.fireworks, 1)));
		itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_BUCKETS, SlotMilker.class, new ItemStack(Items.bucket, 1)));
		itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_CAKES, SlotCake.class, new ItemStack(Items.cake, 1)));
	}
	
	
	@Override
    public int getSizeInventory()
    {
        return 60;
    }

	@Override
    public String getInventoryName()
    {
        return "container.cargomanager";
    }

    public int target[] = new int[] {0, 0, 0, 0};
	public ArrayList<SlotCargo> cargoSlots;
	public int lastLayout = -1;
	
	@Override
	protected void updateLayout() {
		if (cargoSlots != null && lastLayout != layoutType) {
			for (SlotCargo slot : cargoSlots) {
				slot.updatePosition();
			}
			lastLayout = layoutType;
		}		
	}	
	
	@Override
	protected boolean isTargetValid(ManagerTransfer transfer) {
		return target[transfer.getSetting()] >= 0 && target[transfer.getSetting()] < itemSelections.size();
	}
	
	@Override
	protected void receiveClickData(int packetid, int id, int dif) {
		if (packetid == 1) {
			target[id] += dif;

			if (target[id] >= itemSelections.size())
			{
				target[id] = 0;
			}
			else if (target[id] < 0)
			{
				target[id] = itemSelections.size() - 1;
			}

			if (color[id] - 1 == getSide())
			{
				reset();
			}
			
			if (itemSelections.get(target[id]).getValidSlot() == null && dif != 0) {
				receiveClickData(packetid, id, dif);
			}
		}				
	}
	

	@Override
	public void checkGuiData(ContainerManager conManager, ICrafting crafting, boolean isNew) {
		super.checkGuiData(conManager, crafting, isNew);
	
		ContainerCargo con = (ContainerCargo)conManager;
	
		short targetShort = (short)0;
		for (int i = 0; i < 4; i++) {
			targetShort |= (target[i] & 15) << (i*4);
		}
		if (isNew || con.lastTarget != targetShort) {
			updateGuiData(con, crafting, 2, targetShort);
			con.lastTarget = targetShort;
		}	
	}
	
	@Override
	public void receiveGuiData(int id, short data) {
		if(id == 2) {
			for (int i = 0; i < 4; i++) {
				target[i] = (data & (15 << (i*4))) >> (i*4);
			}
		}else{
			super.receiveGuiData(id, data);
		}
	}

    public int getAmount(int id)
    {
        int val = getAmountId(id);

        switch (val)
        {
            case 1:
                return 1;

            case 2:
                return 3;

            case 3:
                return 8;

            case 4:
                return 16;

            case 5:
                return 32;

            case 6:
                return 64;

            case 7:
                return 1;

            case 8:
                return 2;

            case 9:
                return 3;

            case 10:
                return 5;

            default:
                return 0;
        }
    }

    //0 - MAX
    //1 - Items
    //2 - Stacks
    public int getAmountType(int id)
    {
        int val = getAmountId(id);

        if (val == 0)
        {
            return 0;
        }
        else if (val <= 6)
        {
            return 1;
        }
        else
        {
            return 2;
        }
    }
	
	@Override
	public int getAmountCount() {
		return 11;
	}
	

	@Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        setWorkload(nbttagcompound.getByte("workload"));		
        for (int i = 0; i < 4; i++)
        {
            target[i] = nbttagcompound.getByte("target" + i);
        }
		
	}
	
	@Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setByte("workload", (byte)getWorkload());
        for (int i = 0; i < 4; i++)
        {
            nbttagcompound.setByte("target" + i, (byte)target[i]);
        }		
	}
	
	@Override
	protected boolean doTransfer(ManagerTransfer transfer) {
		java.lang.Class slotCart = itemSelections.get(target[transfer.getSetting()]).getValidSlot();
		if (slotCart == null) {
			transfer.setLowestSetting(transfer.getSetting() + 1);
			return true;
		}
		java.lang.Class slotCargo = SlotCargo.class;


		IInventory fromInv;
		Container fromCont;
		java.lang.Class fromValid;
		IInventory toInv;
		Container toCont;
		java.lang.Class toValid;

		if (toCart[transfer.getSetting()])
		{
			fromInv = this;
			fromCont = new ContainerCargo(null, this);
			fromValid = slotCargo;
			toInv = transfer.getCart();
			toCont = transfer.getCart().getVehicle().getCon(null);
			toValid = slotCart;
		}
		else
		{
			fromInv = transfer.getCart();
			fromCont = transfer.getCart().getVehicle().getCon(null);
			fromValid = slotCart;
			toInv = this;
			toCont = new ContainerCargo(null, this);
			toValid = slotCargo;
		}

		latestTransferToBeUsed = transfer;
		for (int i = 0; i < fromInv.getSizeInventory(); i++)
		{
			if (TransferHandler.isSlotOfType(fromCont.getSlot(i),fromValid) && fromInv.getStackInSlot(i) != null)
			{
				ItemStack iStack = fromInv.getStackInSlot(i);
				int stacksize = iStack.stackSize;
				int maxNumber;

				if (getAmountType(transfer.getSetting()) == 1)
				{
					maxNumber = getAmount(transfer.getSetting()) - transfer.getWorkload();
				}
				else
				{
					maxNumber = -1;
				}
				
				TransferHandler.TransferItem(iStack, toInv, toCont, toValid, maxNumber, TRANSFER_TYPE.MANAGER);

				if (iStack.stackSize != stacksize)
				{
					if (getAmountType(transfer.getSetting()) == 1)
					{
						transfer.setWorkload(transfer.getWorkload() + stacksize - iStack.stackSize);
					}
					else if (getAmountType(transfer.getSetting()) == 2)
					{
						transfer.setWorkload(transfer.getWorkload() + 1);
					}

					markDirty();
					transfer.getCart().markDirty();

					if (iStack.stackSize == 0)
					{
						fromInv.setInventorySlotContents(i, null);
					}

					if (transfer.getWorkload() >= getAmount(transfer.getSetting()) && getAmountType(transfer.getSetting()) != 0)
					{
						transfer.setLowestSetting(transfer.getSetting() + 1); //this is not available anymore
					}

					return true;
				}
			}
		}
		return false;
	}
	
	@Override
    public boolean isItemValidForSlot(int slotId, ItemStack item)
    {	
		return true;
	}

	private ManagerTransfer latestTransferToBeUsed;
	public ManagerTransfer getCurrentTransferForSlots() {
		return latestTransferToBeUsed;
	}	
	
}
