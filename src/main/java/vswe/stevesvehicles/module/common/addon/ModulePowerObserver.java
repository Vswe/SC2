package vswe.stevesvehicles.module.common.addon;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.module.common.engine.ModuleEngine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModulePowerObserver extends ModuleAddon {

	public ModulePowerObserver(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	@Override 
	public boolean hasGui() {
		return true;
	}
	
	@Override 
	public boolean hasSlots() {
		return false;
	}	

	@Override
	public int guiWidth() {
		return 190;
	}
	
	@Override
	public int guiHeight() {
		return 150;
	}		
	
	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui,getModuleName(), 8, 6, 0x404040);
	    
	    for (int i = 0; i < 4; i++) {
	    	int[] rect = getPowerRect(i);
	    	
	    	drawString(gui, powerLevel[i] + Localization.MODULES.ADDONS.K.translate(), rect, 0x404040);
	    }
	}
	
	private boolean removeOnPickup() {
		return true;
	}
	

	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		for (int i  = 0; i < getVehicle().getEngines().size(); i++) {
			if (!removeOnPickup() || currentEngine != i) {
				drawEngine(gui, i, getEngineRect(i));
			}
		}
		
		ResourceHelper.bindResource("/gui/observer.png");		
					
		for (int i = 0; i < 4; i++) {
			int[] rect = getAreaRect(i);
			
			drawImage(gui, rect, 18, 22 * i);
			if (inRect(x, y, rect)) {
				drawImage(gui, rect, 18, 22 * (i + 4));
			}
			
						
			int count = 0;
			for (int j = 0; j < getVehicle().getEngines().size(); j++) {
				if ((areaData[i] & (1 << j)) != 0) {
					drawEngine(gui, j, getEngineRectInArea(i, count));
					count++;
				}
			}
			
			ResourceHelper.bindResource("/gui/observer.png");	
			
			rect = getPowerRect(i);
			if (isAreaActive(i)) {
				drawImage(gui, rect, 122, 0);
			}else{
				drawImage(gui, rect, 122 + rect[2], 0);
			}
			
			if (inRect(x,y,rect)) {
				drawImage(gui, rect, 122 + rect[2] * 2, 0);
			}
		}
		

		
		if (currentEngine != -1) {
			drawEngine(gui, currentEngine, getEngineRectMouse(x, y + getVehicle().getRealScrollY()));
		}
	}	
	
	
	private void drawEngine(GuiVehicle gui, int id, int [] rect) {
		ModuleEngine engine = getVehicle().getEngines().get(id);
		
		ResourceHelper.bindResourcePath("/atlas/items.png");
		
		drawImage(gui, engine.getModuleData().getIcon(), rect, 0, 0);
	}
		
	private int[] getAreaRect(int id) {
		return new int[] {10, 40 + 25 * id, 104, 22};
	}

	private int[] getEngineRect(int id) {
		return new int[] {11 + id * 20, 21, 16, 16};
	}
	
	private int[] getEngineRectMouse(int x, int y) {
		return new int[] {x - 8, y - 8, 16, 16};
	}
	
	private int[] getEngineRectInArea(int areaId, int number) {
		int[] area = getAreaRect(areaId);
		
		return new int[] {area[0] + 4 + number * 20, area[1] + 3, 16, 16};
	}
	
	private int[] getPowerRect(int areaid) {
		int[] area = getAreaRect(areaid);
		
		return new int[] {area[0] + area[2] + 10, area[1] + 2, 35, 18};
	}	
	
	@Override
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		for (int i  = 0; i < getVehicle().getEngines().size(); i++) {
			if (!removeOnPickup() || currentEngine != i) {
				ModuleEngine engine = getVehicle().getEngines().get(i);

				drawStringOnMouseOver(gui, engine.getModuleData().getName() + "\n" + Localization.MODULES.ADDONS.OBSERVER_INSTRUCTION.translate(), x, y, getEngineRect(i));
			}
		}
		
		for (int i = 0; i < 4; i++) {
		    int count = 0;
			for (int j = 0; j < getVehicle().getEngines().size(); j++) {
				if ((areaData[i] & (1 << j)) != 0) {
					ModuleEngine engine = getVehicle().getEngines().get(j);

					drawStringOnMouseOver(gui, engine.getModuleData().getName() + "\n" + Localization.MODULES.ADDONS.OBSERVER_REMOVE.translate(), x, y, getEngineRectInArea(i, count));
					count++;
				}
			}	
		
			if (currentEngine != -1) {
				drawStringOnMouseOver(gui, Localization.MODULES.ADDONS.OBSERVER_DROP.translate(), x, y, getAreaRect(i));
			}
			drawStringOnMouseOver(gui, Localization.MODULES.ADDONS.OBSERVER_CHANGE.translate() + "\n" + Localization.MODULES.ADDONS.OBSERVER_CHANGE_10.translate(), x, y, getPowerRect(i));
		}
	}	
	
	@Override
	public int numberOfGuiData() {
		return 8;
	}	
	
	@Override
	protected void checkGuiData(Object[] info) {
		for (int i = 0; i < 4; i++) {
			updateGuiData(info, i, areaData[i]);
		}
		for (int i = 0; i < 4; i++) {
			updateGuiData(info, i+4, powerLevel[i]);
		}		
	}
	
	@Override
	public void receiveGuiData(int id, short data) {
		if (id >= 0 && id < 4) {
			areaData[id] = data;
		}else if(id >= 4 && id < 8) {
			powerLevel[id - 4] = data;
		}
	}	

	@Override
	public int numberOfPackets() {
		return 3;
	}	
	
	private short[] areaData = new short[4];
	private short[] powerLevel = new short[4];
	
	@Override
	protected void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id == 0) {
			int area = data[0];
			int engine = data[1];
			
			areaData[area] |= 1 << engine;
		}else if (id == 1) {
			int area = data[0];
			int engine = data[1];		
		
			areaData[area] &= ~(1 << engine);
		}else if(id == 2) {
			int area = data[0];
			int button = data[1] & 1;
			boolean shift = (data[1] & 2) != 0;
			
			int change = button == 0 ? 1 : -1;
			if (shift) {
				change *= 10;
			}
			
			short value = powerLevel[area];
			value += change;
			if (value < 0) {
				value = 0;
			}else if(value > 999){
				value = 999;
			}
			
			powerLevel[area] = value;
		}
	}	
	
	private int currentEngine = -1;
	
	@Override
	public void mouseMovedOrUp(GuiVehicle gui,  int x, int y, int button) {
		if (button != -1) {
			if (button == 0) {
				for (int i = 0; i < 4; i++) {
					int[] rect = getAreaRect(i);
	
					if (inRect(x, y, rect)) {
						sendPacket(0, new byte[] {(byte)i, (byte)currentEngine});
						break;
					}
				}
			}
			currentEngine = -1;		
		}
	}
	
	@Override
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {

		for (int i  = 0; i < 4; i++) {
			int[] rect = getPowerRect(i);
		
			if (inRect(x, y, rect)) {
				sendPacket(2, new byte[] {(byte)i, (byte)(button | (GuiScreen.isShiftKeyDown() ? 2 : 0))});
				break;
			}
		}				
		
		if (button == 0){	
			for (int i  = 0; i < getVehicle().getEngines().size(); i++) {
				int[] rect = getEngineRect(i);
			
				if (inRect(x, y, rect)) {
					currentEngine = i; 
					break;
				}
			}
					
		}else if(button == 1) {
			for (int i = 0; i < 4; i++) {
				int count = 0;
				for (int j = 0; j < getVehicle().getEngines().size(); j++) {
					if ((areaData[i] & (1 << j)) != 0) {
						int[] rect = getEngineRectInArea(i, count);
					
						if (inRect(x, y, rect)) {
							sendPacket(1, new byte[] {(byte)i, (byte)j});
							break;
						}
						count++;
					}
				}
			}
		}
		
	}
	
	
	public boolean isAreaActive(int area) {
		int power = 0;
		for (int i  = 0; i < getVehicle().getEngines().size(); i++) {
			ModuleEngine engine = getVehicle().getEngines().get(i);
			if ((areaData[area] & (1 << i)) != 0) {
				power += engine.getTotalFuel();
			}	
		}
		return power > powerLevel[area] * 1000;
	}
	
	@Override
	protected void save(NBTTagCompound tagCompound) {
		for (int i = 0; i < 4; i++) {
			tagCompound.setShort("AreaData" + i, areaData[i]);
			tagCompound.setShort("PowerLevel" + i, powerLevel[i]);
		}
	}	
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		for (int i = 0; i < 4; i++) {
			areaData[i] = tagCompound.getShort("AreaData" + i);
			powerLevel[i] = tagCompound.getShort("PowerLevel" + i);
		}
	}	
	
}