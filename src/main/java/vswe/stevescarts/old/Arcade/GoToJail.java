package vswe.stevescarts.old.Arcade;

public class GoToJail extends CornerPlace {

	public GoToJail(ArcadeMonopoly game) {
		super(game, 3);
	}

	
	@Override
	public boolean onPieceStop(Piece piece) {
		piece.goToJail();
		
		return super.onPieceStop(piece);
	}
}
