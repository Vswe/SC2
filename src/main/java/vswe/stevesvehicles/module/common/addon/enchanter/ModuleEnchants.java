package vswe.stevesvehicles.module.common.addon.enchanter;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.client.gui.screen.GuiVehicle;
import vswe.stevesvehicles.localization.entry.module.LocalizationUtility;
import vswe.stevesvehicles.module.common.addon.ModuleAddon;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.container.slots.SlotBase;
import vswe.stevesvehicles.container.slots.SlotEnchantment;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModuleEnchants extends ModuleAddon {

	public ModuleEnchants(VehicleBase vehicleBase) {
		super(vehicleBase);
		enchants = new EnchantmentData[3];
		enabledTypes = new ArrayList<EnchantmentInfo.Enchantment_Type>();
	}

	private EnchantmentData[] enchants;
	private ArrayList<EnchantmentInfo.Enchantment_Type> enabledTypes;
	
	
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
		return new SlotEnchantment(getVehicle().getVehicleEntity(), enabledTypes, slotId, 8, 14 + y * 20);
	}
	

	@Override
	public void update() {
		super.update();
		
		
		if (!getVehicle().getWorld().isRemote) {
			for (int i = 0; i < 3; i++) {
				if (getStack(i) != null && getStack(i).stackSize > 0) {
										
					int stackSize = getStack(i).stackSize;
					enchants[i] = EnchantmentInfo.addBook(enabledTypes, enchants[i], getStack(i));
					if (getStack(i).stackSize != stackSize) {
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
	
	public void damageEnchant(EnchantmentInfo.Enchantment_Type type, int dmg) {
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
	
			

    private static final int BOX_SRC_Y = 1;
    private static final int BOX_SRC_X = 1;
    private static final int BOX_HOVER_SRC_X = 71;

    private static final int MARKER_SRC_X = 63;
    private static final int MARKER_SRC_Y = 2;

    private static final int BAR_SRC_X = 1;
    private static final int BAR_SRC_Y = 14;


    private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/gui/enchant.png");

	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource(TEXTURE);
		
		for (int i = 0; i < 3; i++) {
			int[] box = getBoxRect(i);
			
			if (inRect(x, y, box)) {
				drawImage(gui, box, BOX_HOVER_SRC_X, BOX_SRC_Y);
			}else{
				drawImage(gui, box, BOX_SRC_X, BOX_SRC_Y);
			}
			
			EnchantmentData data = enchants[i];
			if (data != null) {
				int maxLevel = data.getEnchantment().getEnchantment().getMaxLevel();
				int value = data.getValue();
				for (int j = 0; j < maxLevel; j++) {
					int[] bar = getBarRect(i, j, maxLevel);
					if (j != maxLevel - 1) {
						drawImage(gui, bar[0] + bar[2], bar[1], MARKER_SRC_X + j * 2, MARKER_SRC_Y, 1, bar[3]);
					}
					
					int levelMaxValue = data.getEnchantment().getValue(j + 1);
					if (value > 0) {
						float multiplier = (float)value / levelMaxValue;
						if (multiplier > 1) {
							multiplier = 1;
						}
						bar[2] *= multiplier;
						drawImage(gui, bar, BAR_SRC_X, BAR_SRC_Y + 11 * j);
					}
					value -= levelMaxValue;
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
				str = LocalizationUtility.ENCHANTER_INSTRUCTION.translate();
			}
			
			
			drawStringOnMouseOver(gui, str, x, y, getBoxRect(i));
		}
	}
	
	private int[] getBoxRect(int id) {
		return new int[] {40, 17 + id * 20, 61, 12};
	}
	
	private int[] getBarRect(int id, int barId, int maxLevel) {
		int width = (59 - (maxLevel - 1)) / maxLevel;
		return new int[] {41 + (width + 1) * barId, 18 + id * 20, width, 10};
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
				updateGuiData(info, i * 3,      (short)(-1));
			}else{
                updateGuiData(info, i * 3,      (short)(data.getEnchantment().getEnchantment().effectId));
				updateGuiData(info, i * 3 + 1,  (short)(data.getValue() & 65535));
				updateGuiData(info, i * 3 + 2,  (short)((data.getValue() >> 16) & 65535));
			}
		}
	}
	
	
	
	@Override
	public void receiveGuiData(int id, short data) {	
		int dataInt = data;
		if (dataInt < 0) {
			dataInt += 65536;
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
				enchants[enchantId].setValue(((enchants[enchantId].getValue() & -65536) | dataInt));
			}else if (id == 2) {
				enchants[enchantId].setValue(((enchants[enchantId].getValue() & 65535) | (dataInt << 16)));
			}			
		}
		

	}
	
	
	@Override
	protected void save(NBTTagCompound tagCompound) {
		super.save(tagCompound);
		for (int i = 0; i < 3; i++) {
			if (enchants[i] == null) {
				tagCompound.setShort("EffectId" + i, (short)-1);
			}else{
				tagCompound.setShort("EffectId" + i, (short)enchants[i].getEnchantment().getEnchantment().effectId);
				tagCompound.setInteger("Value" + i, enchants[i].getValue());
			}
		}
	}
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		super.load(tagCompound);
		for (int i = 0; i < 3; i++) {
			short effect = (tagCompound.getShort("EffectId" + i));
			if (effect == -1) {
				enchants[i] = null;
			}else{
				enchants[i] = EnchantmentInfo.createDataFromEffectId(enchants[i], effect);
				if (enchants[i] != null) {
					enchants[i].setValue(tagCompound.getInteger("Value" + i));
				}
			}
		}
	}
	
	@Override
	public int guiWidth() {
		return 110;
	}

	public void addType(EnchantmentInfo.Enchantment_Type type) {
		enabledTypes.add(type);
	}
}
