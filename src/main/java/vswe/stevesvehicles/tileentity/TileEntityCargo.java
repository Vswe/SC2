package vswe.stevesvehicles.tileentity;
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
import vswe.stevesvehicles.localization.entry.block.LocalizationCargo;
import vswe.stevesvehicles.network.DataReader;
import vswe.stevesvehicles.old.Helpers.*;
import vswe.stevesvehicles.item.ModItems;
import vswe.stevesvehicles.container.ContainerBase;
import vswe.stevesvehicles.container.ContainerCargo;
import vswe.stevesvehicles.container.ContainerManager;
import vswe.stevesvehicles.client.gui.screen.GuiBase;
import vswe.stevesvehicles.client.gui.screen.GuiCargo;
import vswe.stevesvehicles.container.slots.ISlotExplosions;
import vswe.stevesvehicles.container.slots.SlotArrow;
import vswe.stevesvehicles.container.slots.SlotBridge;
import vswe.stevesvehicles.container.slots.SlotBuilder;
import vswe.stevesvehicles.container.slots.SlotCake;
import vswe.stevesvehicles.container.slots.SlotCargo;
import vswe.stevesvehicles.container.slots.SlotChest;
import vswe.stevesvehicles.container.slots.SlotFertilizer;
import vswe.stevesvehicles.container.slots.SlotFirework;
import vswe.stevesvehicles.container.slots.SlotFuel;
import vswe.stevesvehicles.container.slots.SlotMilker;
import vswe.stevesvehicles.container.slots.SlotSapling;
import vswe.stevesvehicles.container.slots.SlotSeed;
import vswe.stevesvehicles.container.slots.SlotTorch;
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



	public static ArrayList<CargoItemSelection> itemSelections;


    //TODO maybe make this registration a bit nice
	public static void loadSelectionSettings() {
		itemSelections = new ArrayList<CargoItemSelection>();
		itemSelections.add(new CargoItemSelection(LocalizationCargo.SLOT_ALL, Slot.class, new ItemStack(ModItems.vehicles, 1, 0)));
		itemSelections.add(new CargoItemSelection(LocalizationCargo.SLOT_ENGINE, SlotFuel.class, new ItemStack(ModItems.modules, 1, 0)));       //TODO don't use static ids for modules
		itemSelections.add(new CargoItemSelection(LocalizationCargo.SLOT_RAILER, SlotBuilder.class, new ItemStack(ModItems.modules, 1, 10))); //TODO don't use static ids for modules
		itemSelections.add(new CargoItemSelection(LocalizationCargo.SLOT_STORAGE, SlotChest.class, new ItemStack(Blocks.chest, 1)));
		itemSelections.add(new CargoItemSelection(LocalizationCargo.SLOT_TORCH, SlotTorch.class, new ItemStack(Blocks.torch, 1)));
		itemSelections.add(new CargoItemSelection(LocalizationCargo.SLOT_EXPLOSIVE, ISlotExplosions.class, ComponentTypes.DYNAMITE.getItemStack()));
		itemSelections.add(new CargoItemSelection(LocalizationCargo.SLOT_ARROW, SlotArrow.class, new ItemStack(Items.arrow, 1)));
		itemSelections.add(new CargoItemSelection(LocalizationCargo.SLOT_BRIDGE, SlotBridge.class, new ItemStack(Blocks.brick_block, 1)));
		itemSelections.add(new CargoItemSelection(LocalizationCargo.SLOT_SEED, SlotSeed.class, new ItemStack(Items.wheat_seeds, 1)));
		itemSelections.add(new CargoItemSelection(LocalizationCargo.SLOT_FERTILIZER, SlotFertilizer.class, new ItemStack(Items.dye, 1, 15)));
		itemSelections.add(new CargoItemSelection(null, null, null));
		itemSelections.add(new CargoItemSelection(LocalizationCargo.SLOT_SAPLING, SlotSapling.class, new ItemStack(Blocks.sapling, 1)));
		itemSelections.add(new CargoItemSelection(LocalizationCargo.SLOT_FIREWORK, SlotFirework.class, new ItemStack(Items.fireworks, 1)));
		itemSelections.add(new CargoItemSelection(LocalizationCargo.SLOT_BUCKET, SlotMilker.class, new ItemStack(Items.bucket, 1)));
		itemSelections.add(new CargoItemSelection(LocalizationCargo.SLOT_CAKE, SlotCake.class, new ItemStack(Items.cake, 1)));
	}
	
	
	@Override
    public int getSizeInventory() {
        return 60;
    }

	@Override
    public String getInventoryName() {
        return "container.cargo_manager";
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
	protected void receivePacket(PacketId id, DataReader dr) {
		if (id == PacketId.VEHICLE_PART) {
            int railId = dr.readByte();
            int dif = dr.readBoolean() ? 1 : -1;

			do {
                target[railId] += dif;

                if (target[railId] >= itemSelections.size()) {
                    target[railId] = 0;
                }else if (target[railId] < 0) {
                    target[railId] = itemSelections.size() - 1;
                }
			}while (itemSelections.get(target[railId]).getValidSlot() == null);

            if (color[railId] - 1 == getSide()) {
                reset();
            }
		}else{
            super.receivePacket(id, dr);
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

    public int getAmount(int id) {
        int val = getAmountId(id);

        switch (val) {
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
    public int getAmountType(int id) {
        int val = getAmountId(id);

        if (val == 0) {
            return 0;
        }else if (val <= 6) {
            return 1;
        }else {
            return 2;
        }
    }
	
	@Override
	public int getAmountCount() {
		return 11;
	}

	@Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        setWorkload(nbttagcompound.getByte("workload"));		
        for (int i = 0; i < 4; i++) {
            target[i] = nbttagcompound.getByte("target" + i);
        }
	}
	
	@Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setByte("workload", (byte)getWorkload());
        for (int i = 0; i < 4; i++) {
            nbttagcompound.setByte("target" + i, (byte)target[i]);
        }		
	}
	
	@Override
	protected boolean doTransfer(ManagerTransfer transfer) {
		Class slotCart = itemSelections.get(target[transfer.getSetting()]).getValidSlot();
		if (slotCart == null) {
			transfer.setLowestSetting(transfer.getSetting() + 1);
			return true;
		}
		Class slotCargo = SlotCargo.class;


		IInventory fromInv;
		Container fromCont;
		Class fromValid;
		IInventory toInv;
		Container toCont;
		Class toValid;

		if (toCart[transfer.getSetting()]) {
			fromInv = this;
			fromCont = new ContainerCargo(null, this);
			fromValid = slotCargo;
			toInv = transfer.getCart();
			toCont = transfer.getCart().getVehicle().getCon(null);
			toValid = slotCart;
		}else {
			fromInv = transfer.getCart();
			fromCont = transfer.getCart().getVehicle().getCon(null);
			fromValid = slotCart;
			toInv = this;
			toCont = new ContainerCargo(null, this);
			toValid = slotCargo;
		}

		latestTransferToBeUsed = transfer;
		for (int i = 0; i < fromInv.getSizeInventory(); i++) {
			if (TransferHandler.isSlotOfType(fromCont.getSlot(i),fromValid) && fromInv.getStackInSlot(i) != null) {
				ItemStack iStack = fromInv.getStackInSlot(i);
				int stackSize = iStack.stackSize;
				int maxNumber;

				if (getAmountType(transfer.getSetting()) == 1) {
					maxNumber = getAmount(transfer.getSetting()) - transfer.getWorkload();
				}else {
					maxNumber = -1;
				}
				
				TransferHandler.TransferItem(iStack, toInv, toCont, toValid, maxNumber, TransferHandler.TransferType.MANAGER);

				if (iStack.stackSize != stackSize) {
					if (getAmountType(transfer.getSetting()) == 1) {
						transfer.setWorkload(transfer.getWorkload() + stackSize - iStack.stackSize);
					}else if (getAmountType(transfer.getSetting()) == 2) {
						transfer.setWorkload(transfer.getWorkload() + 1);
					}

					markDirty();
					transfer.getCart().markDirty();

					if (iStack.stackSize == 0) {
						fromInv.setInventorySlotContents(i, null);
					}

					if (transfer.getWorkload() >= getAmount(transfer.getSetting()) && getAmountType(transfer.getSetting()) != 0) {
						transfer.setLowestSetting(transfer.getSetting() + 1); //this is not available anymore
					}

					return true;
				}
			}
		}
		return false;
	}
	
	@Override
    public boolean isItemValidForSlot(int slotId, ItemStack item) {
		return true;
	}

	private ManagerTransfer latestTransferToBeUsed;
	public ManagerTransfer getCurrentTransferForSlots() {
		return latestTransferToBeUsed;
	}	
	
}
