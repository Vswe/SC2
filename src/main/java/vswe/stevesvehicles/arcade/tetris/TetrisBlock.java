package vswe.stevesvehicles.arcade.tetris;

import vswe.stevesvehicles.client.gui.GuiBase.RENDER_ROTATION;
import vswe.stevesvehicles.client.gui.GuiVehicle;

public class TetrisBlock {

	private int u;
	private int v;
	private RENDER_ROTATION r;
	
	
	public TetrisBlock(int u, int v) {
		this.u = u;
		this.v = v;
		this.r = RENDER_ROTATION.NORMAL;
	}
	
	
	public void render(ArcadeTetris game, GuiVehicle gui, int x, int y) {
		if (y >= 0) {
			game.getModule().drawImage(gui, ArcadeTetris.BOARD_START_X + x * 10,  ArcadeTetris.BOARD_START_Y + y * 10, u, v, 10, 10, r);
		}
	}


	public void rotate() {
		r = r.getNextRotation();
	}
	
	
}
