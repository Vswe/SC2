package vswe.stevesvehicles.old.Upgrades;

import java.util.ArrayList;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Items.ModItems;
import vswe.stevesvehicles.old.Containers.ContainerCartAssembler;
import vswe.stevesvehicles.old.Helpers.TransferHandler;
import vswe.stevesvehicles.modules.data.ModuleData;
import vswe.stevesvehicles.old.Slots.SlotCart;
import vswe.stevesvehicles.old.Slots.SlotModule;
import vswe.stevesvehicles.old.TileEntities.TileEntityCartAssembler;
import vswe.stevesvehicles.old.TileEntities.TileEntityUpgrade;

public class Disassemble extends InventoryEffect {


	public Disassemble() {
		super();
	}
	
	@Override
	public int getInventorySize() {
		return 31;
	}
	@Override
	public int getSlotX(int id) {
		if (id == 0) {
			return ((256 - 18) * 3) / 4;
		}else{
			return (256 - 18 * 10) / 2 + ((id-1) % 10)* 18;
		}
	}
	
	@Override	
	public int getSlotY(int id) {
		int y;
		if (id == 0) {
			y = 0;
		}else{
			y = (id - 1) / 10 + 2; 
		}
		return (107 - 18 * 5) / 2 + y * 18;
	}
	
	@Override
	public Class<? extends Slot> getSlot(int i) {
		if (i == 0) {
			return SlotCart.class;
		}else{
			return SlotModule.class;
		}
	}
	
	@Override
	public void load(TileEntityUpgrade upgrade, NBTTagCompound compound) {
		setLastCart(upgrade, upgrade.getStackInSlot(0));
	}
	
	@Override
	public String getName() {
		return Localization.UPGRADES.DISASSEMBLE.translate();
	}
	
	@Override
	public void onInventoryChanged(TileEntityUpgrade upgrade) {

		ItemStack cart = upgrade.getStackInSlot(0);
		if (!updateCart(upgrade, cart)) {
			boolean needsToPuke = true;
			for (int i = 1; i < getInventorySize(); i++) {
				if (upgrade.getStackInSlot(i) == null) {
					ItemStack item = upgrade.getStackInSlot(0);
					upgrade.setInventorySlotContents(0, null);
					upgrade.setInventorySlotContents(i, item);
					needsToPuke = false;
					break;
				}
			}
		
			if (needsToPuke) {
				if ( !upgrade.getWorldObj().isRemote) {
					upgrade.getMaster().puke(upgrade.getStackInSlot(0).copy());
				}
				upgrade.setInventorySlotContents(0, null);
			}
			
			
		}
	}
	
	@Override
	public void removed(TileEntityUpgrade upgrade) {
		updateCart(upgrade, null);
	}	
	
	private void resetMaster(TileEntityCartAssembler master, boolean full) {
		for (int i = 0; i < master.getSizeInventory() - master.nonModularSlots(); i++) {
			if (master.getStackInSlot(i) != null) {
				if (master.getStackInSlot(i).stackSize <= 0) {
					master.setInventorySlotContents(i, null);
				}else if(full) {
					if (!master.getWorldObj().isRemote) {
						master.puke(master.getStackInSlot(i).copy());
					}
					master.setInventorySlotContents(i, null);
				}
			}
		}		
	}
	
	private void setLastCart(TileEntityUpgrade upgrade, ItemStack cart) {
		if (cart == null) {
			upgrade.getCompound().setShort("id", (short)0);
		}else{
			cart.writeToNBT(upgrade.getCompound());
		}
	}
	
	private ItemStack getLastCart(TileEntityUpgrade upgrade) {
		return ItemStack.loadItemStackFromNBT(upgrade.getCompound());
	}

	private boolean updateCart(TileEntityUpgrade upgrade, ItemStack cart) {
		if (upgrade.getMaster() != null) {
			if (cart == null || cart.getItem() != ModItems.carts || cart.getTagCompound() == null || cart.getTagCompound().hasKey("maxTime")) {
				resetMaster(upgrade.getMaster(), false);
				setLastCart(upgrade,null);
				if (cart != null) {
					upgrade.getMaster().puke(cart);
					upgrade.setInventorySlotContents(0, null);
				}
			}else{
				ItemStack last = getLastCart(upgrade);
				setLastCart(upgrade, cart);
							
				int result = canDisassemble(upgrade);
				boolean reset = false;
				if (result > 0 && last != null && !ItemStack.areItemStacksEqual(cart,  last)) {
					result = 2;
					reset = true;
				}
				
				if (result != 2) {
					return result == 1 && upgrade.getMaster().getStackInSlot(0) != null;
				}
				
				if (reset) {
					resetMaster(upgrade.getMaster(), true);					
				}

						
				boolean addedHull = false;
				ArrayList<ItemStack> modules = ModuleData.getModularItems(cart); //TODO
				for (ItemStack item : modules) {
					item.stackSize = 0;	
					
					TransferHandler.TransferItem(
								item, 
								upgrade.getMaster(), 
								new ContainerCartAssembler(null,upgrade.getMaster()),
								1
								);	
												

					if (!addedHull) {
						addedHull = true;
						upgrade.getMaster().updateSlots();
					}
				}
			}
		}
		return true;
	}
	
	public int canDisassemble(TileEntityUpgrade upgrade) {
		int disassembleCount = 0;
		
		for (BaseEffect effect : upgrade.getMaster().getEffects()) {
			if (effect instanceof Disassemble) {
				disassembleCount++;
			}
		}
		
		if (disassembleCount != 1) {
			return 0;
		}
	
	
		for (int i = 0; i < upgrade.getMaster().getSizeInventory() - upgrade.getMaster().nonModularSlots(); i++) {
			if (upgrade.getMaster().getStackInSlot(i) != null && upgrade.getMaster().getStackInSlot(i).stackSize <= 0) {
				return 1;
			}
		}
		for (int i = 0; i < upgrade.getMaster().getSizeInventory() - upgrade.getMaster().nonModularSlots(); i++) {
			if (upgrade.getMaster().getStackInSlot(i) != null) {
				return 0;
			}
		}		
		return 2;
	}
	
}