package vswe.stevescarts.old.Modules.Addons;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.client.interfaces.GuiVehicle;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.Helpers.EnchantmentData;
import vswe.stevescarts.old.Helpers.EnchantmentInfo;
import vswe.stevescarts.old.Helpers.EnchantmentInfo.ENCHANTMENT_TYPE;
import vswe.stevescarts.old.Helpers.Localization;
import vswe.stevescarts.old.Helpers.ResourceHelper;
import vswe.stevescarts.old.Slots.SlotBase;
import vswe.stevescarts.old.Slots.SlotEnchantment;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModuleEnchants extends ModuleAddon  {

	public ModuleEnchants(EntityModularCart cart) {
		super(cart);
		enchants = new EnchantmentData[3];
		enabledTypes = new ArrayList<ENCHANTMENT_TYPE>();
	}

	private EnchantmentData[] enchants;
	private ArrayList<ENCHANTMENT_TYPE> enabledTypes;
	
	
	//---------TOOLS---------
	public int getFortuneLevel() {
		if (useSilkTouch()) {
			return 0;
		}
		
		
		return getEnchantLevel(EnchantmentInfo.fortune);
	}
	
	//implemented but doesn't work properly
	public boolean useSilkTouch() {
		return false;
	}
	
	public int getUnbreakingLevel() { 
		return getEnchantLevel(EnchantmentInfo.unbreaking);
	}
	
	public int getEfficiencyLevel() {
		return getEnchantLevel(EnchantmentInfo.efficiency);
	}	
	
	
	
	
	//---------SHOOTERS---------
	public int getPowerLevel() {
		return getEnchantLevel(EnchantmentInfo.power);
	}

	public int getPunchLevel() {
		return getEnchantLevel(EnchantmentInfo.punch);
	}

	public boolean useFlame() {
		return getEnchantLevel(EnchantmentInfo.flame) > 0;
	}

	public boolean useInfinity() {
		return getEnchantLevel(EnchantmentInfo.infinity) > 0;
	}
	
	@Override
	public boolean hasGui() {
		return true;
	}
	

	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, getModuleName(), 8, 6, 0x404040);
	}	
	
	@Override
	protected int getInventoryWidth() {
		return 1;
	}
	
	@Override
	protected int getInventoryHeight() {
		return 3;
	}
	
	@Override
	protected SlotBase getSlot(int slotId, int x, int y) {
		return new SlotEnchantment(getCart(), enabledTypes, slotId,8 , 14 + y * 20);
	}
	

	@Override
	public void update() {
		super.update();
		
		
		if (!getCart().worldObj.isRemote) {
			for (int i = 0; i < 3; i++) {
				if (getStack(i) != null && getStack(i).stackSize > 0) {
										
					int stacksize = getStack(i).stackSize;
					enchants[i] = EnchantmentInfo.addBook(enabledTypes, enchants[i], getStack(i));
					if (getStack(i).stackSize != stacksize) {
						boolean valid = true;
						for (int j = 0; j < 3; j++) {
							if (i != j) {
								if (enchants[i] != null && enchants[j] != null && enchants[i].getEnchantment() == enchants[j].getEnchantment()) {
									enchants[i] = null;
									getStack(i).stackSize += 1;
									valid = false;
									break;
								}
							}
						}
						if (valid && getStack(i).stackSize <= 0) {
							setStack(i, null);									
						}
					}
				}
			}
			
		}
	}
	
	public void damageEnchant(ENCHANTMENT_TYPE type, int dmg) {
		for (int i = 0; i < 3; i++) {
			if (enchants[i] != null && enchants[i].getEnchantment().getType() == type) {
				enchants[i].damageEnchant(dmg);
				if (enchants[i].getValue() <= 0) {
					enchants[i] = null;
				}
			}
		}		
	}
	
	private int getEnchantLevel(EnchantmentInfo info) {
		if (info != null) {
			for (int i = 0; i < 3; i++) {
				if (enchants[i] != null && enchants[i].getEnchantment() == info) {
					return enchants[i].getLevel();
				}
			}				
		}
		
		return 0;
	}
	
			
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource("/gui/enchant.png");
		
		for (int i = 0; i < 3; i++) {
			int[] box = getBoxRect(i);
			
			if (inRect(x, y, box)) {
				drawImage(gui, box, 65, 0);
			}else{
				drawImage(gui, box, 0, 0);
			}
			
			EnchantmentData data = enchants[i];
			if (data != null) {
				int maxlevel = data.getEnchantment().getEnchantment().getMaxLevel();
				int value = data.getValue();
				for (int j = 0; j < maxlevel; j++) {
					int[] bar = getBarRect(i, j, maxlevel);
					if (j != maxlevel - 1) {
						drawImage(gui, bar[0] + bar[2], bar[1], 61 + j, 1, 1, bar[3]);
					}
					
					int levelmaxvalue = data.getEnchantment().getValue(j + 1);
					if (value > 0) {
						float mult = (float)value / levelmaxvalue;
						if (mult > 1) {
							mult = 1;
						}
						bar[2] *= mult;
						drawImage(gui, bar, 1, 13 + 11 * j);	
					}
					value -= levelmaxvalue;
				}
			}
		}
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		for (int i = 0; i < 3; i++) {
			EnchantmentData data = enchants[i];
			String str;
			
			if (data != null) {
				str = data.getInfoText();
			}else{
				str = Localization.MODULES.ADDONS.ENCHANT_INSTRUCTION.translate();
			}
			
			
			drawStringOnMouseOver(gui, str, x, y, getBoxRect(i));
		}
	}
	
	private int[] getBoxRect(int id) {
		return new int[] {40, 17 + id * 20, 61, 12};
	}
	
	private int[] getBarRect(int id, int barid, int maxlevel) {
		int width = (59 - (maxlevel - 1)) / maxlevel;
		return new int[] {41 + (width + 1) * barid, 18 + id * 20, width, 10};
	}	
	

	@Override
	public int numberOfGuiData() {
		return 9;
	}	
	
	@Override
	protected void checkGuiData(Object[] info) {
		for (int i = 0; i < 3; i++) {
			EnchantmentData data = enchants[i];
			if (data == null) {
				updateGuiData(info, i * 3 + 0, (short)(-1));
			}else{
				updateGuiData(info, i * 3 + 0, (short)(data.getEnchantment().getEnchantment().effectId));		
				updateGuiData(info, i * 3 + 1, (short)(data.getValue() & 65535));
				updateGuiData(info, i * 3 + 2, (short)((data.getValue() >> 16) & 65535));
			}
		}
	}
	
	
	
	@Override
	public void receiveGuiData(int id, short data) {	
		int dataint = data;
		if (dataint < 0) {
			dataint += 65536;
		}
		
		int enchantId = id / 3;
		id %= 3;
		
		if(id == 0) {
			if (data == -1) {
				enchants[enchantId] = null;
			}else{
				enchants[enchantId] = EnchantmentInfo.createDataFromEffectId(enchants[enchantId], data);
			}
		}else if(enchants[enchantId] != null) {
			if (id == 1) {
				enchants[enchantId].setValue(((enchants[enchantId].getValue() & -65536) | dataint));
			}else if (id == 2) {
				enchants[enchantId].setValue(((enchants[enchantId].getValue() & 65535) | (dataint << 16)));
			}			
		}
		

	}
	
	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		super.Save(tagCompound, id);
		for (int i = 0; i < 3; i++) {
			if (enchants[i] == null) {
				tagCompound.setShort(generateNBTName("EffectId" + i,id), (short)-1);	
			}else{
				tagCompound.setShort(generateNBTName("EffectId" + i,id), (short)enchants[i].getEnchantment().getEnchantment().effectId);	
				tagCompound.setInteger(generateNBTName("Value" + i,id), enchants[i].getValue());
			}
		}
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		super.Load(tagCompound, id);
		for (int i = 0; i < 3; i++) {
			short effect = (tagCompound.getShort(generateNBTName("EffectId" + i,id)));		
			if (effect == -1) {
				enchants[i] = null;
			}else{
				enchants[i] = EnchantmentInfo.createDataFromEffectId(enchants[i], effect);
				if (enchants[i] != null) {
					enchants[i].setValue(tagCompound.getInteger(generateNBTName("Value" + i,id)));
				}
			}
		}
	}
	
	@Override
	public int guiWidth() {
		return 110;
	}

	public void addType(ENCHANTMENT_TYPE type) {
		enabledTypes.add(type);
	}
}
