package vswe.stevesvehicles.old.Arcade;

import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.old.StevesVehicles;

public class TetrisPiece {

	private TetrisPiecePart[] parts;
	private int x;
	private int y;
	private String sound;
	private float volume;
	private int rotationOffset;
	
	private TetrisPiece(TetrisPiecePart[] parts) {
		this.parts = parts;
		this.x = 5;
		this.y = -2;
	}
	
	public static TetrisPiece createPiece(int type) {
		TetrisPiecePart[] parts;

		String sound = null;
		float volume = 0.5F;
		int rotationOffset = 0;
		switch (type) {
			case 0:
				parts = createEndermanParts();
				sound = "mob.endermen.hit";
				break;
			case 1:
				parts = createSlimeParts();
				sound = "mob.slime.big";
				rotationOffset = 1;
				break;
			case 2:
				parts = createWitherParts();
				sound = "mob.wither.hurt";
				volume = 0.25F;
				break;	
			case 3:
				parts = createWitchParts();
				sound = "mob.cat.hitt";
				break;	
			case 4:
				parts = createPigParts();
				sound = "mob.pig.say";
				break;	
			case 5:
				parts = createSteveParts();
				sound = "damage.hit";
				break;
			case 6:
				parts = createSheepParts();
				sound = "mob.sheep.say";
				break;
			default:
				return null;
		}
		
		
		TetrisPiece piece = new TetrisPiece(parts);
		piece.sound = sound;
		piece.rotationOffset = rotationOffset;
		piece.volume = volume;
		return piece;
	}
	
	//4x1 piece
	private static TetrisPiecePart[] createEndermanParts() {
		TetrisPiecePart[] parts = new TetrisPiecePart[4];
		
		parts[0] = new TetrisPiecePart(new TetrisBlock(0,0), 0, -1);
		parts[1] = new TetrisPiecePart(new TetrisBlock(0,10), 0, 0);
		parts[2] = new TetrisPiecePart(new TetrisBlock(0,20), 0, 1);
		parts[3] = new TetrisPiecePart(new TetrisBlock(0,30), 0, 2);
				
		return parts;
	}
	
	//2x2 piece
	private static TetrisPiecePart[] createSlimeParts() {
		TetrisPiecePart[] parts = new TetrisPiecePart[4];
		
		parts[0] = new TetrisPiecePart(new TetrisBlock(10,0), 0, 0);
		parts[1] = new TetrisPiecePart(new TetrisBlock(20,0), 1, 0);
		parts[2] = new TetrisPiecePart(new TetrisBlock(10,10), 0, 1);
		parts[3] = new TetrisPiecePart(new TetrisBlock(20,10), 1, 1);
				
		return parts;
	}
	
	//T-piece
	private static TetrisPiecePart[] createWitherParts() {
		TetrisPiecePart[] parts = new TetrisPiecePart[4];
		
		parts[0] = new TetrisPiecePart(new TetrisBlock(30,0), -1, 0);
		parts[1] = new TetrisPiecePart(new TetrisBlock(40,0), 0, 0);
		parts[2] = new TetrisPiecePart(new TetrisBlock(50,0), 1, 0);
		parts[3] = new TetrisPiecePart(new TetrisBlock(40,10), 0, 1);
				
		return parts;
	}
	
	//Inverted L-piece
	private static TetrisPiecePart[] createWitchParts() {
		TetrisPiecePart[] parts = new TetrisPiecePart[4];
		
		parts[0] = new TetrisPiecePart(new TetrisBlock(70,0), 0, -1);
		parts[1] = new TetrisPiecePart(new TetrisBlock(70,10), 0, 0);
		parts[2] = new TetrisPiecePart(new TetrisBlock(70,20), 0, 1);
		parts[3] = new TetrisPiecePart(new TetrisBlock(60,20), -1, 1);
				
		return parts;
	}		
		
	//L-piece
	private static TetrisPiecePart[] createPigParts() {
		TetrisPiecePart[] parts = new TetrisPiecePart[4];
		
		parts[0] = new TetrisPiecePart(new TetrisBlock(80,0), 0, -1);
		parts[1] = new TetrisPiecePart(new TetrisBlock(80,10), 0, 0);
		parts[2] = new TetrisPiecePart(new TetrisBlock(80,20), 0, 1);
		parts[3] = new TetrisPiecePart(new TetrisBlock(90,20), 1, 1);
				
		return parts;
	}		
	
	//z-piece
	private static TetrisPiecePart[] createSteveParts() {
		TetrisPiecePart[] parts = new TetrisPiecePart[4];
		
		parts[0] = new TetrisPiecePart(new TetrisBlock(100,0), -1, -1);
		parts[1] = new TetrisPiecePart(new TetrisBlock(110,0), 0, -1);
		parts[2] = new TetrisPiecePart(new TetrisBlock(110,10), 0, 0);
		parts[3] = new TetrisPiecePart(new TetrisBlock(120,10), 1, 0);
				
		return parts;
	}	
	
	//s-piece
	private static TetrisPiecePart[] createSheepParts() {
		TetrisPiecePart[] parts = new TetrisPiecePart[4];
		
		parts[0] = new TetrisPiecePart(new TetrisBlock(130,10), -1, 1);
		parts[1] = new TetrisPiecePart(new TetrisBlock(140,10), 0, 1);
		parts[2] = new TetrisPiecePart(new TetrisBlock(140,0), 0, 0);
		parts[3] = new TetrisPiecePart(new TetrisBlock(150,0), 1, 0);
				
		return parts;
	}		
	
	
	public void render(ArcadeTetris game, GuiVehicle gui) {
		for (int i = 0; i < parts.length; i++) {
			parts[i].render(game, gui, x, y);
		}
	}

	public void rotate(TetrisBlock[][] board) {
		for (int i = 0; i < parts.length; i++) {
			if (!parts[i].canRotate(board, x, y, rotationOffset)) {
				return;
			}
		}
		
		for (int i = 0; i < parts.length; i++) {
			parts[i].rotate(rotationOffset); 
		}		
	}

	public MOVE_RESULT move(ArcadeTetris game, TetrisBlock[][] board, int offX, int offY, boolean placeOnFail) {
		for (int i = 0; i < parts.length; i++) {
			if (!parts[i].canMoveTo(board, x + offX, y + offY)) {
				boolean isGameOver = false;
				if (placeOnFail) {		
					for (int j = 0; j < parts.length; j++) {
						if (parts[j].canPlaceInBoard(y)) {						
							parts[j].placeInBoard(board, x, y);
						}else{
							isGameOver = true;
						}
					}
					if (StevesVehicles.instance.useArcadeMobSounds) {
						if (sound != null) {
							ArcadeGame.playDefaultSound(sound, volume, (game.getModule().getCart().rand.nextFloat() - game.getModule().getCart().rand.nextFloat()) * 0.2F + 1.0F);
						}
					}else{
						ArcadeGame.playSound("boop", 1, 1);
					}
				}
				return isGameOver ? MOVE_RESULT.GAME_OVER : MOVE_RESULT.FAIL;
			}
		}
		
		x += offX;
		y += offY;
		return MOVE_RESULT.SUCCESS;
		
	}

	
	public static enum MOVE_RESULT {
		SUCCESS,
		FAIL,
		GAME_OVER
	}
	
}
