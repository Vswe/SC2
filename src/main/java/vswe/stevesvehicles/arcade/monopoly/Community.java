package vswe.stevesvehicles.arcade.monopoly;

import java.util.EnumSet;

import vswe.stevesvehicles.client.gui.screen.GuiVehicle;


public class Community extends CardPlace {

	public Community(ArcadeMonopoly game) {
		super(game);
	}

	@Override
	protected int getTextureId() {
		return 5;
	}
	
	@Override
	public void drawText(GuiVehicle gui, EnumSet<PlaceState> states) {
		game.getModule().drawSplitString(gui, "Dungeon Chest", 3 + gui.getGuiLeft(), 10 + gui.getGuiTop(), ArcadeMonopoly.PLACE_WIDTH - 6, true, 0x404040);
	}

	@Override
	public Card getCard() {
		return CardCommunity.cards.get(game.getModule().getVehicle().getRandom().nextInt(CardCommunity.cards.size()));
	}
	
}
