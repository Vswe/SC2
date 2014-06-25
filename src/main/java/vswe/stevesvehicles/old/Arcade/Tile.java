package vswe.stevesvehicles.old.Arcade;

import vswe.stevesvehicles.client.interfaces.GuiVehicle;

public class Tile {

	
	private int nearbyCreepers;
	private TILE_STATE state;
	private ArcadeSweeper game;

	
	public Tile(ArcadeSweeper game) {
		state = TILE_STATE.CLOSED;	
		this.game = game;
	}
	
	public void setCreeper() {
		nearbyCreepers = 9;
	}
	
	public void setNearbyCreepers(int val) {
		nearbyCreepers = val;
	}
	
	
	public static enum TILE_STATE {
		CLOSED,
		OPENED,
		FLAGGED,
		MARKED
	}
	
	public static enum TILE_OPEN_RESULT {
		OK,
		BLOB,
		FAILED,
		DEAD
	}


	public boolean isCreeper() {
		return nearbyCreepers == 9;
	}

	public void draw(ArcadeSweeper game, GuiVehicle gui, int x, int y, int mx, int my) {
		int[] rect = new int[] {x, y, 10, 10};
		
		if (isCreeper() && game.hasFinished) {			
			game.getModule().drawImage(gui, rect, 30, 0);
		}else{
		
			int u = (isOpen() || (state == TILE_STATE.FLAGGED && !isCreeper() && !game.isPlaying && !game.hasFinished)) ? 0 : game.getModule().inRect(mx, my, rect) ? 20 : 10;
			
			game.getModule().drawImage(gui, rect, u, 0);
			
			if (isOpen() && nearbyCreepers != 0) {
				game.getModule().drawImage(gui, x + 1, y + 1, (nearbyCreepers - 1) * 8, 11, 8, 8);
			}
			
			if(state == TILE_STATE.FLAGGED) {
				if (!game.isPlaying && !isCreeper()) {
					game.getModule().drawImage(gui, x + 1, y + 1, 16, 20, 8, 8);
				}else{
					game.getModule().drawImage(gui, x + 1, y + 1, 0, 20, 8, 8);
				}
			}else if(state == TILE_STATE.MARKED) {
				game.getModule().drawImage(gui, x + 1, y + 1, 8, 20, 8, 8);
			}
		}
	}
	
	private boolean isOpen() {
		return (isCreeper()  && !game.isPlaying && !game.hasFinished) || state == TILE_STATE.OPENED;
	}
	
	public TILE_OPEN_RESULT open() {
		if (state != TILE_STATE.OPENED && state != TILE_STATE.FLAGGED) {			
			state = TILE_STATE.OPENED;
			if (nearbyCreepers == 0) {
				game.emptyLeft--;
				return TILE_OPEN_RESULT.BLOB;
			}else if (isCreeper()) {
				return TILE_OPEN_RESULT.DEAD;
			}else{
				game.emptyLeft--;
				return TILE_OPEN_RESULT.OK;
			}
		}else{
			return TILE_OPEN_RESULT.FAILED;
		}		
	}

	public void mark() {
		switch (state) {
			case CLOSED:
				state = TILE_STATE.FLAGGED;
				game.creepersLeft--;
				break;
			case FLAGGED:
				state = TILE_STATE.MARKED;
				game.creepersLeft++;
				break;
			case MARKED:
				state = TILE_STATE.CLOSED;
				break;
			default:
		}	
	}
	
	public TILE_STATE getState() {
		return state;
	}
	
	public void setState(TILE_STATE state) {
		this.state = state;
	}
	
	public int getNearbyCreepers() {
		return nearbyCreepers;
	}
	
	
	
	
}
