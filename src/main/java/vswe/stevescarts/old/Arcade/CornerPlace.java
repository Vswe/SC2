package vswe.stevescarts.old.Arcade;

import java.util.EnumSet;

import vswe.stevescarts.client.interfaces.GuiVehicle;

public class CornerPlace extends Place {

	private int texture;
	
	public CornerPlace(ArcadeMonopoly game, int texture) {
		super(game);
		this.texture = texture;
	}
	
	@Override
	public void draw(GuiVehicle gui, EnumSet<PLACE_STATE> states) {
		game.loadTexture(gui, 2);
		applyColorFilter(gui, states);
		game.getModule().drawImage(gui, 0, 0, ArcadeMonopoly.PLACE_HEIGHT * (texture % 2), ArcadeMonopoly.PLACE_HEIGHT * (texture / 2), ArcadeMonopoly.PLACE_HEIGHT, ArcadeMonopoly.PLACE_HEIGHT);	
	}

}
