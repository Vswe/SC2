package vswe.stevescarts.Arcade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.SoundHandler;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.Realtimers.ModuleArcade;
import cpw.mods.fml.relauncher.ReflectionHelper;
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
        //TODO figure out/test how these sounds behave
		//if (StevesCarts.instance.useArcadeSounds && getModule().getCart().getSoundUpdater() != null) {
		//	ReflectionHelper.setPrivateValue(SoundUpdaterMinecart.class, getModule().getCart().getSoundUpdater(), 0F, 8/*"i"*/ /*"minecartMoveSoundVolume"*/);
		//	ReflectionHelper.setPrivateValue(SoundUpdaterMinecart.class, getModule().getCart().getSoundUpdater(), 0F, 9/*"j"*/  /*"minecartRideSoundVolume"*/);
		//}
	}
	
	
	@SideOnly(Side.CLIENT)
	public void drawForeground(GuiMinecart gui) {}
	
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiMinecart gui, int x, int y) {}
	
	@SideOnly(Side.CLIENT)
	public void drawMouseOver(GuiMinecart gui, int x, int y) {}
	
	@SideOnly(Side.CLIENT)
	public void mouseClicked(GuiMinecart gui, int x, int y, int button) {}
	
	@SideOnly(Side.CLIENT)
	public void mouseMovedOrUp(GuiMinecart gui,int x, int y, int button) {}	
	
	@SideOnly(Side.CLIENT)
	public void keyPress(GuiMinecart gui, char character, int extraInformation) {}	
	
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
		if (StevesCarts.instance.useArcadeSounds && sound != null) {
			SoundHandler.playSound(sound, volume, pitch);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void playDefaultSound(String sound, float volume, float pitch) {
		if (StevesCarts.instance.useArcadeSounds && sound != null) {
			SoundHandler.playDefaultSound(sound, volume, pitch);
		}
	}	
	
	public boolean allowKeyRepeat() {
		return false;
	}


	public void load(GuiMinecart gui) {
		gui.enableKeyRepeat(allowKeyRepeat());
	}
	
	public void unload(GuiMinecart gui) {
		if (allowKeyRepeat()) {
			gui.enableKeyRepeat(false);
		}
	}	
	
	public void drawImageInArea(GuiMinecart gui, int x, int y, int u, int v, int w, int h) {
		drawImageInArea(gui, x, y, u, v, w, h, 5, 4, MinecartModular.MODULAR_SPACE_WIDTH,  MinecartModular.MODULAR_SPACE_HEIGHT);
	}
	
	public void drawImageInArea(GuiMinecart gui, int x, int y, int u, int v, int w, int h, int x1, int y1, int x2, int y2) {		
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
