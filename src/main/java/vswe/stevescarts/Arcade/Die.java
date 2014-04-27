package vswe.stevescarts.Arcade;

import vswe.stevescarts.Interfaces.GuiMinecart;

public class Die {

	private ArcadeMonopoly game;
	private int number;
	private int graphicalId;
	
	public Die(ArcadeMonopoly game, int graphicalId) {
		this.game = game;
		this.graphicalId = graphicalId;
		randomize();
	}
	
	public void draw(GuiMinecart gui, int x, int y) {
		game.getModule().drawImage(gui, x, y, 256 - 24 * (graphicalId + 1), 256 - 24, 24, 24);
		
		switch (number) {
			case 5:
				drawEye(gui, x + 15, y + 3);
				drawEye(gui, x + 3, y + 15);				
			case 3:
				drawEye(gui, x + 3, y + 3);
				drawEye(gui, x + 15, y + 15);
			case 1:
				drawEye(gui, x + 9, y + 9);
				break;
			case 4:
				drawEye(gui, x + 3, y + 3);
				drawEye(gui, x + 15, y + 15);
			case 2:
				drawEye(gui, x + 15, y + 3);
				drawEye(gui, x + 3, y + 15);
				break;
			case 6:
				drawEye(gui, x + 3, y + 2);		
				drawEye(gui, x + 3, y + 9);	
				drawEye(gui, x + 3, y + 16);	
				drawEye(gui, x + 15, y + 2);		
				drawEye(gui, x + 15, y + 9);	
				drawEye(gui, x + 15, y + 16);
				break;
		}
	}

	private void drawEye(GuiMinecart gui, int x, int y) {
		game.getModule().drawImage(gui, x, y, 256 - 6 * (graphicalId + 1), 256 - 24 - 6, 6, 6);
	}
	
	public int getNumber() {
		return number;
	}
	
	public void randomize() {
		number = game.getModule().getCart().rand.nextInt(6) + 1;
	}
	
}
