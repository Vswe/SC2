package vswe.stevesvehicles.old.Arcade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.StevesVehicles;
import vswe.stevesvehicles.vehicles.VehicleBase;
import vswe.stevesvehicles.vehicles.entities.EntityModularCart;
import vswe.stevesvehicles.old.Helpers.SoundHandler;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleArcade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public abstract class ArcadeGame {
	
	private ModuleArcade module;
	private Localization.ARCADE name;

	
	public ArcadeGame(ModuleArcade module, Localization.ARCADE name) {
		this.name = name;
		this.module = module;
	}

	
	public String getName() {
		return name.translate();
	}
	
	public ModuleArcade getModule() {
		return module;
	}
	
	@SideOnly(Side.CLIENT)
	public void update() {
        if (StevesVehicles.instance.useArcadeSounds) {
            ((EntityModularCart)getModule().getVehicle().getEntity()).silent();
        }
	}
	
	
	@SideOnly(Side.CLIENT)
	public void drawForeground(GuiVehicle gui) {}
	
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {}
	
	@SideOnly(Side.CLIENT)
	public void drawMouseOver(GuiVehicle gui, int x, int y) {}
	
	@SideOnly(Side.CLIENT)
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {}
	
	@SideOnly(Side.CLIENT)
	public void mouseMovedOrUp(GuiVehicle gui,int x, int y, int button) {}
	
	@SideOnly(Side.CLIENT)
	public void keyPress(GuiVehicle gui, char character, int extraInformation) {}
	
	public void Save(NBTTagCompound tagCompound, int id) {}
	public void Load(NBTTagCompound tagCompound, int id) {}
	public void receivePacket(int id, byte[] data, EntityPlayer player) {}
	public void checkGuiData(Object[] info) {}
	public void receiveGuiData(int id, short data) {}


	public boolean disableStandardKeyFunctionality() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public static void playSound(String sound, float volume, float pitch) {
		if (StevesVehicles.instance.useArcadeSounds && sound != null) {
			SoundHandler.playSound(sound, volume, pitch);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void playDefaultSound(String sound, float volume, float pitch) {
		if (StevesVehicles.instance.useArcadeSounds && sound != null) {
			SoundHandler.playDefaultSound(sound, volume, pitch);
		}
	}	
	
	public boolean allowKeyRepeat() {
		return false;
	}


	public void load(GuiVehicle gui) {
		gui.enableKeyRepeat(allowKeyRepeat());
	}
	
	public void unload(GuiVehicle gui) {
		if (allowKeyRepeat()) {
			gui.enableKeyRepeat(false);
		}
	}	
	
	public void drawImageInArea(GuiVehicle gui, int x, int y, int u, int v, int w, int h) {
		drawImageInArea(gui, x, y, u, v, w, h, 5, 4, VehicleBase.MODULAR_SPACE_WIDTH,  VehicleBase.MODULAR_SPACE_HEIGHT);
	}
	
	public void drawImageInArea(GuiVehicle gui, int x, int y, int u, int v, int w, int h, int x1, int y1, int x2, int y2) {
		if (x < x1) {
			w -= x1 - x;
			u += x1 - x;
			x = x1;
		}else if(x + w > x2) {
			w = x2 - x;
		}
		
		if (y < y1) {
			h -= y1 - y;
			v += y1 - y;
			y = y1;
		}else if(y + h > y2) {
			h = y2 - y;
		}
		
				
		if (w > 0 && h > 0) {		
			getModule().drawImage(gui, 
					x, 
					y, 
					u,
					v,
					w,
					h
				);	
		}		
	}
	
}
