package vswe.stevesvehicles.old.Arcade;

import java.util.EnumSet;

import vswe.stevesvehicles.client.interfaces.GuiVehicle;

public class Go extends CornerPlace {

	public Go(ArcadeMonopoly game) {
		super(game, 0);
	}

	@Override
	public void draw(GuiVehicle gui, EnumSet<PLACE_STATE> states) {
		super.draw(gui, states);
		
		Note.DIAMOND.draw(game, gui, 45, 5, 2);
	}
	
	@Override
	public void drawText(GuiVehicle gui, EnumSet<PLACE_STATE> states) {
		game.getModule().drawString(gui, "Collect", 5, 10, 0x404040);
		game.getModule().drawString(gui, "as you pass.", 5, 20, 0x404040);
	}

	@Override
	public void onPiecePass(Piece piece) {
		piece.addMoney(Note.DIAMOND, 2, true);
	}
	
}
