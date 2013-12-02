package vswe.stevescarts.Modules.Realtimers;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Arcade.ArcadeGame;
import vswe.stevescarts.Arcade.ArcadeInvaders;
import vswe.stevescarts.Arcade.ArcadeMonopoly;
import vswe.stevescarts.Arcade.ArcadeSweeper;
import vswe.stevescarts.Arcade.ArcadeTetris;
import vswe.stevescarts.Arcade.ArcadeTracks;
import vswe.stevescarts.Arcade.TrackStory;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ModuleBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class ModuleArcade extends ModuleBase {

	public ModuleArcade(MinecartModular cart) {
		super(cart);
		
		games = new  ArrayList<ArcadeGame>();
		games.add(new ArcadeTracks(this));
		games.add(new ArcadeTetris(this));
		games.add(new ArcadeInvaders(this));
		games.add(new ArcadeSweeper(this));
		//games.add(new ArcadeMonopoly(this));
	}

	private ArrayList<ArcadeGame> games;
	private ArcadeGame currentGame;
	private int afkTimer;

	private boolean isGameActive() {
		return getCart().worldObj.isRemote && currentGame != null;
	}
	
	
	@Override
	public boolean doStealInterface() {
		return isGameActive();
	}	

	@Override
	public boolean hasSlots() {
		return false;
	}	
	
	

	@Override
	public boolean hasGui() {
		return true;
	}
	
	@Override
	public int guiWidth() {
		return 190;
	}
	@Override
	public int guiHeight() {
		return 115;
	}	
	
	@Override
	public void update() {
		if (isGameActive() && afkTimer < 10) {	
			currentGame.update();
			afkTimer++;
		}
	}
	
	@Override
	public void drawForeground(GuiMinecart gui) {
		if (isGameActive()) {
		    //drawString(gui, currentGame.getName(), 8, 6, 0x404040);
			
			currentGame.drawForeground(gui);
		}else{
		    drawString(gui,getModuleName(), 8, 6, 0x404040);
		    
			for (int i = 0; i < games.size(); i++) {
				int[] text = getButtonTextArea(i);
				
				if (text[3] == 8) {				
					drawString(gui, games.get(i).getName(), text[0], text[1], 0x404040);
				}
			}		    
		}
	}
	

		
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiMinecart gui, int x, int y) {
		ResourceHelper.bindResource("/gui/arcade.png");
		
		afkTimer = 0;
		
		if (isGameActive()) {	
			int[] rect = getExitArea();
			
			int srcX = 0;
			int srcY = 104 + (inRect(x, y, rect) ? 16 : 0);
			
			drawImage(gui, rect, srcX, srcY);
			
			currentGame.drawBackground(gui, x, y);			
		}else{
			int[] rect = getListArea();
			
			drawImage(gui, rect, 0, 0);
			
			for (int i = 0; i < games.size(); i++) {
				int[] button = getButtonGraphicArea(i);				
				
				int srcX = 0;
				int srcY = 136 + (inRect(x, y, getButtonBoundsArea(i)) ? button[3] : 0);
							
				if (button[3] > 0) {				
					drawImage(gui, button, srcX, srcY);
					
					int[] icon = getButtonIconArea(i);
					
					drawImage(gui, icon, i * 16, rect[3]);
				}
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawMouseOver(GuiMinecart gui, int x, int y) {
		if (isGameActive()) {
			drawStringOnMouseOver(gui, "Exit", x, y, getExitArea());
			currentGame.drawMouseOver(gui, x, y);
		}
	}	
	

	
	private int[] getExitArea() {
		return new int[] {455, 6, 16, 16};
	}
	
	private int[] getListArea() {
		return new int[] {15, 20, 170, 88};
	}
	
	private int[] getButtonBoundsArea(int i) {
		return getButtonArea(i, false);
	}
	
	private int[] getButtonGraphicArea(int i) {
		return getButtonArea(i, true);
	}	
	
	private int[] getButtonArea(int i, boolean graphic) {
		int[] list = getListArea();
		
		return new int[] {list[0] + 2, list[1] + 2 + i*21, 166, graphic ? 21 : 20};
	}
	
	private int[] getButtonTextArea(int i) {
		int[] button = getButtonGraphicArea(i);
		
		
		return new int[] {button[0] + 24, button[1] + 6, button[2], 8};
	}
	
	private int[] getButtonIconArea(int i) {
		int[] button = getButtonGraphicArea(i);
		
		return new int[] {button[0] + 2, button[1] + 2, 16, 16}; 
	}
	
	@Override
	public void mouseClicked(GuiMinecart gui, int x, int y, int button) {
		if (isGameActive()) {
			if (button == 0 && inRect(x,y, getExitArea())) {
				currentGame.unload(gui);
				currentGame = null;
			}else{
				currentGame.mouseClicked(gui, x, y, button);
			}
		}else{
			if (button == 0) {
				for (int i = 0; i < games.size(); i++) {
					if (inRect(x,y, getButtonBoundsArea(i))) {
						currentGame = games.get(i);
						currentGame.load(gui);
						break;
					}
				}
			}
		}
	}
	
	@Override
	public void mouseMovedOrUp(GuiMinecart gui,int x, int y, int button) {
		if (isGameActive()) {
			currentGame.mouseMovedOrUp(gui, x, y, button);
		}
	}
	
	@Override
	public void keyPress(GuiMinecart gui, char character, int extraInformation) {
		if (isGameActive()) {
			currentGame.keyPress(gui, character, extraInformation);
		}		
	}
	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		for(ArcadeGame game : games) {
			game.Save(tagCompound, id);
		}
	}	
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		for(ArcadeGame game : games) {
			game.Load(tagCompound, id);
		}
	}	
	
	@Override
	public int numberOfPackets() {
		return 4;
	}	
	
	@Override
	protected void receivePacket(int id, byte[] data, EntityPlayer player) {
		for(ArcadeGame game : games) {
			game.receivePacket(id, data, player);
		}	
	}		
	
	@Override
	public int numberOfGuiData() {
		return TrackStory.stories.size() + 5;
	}	
	
	@Override
	protected void checkGuiData(Object[] info) {
		for(ArcadeGame game : games) {
			game.checkGuiData(info);
		}						
	}
	
	@Override
	public void receiveGuiData(int id, short data) {
		for(ArcadeGame game : games) {
			game.receiveGuiData(id, data);
		}	
	}	
	
	@Override
	public boolean disableStandardKeyFunctionality() {
		if (currentGame != null) {
			return currentGame.disableStandardKeyFunctionality();
		}

		return false;
	}	
	
}
