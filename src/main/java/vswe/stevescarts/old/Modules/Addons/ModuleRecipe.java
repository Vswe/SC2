package vswe.stevescarts.old.Modules.Addons;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.Helpers.Localization;
import vswe.stevescarts.old.Helpers.ResourceHelper;
import vswe.stevescarts.old.Interfaces.GuiMinecart;
import vswe.stevescarts.vehicles.modules.ModuleBase;
import vswe.stevescarts.old.Slots.SlotBase;
import vswe.stevescarts.old.Slots.SlotChest;
import vswe.stevescarts.old.TileEntities.TileEntityCargo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ModuleRecipe extends ModuleAddon {

	public ModuleRecipe(EntityModularCart cart) {
		super(cart);
		target = 3;
		dirty = true;
		allTheSlots = new ArrayList<SlotBase>();
		outputSlots = new ArrayList<SlotBase>();
	}
	
	
	private int target;
	protected boolean dirty;
	
	protected abstract int getLimitStartX();
	protected abstract int getLimitStartY();

	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiMinecart gui, int x, int y) {
		if (canUseAdvancedFeatures()) {
			int [] area = getArea();		
			
			ResourceHelper.bindResource("/gui/recipe.png");
			
			drawImage(gui, area[0] - 2, area[1] - 2, 0, 0, 20, 20);
			
			if (mode == 1) {
				for (int i = 0; i < 3; i++) {
					drawControlRect(gui, x, y, i);
				}
			}else{
				drawControlRect(gui, x, y, 1);
			}
		}	
	}
	
	private void drawControlRect(GuiMinecart gui, int x, int y, int i) {
		int v = i * 11;
		int[] rect = getControlRect(i);
		
		drawImage(gui, rect, 20 + (inRect(x, y, rect) ? 22 : 0), v);		
	}
	
	private int[] getControlRect(int i) {
		return new int[] {getLimitStartX(), getLimitStartY() + 12 * i, 22, 11};		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawForeground(GuiMinecart gui) {
		if (canUseAdvancedFeatures()) {
			String str;
			
			switch(mode) {
				case 0:
					str = "Inf";
					break;
				case 1:
					str = String.valueOf(maxItemCount);
					break;
				default:
					str = "X";
			}	
			
			drawString(gui, str, getControlRect(1), 0x404040);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackgroundItems(GuiMinecart gui, int x, int y) {
		if (canUseAdvancedFeatures()) {
			ItemStack icon;
			if (isTargetInvalid()) {
				icon = new ItemStack(Items.minecart, 1);
			}else{
				icon = TileEntityCargo.itemSelections.get(target).getIcon();
			}
			int [] area = getArea();		
			drawItemInInterface(gui, icon, area[0], area[1]);
		}	
	}
	
	private boolean isTargetInvalid() {
		return target < 0 || target >= TileEntityCargo.itemSelections.size() ||  TileEntityCargo.itemSelections.get(target).getValidSlot() == null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawMouseOver(GuiMinecart gui, int x, int y) {
		if (canUseAdvancedFeatures()) {
			String str = Localization.MODULES.ADDONS.RECIPE_OUTPUT.translate() + "\n" + Localization.MODULES.ADDONS.CURRENT.translate() + ": ";
			if (isTargetInvalid()) {
				str += Localization.MODULES.ADDONS.INVALID_OUTPUT.translate();
			}else{
				str += TileEntityCargo.itemSelections.get(target).getName();
			}
			drawStringOnMouseOver(gui, str, x, y, getArea());
			
			for (int i = 0; i < 3; i++) {
				
				
				if (i == 1) {
					str = Localization.MODULES.ADDONS.RECIPE_MODE.translate() + "\n" + Localization.MODULES.ADDONS.CURRENT.translate() + ": ";
					switch (mode) {
						case 0:
							str += Localization.MODULES.ADDONS.RECIPE_NO_LIMIT.translate();
							break;
						case 1:
							str += Localization.MODULES.ADDONS.RECIPE_LIMIT.translate();
							break;
						default:
							str += Localization.MODULES.ADDONS.RECIPE_DISABLED.translate();
					}
				}else if (mode != 1){
					str = null;
				}else{
					str = Localization.MODULES.ADDONS.RECIPE_CHANGE_AMOUNT.translate(i == 0 ? "0" : "1") + "\n" + Localization.MODULES.ADDONS.RECIPE_CHANGE_AMOUNT_10.translate() + "\n" + Localization.MODULES.ADDONS.RECIPE_CHANGE_AMOUNT_64.translate();
				}
				
				if (str != null) {
					drawStringOnMouseOver(gui, str, x, y, getControlRect(i));
				}
			}	
		}
	}
	
	protected abstract int[] getArea();

	
	@Override
	public int numberOfGuiData() {
		return canUseAdvancedFeatures() ? 3 : 0;
	}
	
	@Override
	protected void checkGuiData(Object[] info) {
		if (canUseAdvancedFeatures()) {
			updateGuiData(info, 0, (short)target);
			updateGuiData(info, 1, (short)mode);
			updateGuiData(info, 2, (short)maxItemCount);
		}
	}
	
	@Override
	public void receiveGuiData(int id, short data) {
		if (canUseAdvancedFeatures()) {
			if (id == 0) {
				target = data;
			}else if (id == 1) {
				mode = data;
			}else if(id == 2) {
				maxItemCount =  data;
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void mouseClicked(GuiMinecart gui, int x, int y, int button) {
		if (canUseAdvancedFeatures()) {
			if (inRect(x, y, getArea())) {
				sendPacket(0, (byte)button);
			}
			
			for (int i = 0; i < 3; i++) {
				if (mode == 1 || i == 1) {
					if (inRect(x, y, getControlRect(i))) {
						if (i == 1) {
							sendPacket(1, (byte)button);
						}else{
							byte encodedData = (byte)(i == 0 ? 0 : 1);
							if (GuiScreen.isCtrlKeyDown()) {
								encodedData |= 2;
							}else if (GuiScreen.isShiftKeyDown()) {
								encodedData |= 4;
							}
							sendPacket(2, (byte)encodedData);
						}
						break;
					}
				}
			}
		}
	}
	
	@Override 
	protected int numberOfPackets() {
		return canUseAdvancedFeatures() ? 3 : 0;
	}
	
	@Override
	protected void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (canUseAdvancedFeatures()) {
			if	(id == 0) {
				dirty = true;
				changeTarget(data[0] == 0);
			}else if(id == 1) {
				if (data[0] == 0) {
					if (++mode > 2) {
						mode = 0;
					}
				}else{
					if (--mode < 0) {
						mode = 2;
					}	
				}
			}else if(id == 2) {
				

				int dif = (data[0] & 1) == 0 ? 1 : -1;
				
				if ((data[0] & 2) != 0) {
					dif *= 64;
				}else if((data[0] & 4) != 0) {
					dif *= 10;
				}
				
				maxItemCount = Math.min(Math.max(1, maxItemCount + dif), 999);
												
			}
		}
	}
	
	private void changeTarget(boolean up) {
		if (!up) {
			if (--target < 0) {
				target = TileEntityCargo.itemSelections.size() - 1;
			}
		}else if (++target >= TileEntityCargo.itemSelections.size()) {
			target = 0;
		}	
		
		if (isTargetInvalid()) {
			changeTarget(up);
		}
	}
	
	protected abstract boolean canUseAdvancedFeatures();
	
	protected Class getValidSlot() {
		if (isTargetInvalid()) {
			return null;
		}else{
			return TileEntityCargo.itemSelections.get(target).getValidSlot();
		}
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		if (canUseAdvancedFeatures()) {
			target = tagCompound.getByte(generateNBTName("Target",id));
			mode = tagCompound.getByte(generateNBTName("Mode",id));
			maxItemCount = tagCompound.getShort(generateNBTName("MaxItems",id));
		}
	}

	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		if (canUseAdvancedFeatures()) {
			tagCompound.setByte(generateNBTName("Target",id), (byte)target);
			tagCompound.setByte(generateNBTName("Mode",id), (byte)mode);
			tagCompound.setShort(generateNBTName("MaxItems",id), (short)maxItemCount);
		}
	}
	
	protected ArrayList<SlotBase> inputSlots;
	protected ArrayList<SlotBase> outputSlots;	
	protected ArrayList<SlotBase> allTheSlots;	
	
	protected void prepareLists() {
		if (inputSlots == null) {
			inputSlots = new ArrayList<SlotBase>();
			
			for(ModuleBase module : getCart().getModules()) {
				if (module.getSlots() != null) {
			
					for (SlotBase slot : module.getSlots()) {
						if (slot instanceof SlotChest) {
							inputSlots.add(slot);
						}
					}
				}
			}						
		}

		if (dirty) {
			allTheSlots.clear();
			outputSlots.clear();
			Class validSlot = getValidSlot();
			for(ModuleBase module : getCart().getModules()) {
				if (module.getSlots() != null) {
			
					for (SlotBase slot : module.getSlots()) {

						if (validSlot.isInstance(slot)) {
							outputSlots.add(slot);
							allTheSlots.add(slot);
						}else if (slot instanceof SlotChest) {
							allTheSlots.add(slot);
						}
					}
				}
			}						
			dirty = false;
		}		
	}
	
	private int maxItemCount = 1;
	private int mode;
	//0 - infinite
	//1 - limit
	//2 - disabled
	


	
	protected boolean canCraftMoreOfResult(ItemStack result) {
		if (mode == 0) {
			return true;
		}else if(mode == 2) {
			return false;
		}else{
			int count = 0;
			for (int i = 0; i < outputSlots.size(); i++) {
				ItemStack item = outputSlots.get(i).getStack();
				if (item != null && item.isItemEqual(result) && ItemStack.areItemStackTagsEqual(item, result)) {
					count += item.stackSize;
					if (count >= maxItemCount) {
						return false;
					}
				}
			}
			return true;
		}		
	}	
}
