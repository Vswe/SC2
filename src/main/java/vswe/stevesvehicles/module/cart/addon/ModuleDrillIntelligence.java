package vswe.stevesvehicles.module.cart.addon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.module.common.addon.ModuleAddon;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.cart.tool.ModuleDrill;

public class ModuleDrillIntelligence extends ModuleAddon {

	public ModuleDrillIntelligence(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	private ModuleDrill drill;
	private boolean hasHeightController;	
	
	@Override
	public void preInit() {
		super.preInit();
		for (ModuleBase module : getVehicle().getModules()) {
			if (module instanceof ModuleDrill) {
				drill = (ModuleDrill)module;
			}else if (module instanceof ModuleHeightControl) {
				hasHeightController = true;
			}			
		}		
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
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, getModuleName(), 8, 6, 0x404040);
	}
	
	private int getDrillWidth() {
		if (drill == null) {
			return 0;
		}else{
			return drill.getAreaWidth();
		}
	}	
	
	private int getDrillHeight() {
		if (drill == null) {
			return 0;
		}else{
			return drill.getAreaHeight() + (hasHeightController ? 2 : 0);
		}
	}	
	
	private int guiW = -1;
	private int guiH = -1;
	
	@Override
	public int guiWidth() {
		if (guiW == -1) {
			guiW = Math.max(15 + getDrillWidth() * 10 + 5, 93);
		}	
	
		return guiW;
	}
	
	@Override
	public int guiHeight() {
		if (guiH == -1) {
			guiH = 20 + getDrillHeight() * 10 + 5;
		}
	
		return guiH;
	}		
	

	
	
	@Override
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource("/gui/intelligence.png");

		int w = getDrillWidth();
		int h = getDrillHeight();
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int[] rect = getSettingRect(i, j);
				
				int srcX = (!hasHeightController || j != 0 && j != h - 1) ? 0 : 8;
				int srcY = 0;
				
				drawImage(gui, rect, srcX, srcY);
				
				if (isActive(j * w + i)) {
					srcX = isLocked(j * w + i) ? 8 : 0;
					srcY = 8;
					
					drawImage(gui, rect, srcX, srcY);				
				}				
				
				srcX = inRect(x, y, rect) ? 8 : 0;
				srcY = 16;	
				drawImage(gui, rect, srcX, srcY);
				

			}
		}
	}
	
	private short[] isDisabled;
	private void initDisabledData() {
		if (isDisabled == null) {
			isDisabled = new short[(int)Math.ceil((getDrillWidth() * getDrillHeight()) / 16F)];
		}	
	}
	
	public boolean isActive(int x, int y, int offset, boolean direction) {
		y = getDrillHeight() - 1 - y;
	
		if (hasHeightController) {
			y -= offset;
		}
		
		if (!direction) {
			x = getDrillWidth() - 1 - x;
		}
	
		return isActive(y * getDrillWidth() + x);
	}
	
	private boolean isActive(int id) {	
		initDisabledData();

        return isLocked(id) || (isDisabled[id / 16] & (1 << (id % 16))) == 0;
    }
	
	private boolean isLocked(int id) {
		int x = id % getDrillWidth();
		int y = id / getDrillWidth();

        return (y == getDrillHeight() - 1 || (hasHeightController && y == getDrillHeight() - 2)) && x == (getDrillWidth() - 1) / 2;
    }
	
	private void swapActiveness(int id) {	
		initDisabledData();
	
		if (!isLocked(id)) {
			isDisabled[id / 16] ^= 1 << (id % 16);
		}
	}	
	
	
	private int[] getSettingRect(int x, int y) {
		return new int[] {15 + x * 10, 20 + y * 10, 8, 8};
	}
	
	@Override
	public void drawMouseOver(GuiVehicle gui, int x, int y) {

		int w = getDrillWidth();
		int h = getDrillHeight();
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int[] rect = getSettingRect(i, j);
				
				String str = isLocked(j*w+i) ? Localization.MODULES.ADDONS.LOCKED.translate() : Localization.MODULES.ADDONS.CHANGE_INTELLIGENCE.translate() + "\n" + Localization.MODULES.ADDONS.CURRENT_INTELLIGENCE.translate((isActive(j*w+i) ? "0" : "1"));
				
				drawStringOnMouseOver(gui, str, x, y, rect);
			}
		}
	

	}
	
	@Override
	public int numberOfGuiData() {
		int maxDrillWidth = 9;
		int maxDrillHeight = 9;
		return (int)Math.ceil((maxDrillWidth*(maxDrillHeight+2)) / 16F);
	}	
	
	@Override
	protected void checkGuiData(Object[] info) {
		if (isDisabled != null) {
			for (int i = 0; i < isDisabled.length; i++) {
				updateGuiData(info, i, isDisabled[i]);
			}
		}			
	}
	
	@Override
	public void receiveGuiData(int id, short data) {
		initDisabledData();
		
		if (id >= 0 && id < isDisabled.length) {
			isDisabled[id] = data;
		}
	}	
	
	@Override
	public int numberOfPackets() {
		return 1;
	}	
	
	@Override
	protected void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id == 0) {
			swapActiveness(data[0]);
		}
	}	
	
	@Override
	protected void save(NBTTagCompound tagCompound) {
		initDisabledData();

        for (short anIsDisabled : isDisabled) {
            tagCompound.setShort("isDisabled", anIsDisabled);
        }
	}	
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		initDisabledData();
	
		for (int i = 0; i < isDisabled.length; i++) {
			isDisabled[i] = tagCompound.getShort("isDisabled");
		}
	}		
	
	private boolean clickedState;
	private boolean clicked;
	private int lastId;
	@Override
	public void mouseMovedOrUp(GuiVehicle gui,  int x, int y, int button) {
		if (button == -1 && clicked) {
			int w = getDrillWidth();
			int h = getDrillHeight();
			
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					if (lastId == j * w + i || isActive(j * w + i) != clickedState) {
						continue;
					}
				
				
					int[] rect = getSettingRect(i, j);
					
					if (inRect(x, y, rect)) {
						lastId = j * w + i;
						sendPacket(0, (byte)(j * w + i));
						return;
					}
				}
			}		
		}
		
		if (button == 0) {
			clicked = false;
		}		
	}
	
	
	@Override
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (button == 0) {
			int w = getDrillWidth();
			int h = getDrillHeight();
			
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					int[] rect = getSettingRect(i, j);
					
					if (inRect(x, y, rect)) {
						clicked = true;
						clickedState = isActive(j * w + i);
						lastId = j * w + i;
						sendPacket(0, (byte)(j * w + i));
						return;
					}
				}
			}			
		}
	}	
} 