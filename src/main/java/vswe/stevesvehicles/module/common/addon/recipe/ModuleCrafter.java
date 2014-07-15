package vswe.stevesvehicles.module.common.addon.recipe;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.client.gui.screen.GuiVehicle;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.container.slots.SlotBase;
import vswe.stevesvehicles.container.slots.SlotCartCrafter;
import vswe.stevesvehicles.container.slots.SlotCartCrafterResult;

public class ModuleCrafter extends ModuleRecipe {

	public ModuleCrafter(VehicleBase vehicleBase) {
		super(vehicleBase);
		
		dummy = new CraftingDummy(this);
	}

	
	private CraftingDummy dummy;
	private int cooldown = 0;
	

	@Override
	public void update() {
		if (cooldown <= 0) {
			if (!getVehicle().getWorld().isRemote && getValidSlot() != null) {
				ItemStack result = dummy.getResult();
				
				if (result != null && getVehicle().getModules() != null) {
					if (result.stackSize == 0) {
						result.stackSize = 1;
					}
					
					prepareLists();
					
					if (canCraftMoreOfResult(result)) {
					
						ArrayList<ItemStack> originals = new ArrayList<ItemStack>();
                        for (SlotBase slot : allTheSlots) {
                            ItemStack item = slot.getStack();
                            originals.add(item == null ? null : item.copy());
                        }
						
						ArrayList<ItemStack> containers = new ArrayList<ItemStack>();
						
						boolean valid = true;
						boolean edited = false;
						for (int i = 0; i < 9; i++) {
							ItemStack recipe = getStack(i);
							if (recipe != null) {
								valid = false;
                                for (SlotBase inputSlot : inputSlots) {
                                    ItemStack item = inputSlot.getStack();
                                    if (item != null && item.isItemEqual(recipe) && ItemStack.areItemStackTagsEqual(item, recipe)) {
                                        edited = true;

                                        if (item.getItem().hasContainerItem(item)) {
                                            containers.add(item.getItem().getContainerItem(item));
                                        }

                                        item.stackSize--;
                                        if (item.stackSize <= 0) {
                                            inputSlot.putStack(null);
                                        }

                                        valid = true;
                                        break;
                                    }
                                }
								
								if (!valid) {
									break;
								}
							}
						}
						
						if (valid) {
							getVehicle().addItemToChest(result, getValidSlot(), null);
							
							if (result.stackSize > 0) {
								valid = false;
							}else{
								edited = true;

                                for (ItemStack container : containers) {
                                    if (container != null) {
                                        getVehicle().addItemToChest(container, getValidSlot(), null);
                                        if (container.stackSize > 0) {
                                            valid = false;
                                            break;
                                        }
                                    }
                                }
							}
						}
						
						if (!valid && edited) {
							for (int i = 0; i < allTheSlots.size(); i++) {
								allTheSlots.get(i).putStack(originals.get(i));
							}
						}
					
					}
											
					
				}
			}
			
			cooldown = WORK_COOL_DOWN;
		}else{
			--cooldown;
		}
	}

    private static final int[] AREA = new int[] {68, 44, 16, 16};
	@Override
	protected int[] getArea() {
		return AREA;
	}
	
	@Override
	public boolean hasGui() {
		return true;
	}
	
	@Override
	public int getInventorySize(){
		return 10;
	}
	
	@Override
	public int generateSlots(int slotCount) {
		slotGlobalStart = slotCount;
	
		slotList = new ArrayList<SlotBase>();
		
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				slotList.add(new SlotCartCrafter(getVehicle().getVehicleEntity(), slotCount++, 10 + 18*x, 15 + 18 * y));
			}
		}
		slotList.add(new SlotCartCrafterResult(getVehicle().getVehicleEntity(), slotCount++, 67, canUseAdvancedFeatures() ? 20 : 33));


		return slotCount;
	}	
	
	@Override
	public void onInventoryChanged() {
		if (getVehicle().getWorld().isRemote) {
			dummy.update();
		}
	}
	
	@Override
	public void drawForeground(GuiVehicle gui) {
		super.drawForeground(gui);
	    drawString(gui, getModuleName(), 8, 6, 0x404040);
	}		
	
	@Override
	public int guiWidth() {
		return canUseAdvancedFeatures() ? 120  : 95;
	}
	
	@Override
	public int guiHeight() {
		return 75;
	}


	@Override
	protected boolean canUseAdvancedFeatures() {
		return false;
	}
	
	@Override
	protected int getLimitStartX() {
		return 90;
	}
	
	@Override
	protected int getLimitStartY() {
		return 23;
	}
	
}

