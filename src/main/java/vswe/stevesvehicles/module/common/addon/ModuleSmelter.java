package vswe.stevesvehicles.module.common.addon;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.client.gui.GuiVehicle;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.container.slots.SlotBase;
import vswe.stevesvehicles.container.slots.SlotCartCrafterResult;
import vswe.stevesvehicles.container.slots.SlotFurnaceInput;

public class ModuleSmelter extends ModuleRecipe {

	public ModuleSmelter(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	
	private int energyBuffer;
	private int cooldown = 0;
    private static final int ENERGY_BUFFER_SIZE = 10;
	
	
	@Override
	public void update() {
		if (getVehicle().getWorld().isRemote) {
			return;
		}
		
		if (getVehicle().hasFuelForModule() && energyBuffer < ENERGY_BUFFER_SIZE) {
			energyBuffer++;
		}
		
		if (cooldown <= 0) {
			
			if (energyBuffer == ENERGY_BUFFER_SIZE) {
				ItemStack recipe = getStack(0);

				ItemStack result = null;
				if (recipe != null) {
					result = FurnaceRecipes.smelting().getSmeltingResult(recipe);
				}
				if (result != null) {
					result = result.copy();
				}
				
				if (result != null && getVehicle().getModules() != null) {
					
					prepareLists();

					if (canCraftMoreOfResult(result)) {
					
						ArrayList<ItemStack> originals = new ArrayList<ItemStack>();
                        for (SlotBase slot : allTheSlots) {
                            ItemStack item = slot.getStack();
                            originals.add(item == null ? null : item.copy());
                        }


                        for (SlotBase inputSlot : inputSlots) {
                            ItemStack item = inputSlot.getStack();

                            if (item != null && item.isItemEqual(recipe) && ItemStack.areItemStackTagsEqual(item, recipe)) {
                                if (--item.stackSize <= 0) {
                                    inputSlot.putStack(null);
                                }

                                getVehicle().addItemToChest(result, getValidSlot(), null);

                                if (result.stackSize != 0) {
                                    for (int j = 0; j < allTheSlots.size(); j++) {
                                        allTheSlots.get(j).putStack(originals.get(j));
                                    }
                                }else {
                                    energyBuffer = 0;
                                }
                                break;
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
	


	@Override
	public int getConsumption(boolean isMoving) {
		if (energyBuffer < ENERGY_BUFFER_SIZE) {
			return 15;
		}else{
			return super.getConsumption(isMoving);
		}
	}
	
	@Override
	public boolean hasGui() {
		return true;
	}

	@Override
	protected int getInventoryWidth() {
		return 1;
	}
	
	@Override
	protected int getInventoryHeight() {
		return 2;
	}	
	
	@Override
	protected SlotBase getSlot(int slotId, int x, int y) {
		if (y == 0) {
			return new SlotFurnaceInput(getVehicle().getVehicleEntity(), slotId, 10 + 18 * x, 15 + 18 * y);
		}else{
			return new SlotCartCrafterResult(getVehicle().getVehicleEntity(), slotId, 10 + 18 * x, 15 + 18 * y);
		}
	}
	
	@Override
	public int numberOfGuiData() {
		return super.numberOfGuiData() + 1;
	}
	
	@Override
	protected void checkGuiData(Object[] info) {
		super.checkGuiData(info);
		updateGuiData(info, super.numberOfGuiData(), (short)energyBuffer);
	}
	@Override
	public void receiveGuiData(int id, short data) {
		super.receiveGuiData(id, data);
		if (id == super.numberOfGuiData()) {
			energyBuffer = data;
		}
	}
	
	
	@Override
	public void onInventoryChanged() {
		if (getVehicle().getWorld().isRemote) {
			if (getStack(0) != null) {
				setStack(1, FurnaceRecipes.smelting().getSmeltingResult(getStack(0)));
			} else {
				setStack(1, null);
			}
		}
	}
	
	@Override
	public void drawForeground(GuiVehicle gui) {
		super.drawForeground(gui);
	    drawString(gui,getModuleName(), 8, 6, 0x404040);
	}		
	
	@Override
	public int guiWidth() {
		return canUseAdvancedFeatures() ? 100 : 45;
	}

    private static final int[] AREA = new int[] {32, 25, 16, 16};
	@Override
	protected int[] getArea() {
		return AREA;
	}

	@Override
	protected boolean canUseAdvancedFeatures() {
		return false;
	}
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		super.load(tagCompound);
		energyBuffer = tagCompound.getByte("Buffer");
	}
	
	@Override
	protected void save(NBTTagCompound tagCompound) {
		super.save(tagCompound);
		tagCompound.setByte("Buffer", (byte)energyBuffer);
	}
	
	@Override
	protected int getLimitStartX() {
		return 55;
	}
	
	@Override
	protected int getLimitStartY() {
		return 15;
	}	
	
}

