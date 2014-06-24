package vswe.stevescarts.Modules.Addons;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.CraftingDummy;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotCartCrafterResult;
import vswe.stevescarts.Slots.SlotChest;
import vswe.stevescarts.Slots.SlotFurnaceInput;
import vswe.stevescarts.StevesCarts;

public class ModuleSmelter extends ModuleRecipe {

	public ModuleSmelter(MinecartModular cart) {
		super(cart);
	}

	
	private int energyBuffer;
	
	private int cooldown = 0;
	
	
	@Override
	public void update() {
		if (getCart().worldObj.isRemote) {
			return;
		}
		
		if (getCart().hasFuelForModule() && energyBuffer < 10) {
			energyBuffer++;
		}
		
		if (cooldown <= 0) {
			
			if (energyBuffer == 10) {
				ItemStack recipe = getStack(0);

				ItemStack result = null;
				if (recipe != null) {
					result = FurnaceRecipes.smelting().getSmeltingResult(recipe);
				}
				if (result != null) {
					result = result.copy();
				}
				
				if (result != null && getCart().getModules() != null) {
					
					prepareLists();

					if (canCraftMoreOfResult(result)) {
					
						ArrayList<ItemStack> originals = new ArrayList<ItemStack>();
						for (int i = 0; i < allTheSlots.size(); i++) {
							ItemStack item = allTheSlots.get(i).getStack();
							originals.add(item == null ? null : item.copy());
						}
						
						
						for (int i = 0; i < inputSlots.size(); i++) {
							ItemStack item = inputSlots.get(i).getStack();
							
							if (item != null && item.isItemEqual(recipe) && ItemStack.areItemStackTagsEqual(item, recipe)) {
								if (--item.stackSize <= 0) {
									inputSlots.get(i).putStack(null);
								}
								
								getCart().addItemToChest(result, getValidSlot(), null);
		
								if (result.stackSize != 0) {
									for (int j = 0; j < allTheSlots.size(); j++) {
										allTheSlots.get(j).putStack(originals.get(j));
									}							
								}else{
									energyBuffer = 0;
								}
								break;
							}
						}
					}
																				
				}
			}
			
			cooldown = 40;
		}else{
			--cooldown;
		}
	}
	


	@Override
	public int getConsumption(boolean isMoving) {
		if (energyBuffer < 10) {
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
			return new SlotFurnaceInput(getCart(), slotId, 10 + 18*x, 15 + 18 * y);
		}else{
			return new SlotCartCrafterResult(getCart(), slotId, 10 + 18*x, 15 + 18 * y);
		}
	}
	
	@Override
	public int numberOfGuiData() {
		return super.numberOfGuiData() + 1;
	}
	
	@Override
	protected void checkGuiData(Object[] info) {
		super.checkGuiData(info);
		updateGuiData(info, super.numberOfGuiData() + 0, (short)energyBuffer);
	}
	@Override
	public void receiveGuiData(int id, short data) {
		super.receiveGuiData(id, data);
		if (id == super.numberOfGuiData() + 0) {
			energyBuffer = data;
		}
	}
	
	
	@Override
	public void onInventoryChanged() {
		if (getCart().worldObj.isRemote) {
			if (getStack(0) != null) {
				setStack(1, FurnaceRecipes.smelting().getSmeltingResult(getStack(0)));
			} else {
				setStack(1, null);
			}
		}
	}
	
	@Override
	public void drawForeground(GuiMinecart gui) {
		super.drawForeground(gui);
	    drawString(gui,getModuleName(), 8, 6, 0x404040);
	}		
	
	@Override
	public int guiWidth() {
		return canUseAdvancedFeatures() ? 100 : 45;
	}

	@Override
	protected int[] getArea() {
		return new int[] {32, 25, 16, 16};
	}

	@Override
	protected boolean canUseAdvancedFeatures() {
		return false;
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		super.Load(tagCompound, id);
		energyBuffer = tagCompound.getByte(generateNBTName("Buffer",id));
	}
	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		super.Save(tagCompound, id);		
		tagCompound.setByte(generateNBTName("Buffer",id), (byte)energyBuffer);
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

