package vswe.stevesvehicles.arcade.monopoly;

public abstract class Button {

	
	public Button() {

	}
	
	public String getName() {
		return "Undefined";
	}
	
	public boolean isVisible() {
		return false;
	}
	
	public boolean isEnabled() {
		return false;
	}	
	
	public boolean isVisibleForPlayer() {
		return true;
	}
	
	public void onClick() {

	}

	public boolean isReallyEnabled(ArcadeMonopoly game) {
		return game.getCurrentPiece().getController() == Piece.ControlledBy.PLAYER && isEnabled();
	}

	
	public boolean isReallyVisible(ArcadeMonopoly game) {
		return isVisibleForPlayer() && isVisible();
	}
}
