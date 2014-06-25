package vswe.stevesvehicles.old.Arcade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.old.Arcade.Tile.TILE_OPEN_RESULT;
import vswe.stevesvehicles.old.Arcade.Tile.TILE_STATE;
import vswe.stevesvehicles.vehicles.entities.EntityModularCart;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleArcade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ArcadeSweeper extends ArcadeGame {

	public ArcadeSweeper(ModuleArcade module) {
		super(module, Localization.ARCADE.CREEPER);
		highscore = new int[] {999,999,999};
		newGame(currentGameType);
	}
	
	
	private Tile[][] tiles;
	protected boolean isPlaying;
	protected boolean hasFinished;
	private int currentGameType;
	private int ticks;
	protected int creepersLeft;
	protected int emptyLeft;
	private boolean hasStarted;
	private int[] highscore;
	private int highscoreTicks;
	
	private void newGame(int size) {
		switch (size) {
			case 0:
				newGame(9, 9, 10);
				break;
			case 1:
				newGame(16, 16, 40);
				break;
			case 2:
				newGame(30, 16, 99);
				break;
			default:
		}
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void update() {
		super.update();
		
		if (hasStarted && isPlaying && !hasFinished && ticks < 999 * 20) {
			ticks++;
		}
		
		if (highscoreTicks > 0) {
			highscoreTicks++;
			if (highscoreTicks == 78) {
				highscoreTicks = 0;
				playSound("highscore", 1, 1);
			}
		}
	}
	
	private void newGame(int width, int height, int totalCreepers) {	
		isPlaying = true;
		ticks = 0;
		creepersLeft = totalCreepers;
		emptyLeft = width * height - totalCreepers;
		hasStarted = false;
		hasFinished = false;
		highscoreTicks = 0;
		
		tiles = new Tile[width][height];
		
		for (int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				tiles[x][y] = new Tile(this);
			}
		}
		
		int creepers = 0;
		while (creepers < totalCreepers) {
			int x = getModule().getCart().rand.nextInt(width);
			int y = getModule().getCart().rand.nextInt(height);
			
			if (!tiles[x][y].isCreeper()) {
				tiles[x][y].setCreeper();
				creepers++;
			}
		}
		
		for (int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {	
				if (!tiles[x][y].isCreeper()) {
					int count = 0;
					for (int i = -1; i <= 1; i++) {
						for (int j = -1; j <= 1; j++) {
							if (i != 0 || j != 0) {
								int x0 = x + i;
								int y0 = y + j;
								if (x0 >= 0 && y0 >= 0 && x0 < width && y0 < height && tiles[x0][y0].isCreeper()) {
									count++;
								}
							}
						}
					}
					tiles[x][y].setNearbyCreepers(count);
				}
			}
		}
		
		
	}
	
	private int getMarginLeft() {
		return (EntityModularCart.MODULAR_SPACE_WIDTH - tiles.length * 10) / 2;
	}
	
	private int getMarginTop() {
		return (EntityModularCart.MODULAR_SPACE_HEIGHT - tiles[0].length * 10) / 2;
	}
	
	private static String textureMenu = "/gui/sweeper.png";
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource(textureMenu);
		
		for (int i = 0; i < tiles.length; i++) {
			for(int j = 0; j < tiles[0].length; j++) {	
				tiles[i][j].draw(this, gui, getMarginLeft() + i * 10, getMarginTop() + j * 10, x, y);
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (!isPlaying) {
			return;
		}
		
		x -= getMarginLeft();
		y -= getMarginTop();
		
		int xc = x / 10;
		int yc = y / 10;
		

		if (button == 0) {
			openTile(xc, yc, true);
		}else if(button == 1 && isValidCoordinate(xc, yc)) {
			hasStarted = true;
			playSound("flagclick", 1, 1);
			tiles[xc][yc].mark();
		}else if(button == 2 && isValidCoordinate(xc, yc)) {
			if (tiles[xc][yc].getState() == TILE_STATE.OPENED) {
				playSound("click", 1, 1);
				int nearby = tiles[xc][yc].getNearbyCreepers();
				if (nearby != 0) {
					for (int i = -1; i <= 1; i++) {
						for (int j = -1; j <= 1; j++) {
							if ((i != 0 || j != 0) && isValidCoordinate(xc + i, yc + j) && tiles[xc + i][yc + j].getState() == TILE_STATE.FLAGGED) {
								nearby--;
							}
						}
					}
					
					if (nearby == 0) {
						for (int i = -1; i <= 1; i++) {
							for (int j = -1; j <= 1; j++) {
								if (i != 0 || j != 0) {
									openTile(xc + i, yc + j, false);
								}
							}
						}						
					}
				}
			}
		}
		
		
	}
	
	private boolean isValidCoordinate(int x, int y) {
		return x >= 0 && y >= 0 && x < tiles.length && y < tiles[0].length;
	}
	
	private void openTile(int x, int y, boolean first) {
		if (isValidCoordinate(x, y)) {
			hasStarted = true;
			Tile.TILE_OPEN_RESULT result = tiles[x][y].open();
			
			if (emptyLeft == 0) {
				hasFinished = true;
				isPlaying = false;
				playSound("goodjob", 1, 1);
				if (highscore[currentGameType] > ticks / 20) {
					highscoreTicks = 1;
					int val = ticks / 20;
					
					
					byte byte1 = (byte)(val & 255);
					byte byte2 = (byte)((val & (255 << 8)) >> 8);					
					
					getModule().sendPacket(3, new byte[] {(byte)currentGameType, byte1, byte2});
				}
			}else{
				if (result == Tile.TILE_OPEN_RESULT.BLOB) {
					if (first) {
						playSound("blobclick", 1, 1);
					}
					
					for (int i = -1; i <= 1; i++) {
						for (int j = -1; j <= 1; j++) {
							openTile(x+i, y+j, false);
						}
					}
				}else if(result == Tile.TILE_OPEN_RESULT.DEAD) {
					isPlaying = false;					
					playDefaultSound("random.explode", 1.0F, (1.0F + (getModule().getCart().rand.nextFloat() - getModule().getCart().rand.nextFloat()) * 0.2F) * 0.7F);				
				}else if(result == TILE_OPEN_RESULT.OK && first) {
					playSound("click", 1, 1);
				}
			}
		}
	}

	
	@Override
	@SideOnly(Side.CLIENT)
	public void keyPress(GuiVehicle gui, char character, int extraInformation) {
		if (Character.toLowerCase(character) == 'r') {
			newGame(currentGameType);
		}else if(Character.toLowerCase(character) == 't') {
			currentGameType = (currentGameType + 1) % 3;
			newGame(currentGameType);
		}
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawForeground(GuiVehicle gui) {
		String[] mapnames = new String[] {Localization.ARCADE.MAP_1.translate(), Localization.ARCADE.MAP_2.translate(), Localization.ARCADE.MAP_3.translate()};
		
		getModule().drawString(gui, Localization.ARCADE.LEFT.translate(String.valueOf(creepersLeft)), 10, 180, 0x404040);
		getModule().drawString(gui, Localization.ARCADE.TIME.translate(String.valueOf(ticks / 20)), 10, 190, 0x404040);
		
		getModule().drawString(gui, "R - " + Localization.ARCADE.INSTRUCTION_RESTART.translate(), 10, 210, 0x404040);
		
		getModule().drawString(gui, "T - " + Localization.ARCADE.INSTRUCTION_CHANGE_MAP.translate(), 10, 230, 0x404040);
		getModule().drawString(gui, Localization.ARCADE.MAP.translate(mapnames[currentGameType]), 10, 240, 0x404040);
		
		
		getModule().drawString(gui, Localization.ARCADE.HIGH_SCORES.translate(), 330, 180, 0x404040);
		for (int i = 0; i < 3; i++) {
			getModule().drawString(gui, Localization.ARCADE.HIGH_SCORE_ENTRY.translate(mapnames[i], String.valueOf(highscore[i])), 330, 190 + i * 10, 0x404040);
		}

	}
	
	@Override
	public void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id == 3) {
			short data1 = data[1];
			short data2 = data[2];
			if (data1 < 0) {
				data1 += 256;
			}
			if (data2 < 0) {
				data2 += 256;
			}
			
			highscore[data[0]] = (data1 | (data2 << 8));
		}
	}	
	
	
	@Override
	public void checkGuiData(Object[] info) {
		for (int i = 0; i < 3; i++) {
			getModule().updateGuiData(info, TrackStory.stories.size() + 2 + i, (short)(highscore[i]));
		}
	}
	
	
	
	
	@Override
	public void receiveGuiData(int id, short data) {
		if (id >= TrackStory.stories.size() + 2 && id < TrackStory.stories.size() + 5) {
			highscore[id - (TrackStory.stories.size() + 2)] = data;
		}
	}	
	
	@Override
	public void Save(NBTTagCompound tagCompound, int id) {
		for (int i = 0; i < 3; i++) {
			tagCompound.setShort(getModule().generateNBTName("HighscoreSweeper" + i,id), (short)highscore[i]);	
		}
	}
	
	@Override
	public void Load(NBTTagCompound tagCompound, int id) {
		for (int i = 0; i < 3; i++) {
			highscore[i] = tagCompound.getShort(getModule().generateNBTName("HighscoreSweeper" + i,id));
		}
	}		
}
