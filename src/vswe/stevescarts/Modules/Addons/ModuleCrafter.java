package vswe.stevescarts.Modules.Addons;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.CraftingDummy;
import vswe.stevescarts.Helpers.TransferHandler;
import vswe.stevescarts.Helpers.TransferHandler.TRANSFER_TYPE;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotCartCrafter;
import vswe.stevescarts.Slots.SlotCartCrafterResult;
import vswe.stevescarts.Slots.SlotChest;

public class ModuleCrafter extends ModuleRecipe {

	public ModuleCrafter(MinecartModular cart) {
		super(cart);
		
		dummy = new CraftingDummy(this);

	}

	
	
	
	private CraftingDummy dummy;

	
	private int cooldown = 0;
	

	@Override
	public void update() {
		if (cooldown <= 0) {
			if (!getCart().worldObj.isRemote && getValidSlot() != null) {
				ItemStack result = dummy.getResult();
				
				if (result != null && getCart().getModules() != null) {
					if (result.stackSize == 0) {
						result.stackSize = 1;
					}
					
					prepareLists();
					
					if (canCraftMoreOfResult(result)) {
					
						ArrayList<ItemStack> originals = new ArrayList<ItemStack>();
						for (int i = 0; i < allTheSlots.size(); i++) {
							ItemStack item = allTheSlots.get(i).getStack();
							originals.add(item == null ? null : item.copy());
						}
						
						ArrayList<ItemStack> containers = new ArrayList<ItemStack>();
						
						boolean valid = true;
						boolean edited = false;
						for (int i = 0; i < 9; i++) {
							ItemStack recipe = getStack(i);
							if (recipe == null) {
								continue;
							}else{
								valid = false;
								for (int j = 0; j < inputSlots.size(); j++) {
									ItemStack item = inputSlots.get(j).getStack();
									if (item != null && item.isItemEqual(recipe) && ItemStack.areItemStackTagsEqual(item, recipe)) {
										edited = true;
										
										if (item.getItem().hasContainerItem()) {
											containers.add(item.getItem().getContainerItemStack(item));
										}
										
										item.stackSize--;
										if (item.stackSize <= 0) {
											inputSlots.get(j).putStack(null);
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
							getCart().addItemToChest(result, getValidSlot(), null);
							
							if (result.stackSize > 0) {
								valid = false;
							}else{
								edited = true;
								
								for (int i = 0; i < containers.size(); i++) {
									ItemStack container = containers.get(i);
									
									if (container != null) {
										getCart().addItemToChest(container, getValidSlot(), null);
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
			
			cooldown = 40;
		}else{
			--cooldown;
		}
	}
	
	@Override
	protected int[] getArea() {
		return new int[] {68, 44, 16, 16};
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
				slotList.add(new SlotCartCrafter(getCart(), slotCount++, 10 + 18*x, 15 + 18 * y));
			}
		}
		slotList.add(new SlotCartCrafterResult(getCart(), slotCount++, 67, canUseAdvancedFeatures() ? 20 : 33));


		return slotCount;
	}	
	
	@Override
	public void onInventoryChanged() {
		if (getCart().worldObj.isRemote) {
			dummy.update();
		}
	}
	
	@Override
	public void drawForeground(GuiMinecart gui) {
		super.drawForeground(gui);
	    drawString(gui,"Crafter", 8, 6, 0x404040);
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

