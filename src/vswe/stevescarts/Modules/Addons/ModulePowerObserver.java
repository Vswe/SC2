package vswe.stevescarts.Modules.Addons;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.Engines.ModuleEngine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModulePowerObserver extends ModuleAddon {

	public ModulePowerObserver(MinecartModular cart) {
		super(cart);
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
	public void drawForeground(GuiMinecart gui) {
	    drawString(gui,"Power Observer", 8, 6, 0x404040);	
	    
	    for (int i = 0; i < 4; i++) {
	    	int[] rect = getPowerRect(i);
	    	
	    	drawString(gui, powerLevel[i] + "K", rect, 0x404040);	
	    }
	}
	
	private boolean removeOnPickup() {
		return true;
	}
	

	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiMinecart gui, int x, int y) {		
		for (int i  = 0; i < getCart().getEngines().size(); i++) {
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
			for (int j = 0; j < getCart().getEngines().size(); j++) {
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
			drawEngine(gui, currentEngine, getEngineRectMouse(x, y + getCart().getRealScrollY()));
		}
	}	
	
	
	private void drawEngine(GuiMinecart gui, int id, int [] rect) {
		ModuleEngine engine = getCart().getEngines().get(id);
		
		ResourceHelper.bindResourcePath("/atlas/items.png");
		
		drawImage(gui, engine.getData().getIcon(), rect, 0, 0);	
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
	
	private int[] getEngineRectInArea(int areaid, int number) {
		int[] area = getAreaRect(areaid);
		
		return new int[] {area[0] + 4 + number * 20, area[1] + 3, 16, 16};
	}
	
	private int[] getPowerRect(int areaid) {
		int[] area = getAreaRect(areaid);
		
		return new int[] {area[0] + area[2] + 10, area[1] + 2, 35, 18};
	}	
	
	@Override
	public void drawMouseOver(GuiMinecart gui, int x, int y) {
		for (int i  = 0; i < getCart().getEngines().size(); i++) {		
			if (!removeOnPickup() || currentEngine != i) {
				ModuleEngine engine = getCart().getEngines().get(i);

				drawStringOnMouseOver(gui, engine.getData().getName() + "\nClick and drag to place in desired area.", x, y, getEngineRect(i));
			}
		}
		
		for (int i = 0; i < 4; i++) {;
		int count = 0;
			for (int j = 0; j < getCart().getEngines().size(); j++) {
				if ((areaData[i] & (1 << j)) != 0) {
					ModuleEngine engine = getCart().getEngines().get(j);

					drawStringOnMouseOver(gui, engine.getData().getName() + "\nRight click to remove.", x, y, getEngineRectInArea(i, count));
					count++;
				}
			}	
		
			if (currentEngine != -1) {
				drawStringOnMouseOver(gui, "Drop the engine here.", x, y, getAreaRect(i));
			}
			drawStringOnMouseOver(gui, "Click to change power limit\nShift click to change by 10K", x, y, getPowerRect(i));
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
	public void mouseMovedOrUp(GuiMinecart gui,  int x, int y, int button) {
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
	public void mouseClicked(GuiMinecart gui, int x, int y, int button) {

		for (int i  = 0; i < 4; i++) {
			int[] rect = getPowerRect(i);
		
			if (inRect(x, y, rect)) {
				sendPacket(2, new byte[] {(byte)i, (byte)(button | (gui.isShiftKeyDown() ? 2 : 0))});
				break;
			}
		}				
		
		if (button == 0){	
			for (int i  = 0; i < getCart().getEngines().size(); i++) {
				int[] rect = getEngineRect(i);
			
				if (inRect(x, y, rect)) {
					currentEngine = i; 
					break;
				}
			}
					
		}else if(button == 1) {
			for (int i = 0; i < 4; i++) {
				int count = 0;
				for (int j  = 0; j < getCart().getEngines().size(); j++) {
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
		for (int i  = 0; i < getCart().getEngines().size(); i++) {
			ModuleEngine engine = getCart().getEngines().get(i);
			if ((areaData[area] & (1 << i)) != 0) {
				power += engine.getTotalFuel();
			}	
		}
		return power > powerLevel[area] * 1000;
	}
	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		for (int i = 0; i < 4; i++) {
			tagCompound.setShort(generateNBTName("AreaData" + i,id), areaData[i]);
			tagCompound.setShort(generateNBTName("PowerLevel" + i,id), powerLevel[i]);
		}
	}	
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		for (int i = 0; i < 4; i++) {
			areaData[i] = tagCompound.getShort(generateNBTName("AreaData" + i,id));
			powerLevel[i] = tagCompound.getShort(generateNBTName("PowerLevel" + i,id));
		}
	}	
	
}