package vswe.stevesvehicles.arcade.tetris;

import vswe.stevesvehicles.client.gui.screen.GuiVehicle;

public class TetrisPiecePart {

	private TetrisBlock block;
	private int offX;
	private int offY;
	
	public TetrisPiecePart(TetrisBlock block, int offX, int offY) {
		this.block = block;
		this.offX = offX;
		this.offY = offY;
	}

	public void render(ArcadeTetris game, GuiVehicle gui, int x, int y) {
		block.render(game, gui, x + offX, y + offY);
	}

	public void rotate(int offSet) {
		block.rotate();
		
		int temp = offX;
		offX = -offY + offSet;
		offY = temp;
	}
	
	
	public void placeInBoard(TetrisBlock[][] board, int x, int y) {
		board[x+offX][y+offY] = block;
	}

	public boolean canMoveTo(TetrisBlock[][] board, int x, int y) {
		return isValidAt(board, x + offX, y + offY);
	}
	
	public boolean isValidAt(TetrisBlock[][] board, int x, int y) {
		if (x < 0 || x  >= board.length || y  >= board[0].length) {
			return false;
		}else if (y < 0){
			return true;
		}

		return board[x][y] == null;
	}

	public boolean canRotate(TetrisBlock[][] board, int x, int y, int offSet) {
		return isValidAt(board, x - offY + offSet, y + offX);
	}

	public boolean canPlaceInBoard(int y) {
		return y + offY >= 0;
	}
	
	
}
