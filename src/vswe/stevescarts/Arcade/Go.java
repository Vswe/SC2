package vswe.stevescarts.Arcade;

import java.util.EnumSet;

import vswe.stevescarts.Arcade.Place.PLACE_STATE;
import vswe.stevescarts.Interfaces.GuiMinecart;

public class Go extends CornerPlace {

	public Go(ArcadeMonopoly game) {
		super(game, 0);
	}

	@Override
	public void draw(GuiMinecart gui, EnumSet<PLACE_STATE> states) {
		super.draw(gui, states);
		
		Note.DIAMOND.draw(game, gui, 45, 5, 2);
	}
	
	@Override
	public void drawText(GuiMinecart gui, EnumSet<PLACE_STATE> states) {
		game.getModule().drawString(gui, "Collect", 5, 10, 0x404040);
		game.getModule().drawString(gui, "as you pass.", 5, 20, 0x404040);
	}

	@Override
	public void onPiecePass(Piece piece) {
		piece.addMoney(Note.DIAMOND, 2, true);
	}
	
}
