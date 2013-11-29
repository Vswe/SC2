package vswe.stevescarts.Upgrades;

import java.util.ArrayList;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Items.Items;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Containers.ContainerCartAssembler;
import vswe.stevescarts.Helpers.TransferHandler;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.ModuleData.ModuleDataHull;
import vswe.stevescarts.Slots.SlotAssemblerFuel;
import vswe.stevescarts.Slots.SlotModule;
import vswe.stevescarts.TileEntities.TileEntityCartAssembler;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;

public class InputChest extends SimpleInventoryEffect {


	public InputChest(int inventoryWidth, int inventoryHeight) {
		super(inventoryWidth, inventoryHeight);
	}
	
	@Override
	public String getName() {
		return "Input Chest with " + getInventorySize() + " slots";
	}
	
	
	@Override
	public void init(TileEntityUpgrade upgrade) {
		upgrade.getCompound().setByte("TransferCooldown", (byte)0);	
	}

	@Override
	public Class<? extends Slot> getSlot(int i) {
		return SlotModule.class;
	}		
		

	
	@Override
	public void update(TileEntityUpgrade upgrade) {
		if (!upgrade.worldObj.isRemote && upgrade.getMaster() != null) {
			NBTTagCompound comp = upgrade.getCompound();
			
			if (comp.getByte("TransferCooldown") != 0) {
				comp.setByte("TransferCooldown", (byte)(comp.getByte("TransferCooldown") - 1));
			}else{
				comp.setByte("TransferCooldown", (byte)20);

				for (int slotId = 0; slotId < upgrade.getUpgrade().getInventorySize(); slotId++) {
				
					ItemStack itemstack = upgrade.getStackInSlot(slotId);
					if (itemstack != null) {
					
						ModuleData module = Items.modules.getModuleData(itemstack);
						if (module == null) {
							continue;
						}		

						if (!isValidForBluePrint(upgrade.getMaster(), module)) {
							continue;
						}						
						
						if(willInvalidate(upgrade.getMaster(), module)) {
							continue;
						}
					
						int stackSize = itemstack.stackSize;
					
						TransferHandler.TransferItem(
									itemstack, 
									upgrade.getMaster(), 
									new ContainerCartAssembler(null,upgrade.getMaster()),
									Slot.class,
									SlotAssemblerFuel.class,
									1
									);
					
								
						if (itemstack.stackSize == 0) {
							upgrade.setInventorySlotContents(slotId, null);
						}

						if (stackSize != itemstack.stackSize) {
							break;
						}
					}				
				
				}
				
			}
		}		
	}
	
	private boolean willInvalidate(TileEntityCartAssembler assembler, ModuleData module) {
		ModuleDataHull hull = assembler.getHullModule();
		if (hull == null) {
			return false;
		}
		
		ArrayList<ModuleData> modules = assembler.getNonHullModules();
		modules.add(module);
		
		if (ModuleData.checkForErrors(hull, modules) != null) {
			return true;
		}	
		
		return false;
	}
	

	private boolean isValidForBluePrint(TileEntityCartAssembler assembler, ModuleData module) {
		for (TileEntityUpgrade tile : assembler.getUpgradeTiles()) {
			for (BaseEffect effect : tile.getUpgrade().getEffects()) {
				if (effect instanceof Blueprint) {
					return ((Blueprint)effect).isValidForBluePrint(tile, assembler.getModules(true), module);	
				}
			}
		}
		return true;
	}
	
	

}