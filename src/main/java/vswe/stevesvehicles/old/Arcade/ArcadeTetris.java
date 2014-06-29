package vswe.stevesvehicles.old.Arcade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.old.Arcade.TetrisPiece.MOVE_RESULT;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleArcade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ArcadeTetris extends ArcadeGame {

	
	public ArcadeTetris(ModuleArcade module) {
		super(module, Localization.ARCADE.STACKER);
		
		newgame();
	}

	 
	private TetrisBlock[][] board;
	private TetrisPiece piece;
	private static String[] removalSounds = new String[] {"1lines", "2lines", "3lines", "4lines"};

	private void newgame() {
		board = new TetrisBlock[10][15];	
		generatePiece();
		isPlaying = true;
		ticks = 0;
		quickMove = false;
		score = 0;
		removed = 0;
		removedByAmount = new int[4];
		delay = 10;
		piecesSinceDelayChange = 0;
		newHighScore = false;
	}
	
	private void generatePiece() {
		piece = TetrisPiece.createPiece(getModule().getVehicle().getRandom().nextInt(7));
	}
	
	private int ticks = 0;
	private boolean isPlaying = true;
	private boolean quickMove = false;
	private int gameOverTicks;
	
	private int highscore;
	private int score;
	private int removed;
	private int[] removedByAmount;
	private int delay = 10;
	private int piecesSinceDelayChange;
	private boolean newHighScore;
	
	@Override
	@SideOnly(Side.CLIENT)
	public void update() {
		super.update();
		if (isPlaying) {
			if (ticks == 0 || quickMove) {
				if (piece != null) {
					TetrisPiece.MOVE_RESULT result = piece.move(this, board, 0, 1, true);
					if (result == MOVE_RESULT.FAIL) {
						piece = null;
						int removedCount = 0;
						for (int y = 0; y < board[0].length; y++) {
							boolean valid = true;
							for (int x = 0; x < board.length; x++) {
								if (board[x][y] == null) {
									valid = false;
									break;
								}
							}
							
							if (valid) {
								for (int y2 = y; y2 >= 0; y2--) {
									for (int x = 0; x < board.length; x++) {
										TetrisBlock value = y2 == 0 ? null : board[x][y2-1];
										board[x][y2] = value;
									}
								}
								removedCount++;
							}
						}
						
						if (removedCount > 0) {
							removed += removedCount;
							removedByAmount[removedCount - 1]++;
							score += removedCount * removedCount * 100;
							playSound(removalSounds[removedCount - 1], 1, 1);
						}
						
						quickMove = false;
						piecesSinceDelayChange++;
						if (piecesSinceDelayChange == 8) {
							piecesSinceDelayChange = 0;
							if (delay > 0) {
								delay--;
							}
						}
						
					}else if(result == MOVE_RESULT.GAME_OVER) {
						piece = null;
						isPlaying = false;
						quickMove = false;
						gameOverTicks = 0;
						newHighScore();
						playSound("gameover", 1, 1);
					}
				}else{
					generatePiece();
				}
				ticks = delay;
			}else{
				ticks--;
			}
		}else if (gameOverTicks < 170) {
			gameOverTicks = Math.min(170, gameOverTicks + 5);	
		}else if (newHighScore){
			playSound("highscore", 1, 1);
			newHighScore = false;
		}
	}
	
	public static final int BOARD_START_X = (478 - 100) / 2;
	public static final int BOARD_START_Y = (VehicleBase.MODULAR_SPACE_HEIGHT - 150) / 2;
	
	private static String texture = "/gui/tetris.png";
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource(texture);
		
		getModule().drawImage(gui, BOARD_START_X - 2, BOARD_START_Y - 2, 0, 40, 104, 154);

		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				TetrisBlock b = board[i][j];
			
				if (b != null) {
					b.render(this, gui, i, j);
				}
			}
		}
		
		if (piece != null) {
			piece.render(this, gui);
		}
		
		if (!isPlaying) {
			int graphicalValue = Math.min(gameOverTicks, 150);
			getModule().drawImage(gui, BOARD_START_X, BOARD_START_Y + 150 - graphicalValue, 104, 40, 100, graphicalValue);
			
			if (graphicalValue == 150 && getModule().inRect(x, y, new int[] {BOARD_START_X, BOARD_START_Y, 100, 150})) {
				getModule().drawImage(gui, BOARD_START_X + 24, BOARD_START_Y + 98, 0, 194, 54, 34);
			}
		}
	}


	@Override
	@SideOnly(Side.CLIENT)
	public void keyPress(GuiVehicle gui, char character, int extraInformation) {
		if (piece != null) {
			if (Character.toLowerCase(character) == 'w') {			
				piece.rotate(board);			
			}else if (Character.toLowerCase(character) == 'a') {
				piece.move(this, board, -1, 0, false);
			}else if (Character.toLowerCase(character) == 'd') {
				piece.move(this, board, 1, 0, false);
			}else if (Character.toLowerCase(character) == 's') {
				quickMove = true;
			}
		}
		
		if (Character.toLowerCase(character) == 'r') {
			newgame();
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (button == 0 && !isPlaying && gameOverTicks >= 150 && getModule().inRect(x, y, new int[] {BOARD_START_X, BOARD_START_Y, 100, 150})) {
			newgame();
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawForeground(GuiVehicle gui) {
		getModule().drawString(gui, Localization.ARCADE.HIGH_SCORE.translate(String.valueOf(highscore)), 10, 20, 0x404040);
		getModule().drawString(gui, Localization.ARCADE.SCORE.translate(String.valueOf(score)), 10, 40, 0x404040);
		getModule().drawString(gui, Localization.ARCADE.REMOVED_LINES.translate(String.valueOf(removed)), 10, 60, 0x404040);

        for (int i = 0; i < 4; i++) {
            getModule().drawString(gui, Localization.ARCADE.REMOVED_LINES_COMBO.translate(String.valueOf(i), String.valueOf(removedByAmount[i])), 10, 80 + i * 10, 0x404040);
        }

		
		getModule().drawString(gui, "W - " + Localization.ARCADE.INSTRUCTION_ROTATE.translate(), 340, 20, 0x404040);
		getModule().drawString(gui, "A - " + Localization.ARCADE.INSTRUCTION_LEFT.translate(), 340, 30, 0x404040);
		getModule().drawString(gui, "S - " + Localization.ARCADE.INSTRUCTION_DROP.translate(), 340, 40, 0x404040);
		getModule().drawString(gui, "D - " + Localization.ARCADE.INSTRUCTION_RIGHT.translate(),  340, 50, 0x404040);
		
		getModule().drawString(gui, "R - " + Localization.ARCADE.INSTRUCTION_RESTART.translate(), 340, 70, 0x404040);

	}
	

	private void newHighScore() {
		if (score > highscore) {
			int val = score / 100;
			
			byte byte1 = (byte)(val & 255);
			byte byte2 = (byte)((val & (255 << 8)) >> 8);
			
			getModule().sendPacket(1, new byte[] {byte1, byte2});
			newHighScore = true;
		}
	}
	
	@Override
	public void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id == 1) {
			short data1 = data[0];
			short data2 = data[1];
			if (data1 < 0) {
				data1 += 256;
			}
			if (data2 < 0) {
				data2 += 256;
			}
			
			highscore = (data1 | (data2 << 8)) * 100;
		}
	}
	
	@Override
	public void checkGuiData(Object[] info) {
		getModule().updateGuiData(info, TrackStory.stories.size(), (short)(highscore / 100));
	}
	
	
	@Override
	public void receiveGuiData(int id, short data) {
		if (id == TrackStory.stories.size()) {
			highscore = data * 100;
		}
	}
	
	@Override
	public void Save(NBTTagCompound tagCompound, int id) {
		tagCompound.setShort("Highscore", (short)highscore);
	}
	
	@Override
	public void Load(NBTTagCompound tagCompound, int id) {
		highscore = tagCompound.getShort("Highscore");
	}	
	
}
