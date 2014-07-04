package vswe.stevesvehicles.module.common.attachment;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.module.cart.attachment.ModuleAttachment;
import vswe.stevesvehicles.arcade.ArcadeGame;
import vswe.stevesvehicles.arcade.invader.ArcadeInvaders;
import vswe.stevesvehicles.arcade.sweeper.ArcadeSweeper;
import vswe.stevesvehicles.arcade.tetris.ArcadeTetris;
import vswe.stevesvehicles.arcade.tracks.ArcadeTracks;
import vswe.stevesvehicles.arcade.tracks.TrackStory;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class ModuleArcade extends ModuleAttachment {

	public ModuleArcade(VehicleBase vehicleBase) {
		super(vehicleBase);
		
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
		return getVehicle().getWorld().isRemote && currentGame != null;
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
	public void drawForeground(GuiVehicle gui) {
		if (isGameActive()) {
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
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource("/gui/arcade.png");
		
		afkTimer = 0;
		
		if (isGameActive()) {
			int srcX = 0;
			int srcY = 104 + (inRect(x, y, EXIT_AREA) ? 16 : 0);
			
			drawImage(gui, EXIT_AREA, srcX, srcY);
			
			currentGame.drawBackground(gui, x, y);			
		}else{
			drawImage(gui, LIST_AREA, 0, 0);
			
			for (int i = 0; i < games.size(); i++) {
				int[] button = getButtonGraphicArea(i);				
				
				int srcX = 0;
				int srcY = 136 + (inRect(x, y, getButtonBoundsArea(i)) ? button[3] : 0);
							
				if (button[3] > 0) {				
					drawImage(gui, button, srcX, srcY);
					
					int[] icon = getButtonIconArea(i);
					
					drawImage(gui, icon, i * 16, LIST_AREA[3]);
				}
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		if (isGameActive()) {
			drawStringOnMouseOver(gui, "Exit", x, y, EXIT_AREA);
			currentGame.drawMouseOver(gui, x, y);
		}
	}	
	


    private static final int[] EXIT_AREA = new int[] {455, 6, 16, 16};
    private static final int[] LIST_AREA = new int[] {15, 20, 170, 88};


	
	private int[] getButtonBoundsArea(int i) {
		return getButtonArea(i, false);
	}
	
	private int[] getButtonGraphicArea(int i) {
		return getButtonArea(i, true);
	}	
	
	private int[] getButtonArea(int i, boolean graphic) {
		return new int[] {LIST_AREA[0] + 2, LIST_AREA[1] + 2 + i * 21, 166, graphic ? 21 : 20};
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
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (isGameActive()) {
			if (button == 0 && inRect(x,y, EXIT_AREA)) {
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
	public void mouseMovedOrUp(GuiVehicle gui,int x, int y, int button) {
		if (isGameActive()) {
			currentGame.mouseMovedOrUp(gui, x, y, button);
		}
	}
	
	@Override
	public void keyPress(GuiVehicle gui, char character, int extraInformation) {
		if (isGameActive()) {
			currentGame.keyPress(gui, character, extraInformation);
		}		
	}
	
	@Override
	protected void save(NBTTagCompound tagCompound) {
		for(ArcadeGame game : games) {
			game.save(tagCompound);
		}
	}	
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		for(ArcadeGame game : games) {
			game.load(tagCompound);
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
        return currentGame != null && currentGame.disableStandardKeyFunctionality();

    }
	
}
