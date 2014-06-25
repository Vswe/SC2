package vswe.stevescarts.old.Arcade;

public class Jail extends CornerPlace {

	public Jail(ArcadeMonopoly game) {
		super(game, 1);
	}

	@Override
	protected int getPieceYPosition(int area) {
		return area == 1 ? 30 : 95;
	}
	
	@Override
	protected int getAllowedWidth(int area) {
		return area == 1 ? 90 : ArcadeMonopoly.PLACE_HEIGHT;
	}	
	
	public int getPieceAreaCount() {
		return 2;
	}

	public int getPieceAreaForPiece(Piece piece) {
		return piece.isInJail() ? 1 : 0;
	}	
	
}
