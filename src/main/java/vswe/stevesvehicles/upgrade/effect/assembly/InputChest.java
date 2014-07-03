package vswe.stevesvehicles.upgrade.effect.assembly;

import java.util.ArrayList;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.module.data.ModuleDataItemHandler;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Items.ModItems;
import vswe.stevesvehicles.old.Containers.ContainerCartAssembler;
import vswe.stevesvehicles.old.Helpers.TransferHandler;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.module.data.ModuleDataHull;
import vswe.stevesvehicles.container.slots.SlotAssemblerFuel;
import vswe.stevesvehicles.container.slots.SlotModule;
import vswe.stevesvehicles.old.TileEntities.TileEntityCartAssembler;
import vswe.stevesvehicles.old.TileEntities.TileEntityUpgrade;
import vswe.stevesvehicles.upgrade.IEffectInfo;
import vswe.stevesvehicles.upgrade.Upgrade;
import vswe.stevesvehicles.upgrade.effect.BaseEffect;
import vswe.stevesvehicles.upgrade.effect.util.SimpleInventoryEffect;

public class InputChest extends SimpleInventoryEffect {


	public InputChest(TileEntityUpgrade upgrade, Integer inventoryWidth, Integer inventoryHeight) {
		super(upgrade, inventoryWidth, inventoryHeight);
	}
	

	private int cooldown;

	@Override
	public void init() {
		cooldown = 0;
	}

	@Override
	public Class<? extends Slot> getSlot(int i) {
		return SlotModule.class;
	}		
		

	
	@Override
	public void update() {
		if (!upgrade.getWorldObj().isRemote && upgrade.getMaster() != null) {

			if (cooldown > 0) {
				cooldown--;
			}else{
				cooldown = 20;

				for (int slotId = 0; slotId < getInventorySize(); slotId++) {
				
					ItemStack itemstack = upgrade.getStackInSlot(slotId);
					if (itemstack != null) {
					
						ModuleData module = ModItems.modules.getModuleData(itemstack);
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
		
		if (ModuleDataItemHandler.checkForErrors(hull, modules) != null) {
			return true;
		}	
		
		return false;
	}
	

	private boolean isValidForBluePrint(TileEntityCartAssembler assembler, ModuleData module) {
        for (BaseEffect effect : assembler.getEffects()) {
            if (effect instanceof Blueprint) {
                return ((Blueprint)effect).isValidForBluePrint(assembler.getModules(true), module);
            }
        }

		return true;
	}
	
	

}