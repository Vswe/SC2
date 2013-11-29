package vswe.stevescarts.Arcade;

import java.util.EnumSet;

import org.lwjgl.opengl.GL11;

import vswe.stevescarts.Arcade.Place.PLACE_STATE;
import vswe.stevescarts.Interfaces.GuiMinecart;

public class CornerPlace extends Place {

	private int texture;
	
	public CornerPlace(ArcadeMonopoly game, int texture) {
		super(game);
		this.texture = texture;
	}
	
	@Override
	public void draw(GuiMinecart gui, EnumSet<PLACE_STATE> states) {
		game.loadTexture(gui, 2);
		applyColorFilter(gui, states);
		game.getModule().drawImage(gui, 0, 0, ArcadeMonopoly.PLACE_HEIGHT * (texture % 2), ArcadeMonopoly.PLACE_HEIGHT * (texture / 2), ArcadeMonopoly.PLACE_HEIGHT, ArcadeMonopoly.PLACE_HEIGHT);	
	}

}
