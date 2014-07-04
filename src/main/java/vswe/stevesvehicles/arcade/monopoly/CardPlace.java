package vswe.stevesvehicles.arcade.monopoly;

public abstract class CardPlace extends Place {

	public CardPlace(ArcadeMonopoly game) {
		super(game);
	}
	
	public abstract Card getCard();
	
	@Override
	public boolean onPieceStop(Piece piece) {
		return false;
	}

}
