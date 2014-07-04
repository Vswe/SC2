package vswe.stevesvehicles.arcade.tracks;

import vswe.stevesvehicles.arcade.ArcadeGame;


public class TrackEnderHandler extends Track {
	
	private boolean isSpawner;
	public TrackEnderHandler(int x, int y, TrackOrientation orientation, boolean isSpawner) {
		super(x, y, orientation);
		this.isSpawner = isSpawner;
	}
	
	
	@Override
	public void travel(ArcadeTracks game, Cart cart) {
		if (isSpawner) {
			game.getEnderman().setAlive(true);
			game.getEnderman().setDirection(TrackOrientation.Direction.RIGHT);
			game.getEnderman().setX(cart.getX() + 5);
			game.getEnderman().setY(cart.getY());
		}else if (game.getEnderman().isAlive()){
			game.getEnderman().setAlive(false);
		}
        ArcadeGame.playDefaultSound("mob.endermen.portal", 1, 1);
	}		
	
	@Override
	public Track copy() {
		return new TrackEnderHandler(getX(), getY(), getOrientation(), isSpawner);
	}		
	
}
