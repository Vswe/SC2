package vswe.stevesvehicles.arcade.monopoly;

import vswe.stevesvehicles.client.gui.GuiVehicle;

public abstract class Card {

	private String message;
	public Card(String message) {
		this.message = message;
	}

	
	public void render(ArcadeMonopoly game, GuiVehicle gui, int[] rect, boolean isFront) {
		if (isFront) {
			game.loadTexture(gui, 1);
			game.getModule().drawImage(gui, rect, 67, 177);
			game.getModule().drawSplitString(gui, message, rect[0] + gui.getGuiLeft() + 5, rect[1] + gui.getGuiTop() + 5, rect[2] - 10, true, 0x404040);
			
			if (getNote() != null) {
				int x = 10;
				if (!getMoneyPrefix().equals("")) {
					game.getModule().drawString(gui, getMoneyPrefix(), x, 64, 0x404040);
					x += gui.getFontRenderer().getStringWidth(getMoneyPrefix()) + 5;
				}
				getNote().draw(game, gui, x, 59, getNoteCount());				
				x += 26 + 5;
				if (!getMoneyPostfix().equals("")) {
					game.getModule().drawString(gui, getMoneyPostfix(), x, 64, 0x404040);
				}				
			}

		}else{
			game.getModule().drawImage(gui, rect, 0, rect[3] * getBackgroundV());
		}
	}
	
	
	public int getNoteCount() {
		return 0;
	}
	
	public Note getNote() {
		return null;
	}	
	
	public String getMoneyPrefix() {
		return "";
	}
	
	public String getMoneyPostfix() {
		return "";
	}
	
	


	public abstract void doStuff(ArcadeMonopoly game, Piece piece);
	public abstract int getBackgroundV();	
	
}
