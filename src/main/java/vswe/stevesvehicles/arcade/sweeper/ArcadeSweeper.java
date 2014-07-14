package vswe.stevesvehicles.arcade.sweeper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.arcade.ArcadeGame;
import vswe.stevesvehicles.client.gui.screen.GuiVehicle;
import vswe.stevesvehicles.arcade.sweeper.Tile.TILE_STATE;
import vswe.stevesvehicles.localization.entry.arcade.LocalizationSweeper;
import vswe.stevesvehicles.network.DataReader;
import vswe.stevesvehicles.network.DataWriter;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.module.common.attachment.ModuleArcade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ArcadeSweeper extends ArcadeGame {

	public ArcadeSweeper(ModuleArcade module) {
		super(module, LocalizationSweeper.TITLE);
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
			int x = getModule().getVehicle().getRandom().nextInt(width);
			int y = getModule().getVehicle().getRandom().nextInt(height);
			
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
		return (VehicleBase.MODULAR_SPACE_WIDTH - tiles.length * 10) / 2;
	}
	
	private int getMarginTop() {
		return (VehicleBase.MODULAR_SPACE_HEIGHT - tiles[0].length * 10) / 2;
	}
	
	private static final String TEXTURE_MENU = "/gui/sweeper.png";
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource(TEXTURE_MENU);
		
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
			Tile.TileOpenResult result = tiles[x][y].open();
			
			if (emptyLeft == 0) {
				hasFinished = true;
				isPlaying = false;
				playSound("goodjob", 1, 1);
				if (highscore[currentGameType] > ticks / 20) {
					highscoreTicks = 1;
					int val = ticks / 20;

                    DataWriter dw = getDataWriter();
                    dw.writeByte(currentGameType);
                    dw.writeShort(val);
                    sendPacketToServer(dw);
				}
			}else{
				if (result == Tile.TileOpenResult.BLOB) {
					if (first) {
						playSound("blobclick", 1, 1);
					}
					
					for (int i = -1; i <= 1; i++) {
						for (int j = -1; j <= 1; j++) {
							openTile(x+i, y+j, false);
						}
					}
				}else if(result == Tile.TileOpenResult.DEAD) {
					isPlaying = false;					
					playDefaultSound("random.explode", 1.0F, (1.0F + (getModule().getVehicle().getRandom().nextFloat() - getModule().getVehicle().getRandom().nextFloat()) * 0.2F) * 0.7F);
				}else if(result == Tile.TileOpenResult.OK && first) {
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
		String[] mapNames = new String[] {LocalizationSweeper.MAP_TINY.translate(), LocalizationSweeper.MAP_MEDIUM.translate(), LocalizationSweeper.MAP_LARGE.translate()};
		
		getModule().drawString(gui, LocalizationSweeper.REMAINING.translate(String.valueOf(creepersLeft)), 10, 180, 0x404040);
		getModule().drawString(gui, LocalizationSweeper.TIME.translate(String.valueOf(ticks / 20)), 10, 190, 0x404040);
		
		getModule().drawString(gui, "R - " + LocalizationSweeper.RESTART.translate(), 10, 210, 0x404040);
		
		getModule().drawString(gui, "T - " + LocalizationSweeper.CHANGE_MAP.translate(), 10, 230, 0x404040);
		getModule().drawString(gui, LocalizationSweeper.CURRENT_MAP.translate(mapNames[currentGameType]), 10, 240, 0x404040);
		
		
		getModule().drawString(gui, LocalizationSweeper.HIGH_SCORE_TITLE.translate(), 330, 180, 0x404040);
		for (int i = 0; i < 3; i++) {
			getModule().drawString(gui, LocalizationSweeper.HIGH_SCORE_ENTRY.translate(mapNames[i], String.valueOf(highscore[i])), 330, 190 + i * 10, 0x404040);
		}

	}
	
	@Override
	public void receivePacket(DataReader dr, EntityPlayer player) {
	    highscore[dr.readByte()] = dr.readShort();
	}	
	
	
	@Override
	public void checkGuiData(Object[] info) {
		for (int i = 0; i < 3; i++) {
			updateGuiData(info, i, (short) (highscore[i]));
		}
	}

    @Override
    public int numberOfGuiData() {
        return 3;
    }

    @Override
	public void receiveGuiData(int id, short data) {
		if (id >= 0 && id < 3) {
			highscore[id] = data;
		}
	}	
	
	@Override
	public void save(NBTTagCompound tagCompound) {
		for (int i = 0; i < 3; i++) {
			tagCompound.setShort("Highscore" + i, (short)highscore[i]);
		}
	}
	
	@Override
	public void load(NBTTagCompound tagCompound) {
		for (int i = 0; i < 3; i++) {
			highscore[i] = tagCompound.getShort("Highscore" + i);
		}
	}		
}
