package vswe.stevesvehicles.module.common.attachment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.arcade.invader.ArcadeInvaders;
import vswe.stevesvehicles.arcade.sweeper.ArcadeSweeper;
import vswe.stevesvehicles.arcade.tetris.ArcadeTetris;
import vswe.stevesvehicles.arcade.tracks.ArcadeTracks;
import vswe.stevesvehicles.client.gui.screen.GuiVehicle;
import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextAdvanced;
import vswe.stevesvehicles.module.cart.attachment.ModuleAttachment;
import vswe.stevesvehicles.arcade.ArcadeGame;
import vswe.stevesvehicles.network.DataReader;
import vswe.stevesvehicles.network.DataWriter;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.client.ResourceHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class ModuleArcade extends ModuleAttachment {

    private static final List<Class<? extends ArcadeGame>> gameTypesList = new ArrayList<Class<? extends ArcadeGame>>();
    private static final Map<Class<? extends ArcadeGame>, String> gameTypes = new HashMap<Class<? extends ArcadeGame>, String>();
    public static void registerGame(String name, Class<? extends ArcadeGame> game) {
        if (!gameTypes.containsValue(name)) {
            gameTypes.put(game, name);
            gameTypesList.add(game);
        }

    }

    public String getGameName(ArcadeGame game) {
        return gameTypes.get(game.getClass());
    }

    static {
        registerGame("tracks", ArcadeTracks.class);
        registerGame("tetris", ArcadeTetris.class);
        registerGame("invaders", ArcadeInvaders.class);
        registerGame("sweeper", ArcadeSweeper.class);
        //registerGame("monopoly", ArcadeMonopoly.class);
    }

	public ModuleArcade(VehicleBase vehicleBase) {
		super(vehicleBase);
		
		games = new ArrayList<ArcadeGame>();

        for (Class<? extends ArcadeGame> gameType : gameTypesList) {
            ArcadeGame.createGame(this, gameType, games);
        }
	}

	private ArrayList<ArcadeGame> games;
	private ArcadeGame currentGame;
	private int afkTimer;

	private boolean isGameActive() {
		return getVehicle().getWorld().isRemote && currentGame != null;
	}
	
	
	@Override
	public boolean doStealInterface() {
		return isGameActive();
	}	

	@Override
	public boolean hasSlots() {
		return false;
	}	

	@Override
	public boolean hasGui() {
		return true;
	}
	
	@Override
	public int guiWidth() {
		return 190;
	}
	@Override
	public int guiHeight() {
		return 115;
	}	
	
	@Override
	public void update() {
		if (isGameActive() && afkTimer < 10) {	
			currentGame.update();
			afkTimer++;
		}
	}
	
	@Override
	public void drawForeground(GuiVehicle gui) {
		if (isGameActive()) {
			currentGame.drawForeground(gui);
		}else{
		    drawString(gui, getModuleName(), 8, 6, 0x404040);
		    
			for (int i = 0; i < games.size(); i++) {
				int[] text = getButtonTextArea(i);
				
				if (text[3] == 8) {				
					drawString(gui, games.get(i).getName(), text[0], text[1], 0x404040);
				}
			}

            if (games.size() > GAMES_PER_PAGE) {
                int pages = (int)Math.ceil((float) games.size() / GAMES_PER_PAGE);
                drawString(gui, temp.translate(String.valueOf(currentPage + 1), String.valueOf(pages)), ARROW_X + ARROW_WIDTH, ARROW_Y + 5, ARROW_SPACE_WIDTH, true, 0x404040);
            }
		}
	}
	
    private int currentPage;
    private ILocalizedText temp = new LocalizedTextAdvanced("Page [%1] of [%2]"); //TODO localize

    private static final int GAMES_PER_PAGE = 4;
    private static final int ARROW_SRC_X = 35;
    private static final int ARROW_SRC_Y = 90;
    private static final int ARROW_WIDTH = 9;
    private static final int ARROW_HEIGHT = 16;
    private static final int ARROW_SPACE_WIDTH = 75;
    private static final int ARROW_X = 90;
    private static final int ARROW_Y = 2;
    private static final int[] LEFT_ARROW = {ARROW_X, ARROW_Y, ARROW_WIDTH, ARROW_HEIGHT};
    private static final int[] RIGHT_ARROW = {ARROW_X + ARROW_WIDTH + ARROW_SPACE_WIDTH, ARROW_Y, ARROW_WIDTH, ARROW_HEIGHT};

    private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/gui/arcade.png");
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource(TEXTURE);
		afkTimer = 0;
		
		if (isGameActive()) {
			int srcX = 1 + (inRect(x, y, EXIT_AREA) ? 17 : 0);
			int srcY = 90;
			
			drawImage(gui, EXIT_AREA, srcX, srcY);
			
			currentGame.drawBackground(gui, x, y);			
		}else{
			drawImage(gui, LIST_AREA, 1, 1);

            int start = currentPage * GAMES_PER_PAGE;
            int end = Math.min(start + GAMES_PER_PAGE, games.size());
			for (int i = start; i < end; i++) {
                int pos = i - start;
				int[] button = getButtonGraphicArea(pos);
				
				int srcX = 1;
				int srcY = 107 + (inRect(x, y, getButtonBoundsArea(pos)) ? button[3] + 1 : 0);
							

                drawImage(gui, button, srcX, srcY);

                int[] icon = getButtonIconArea(pos);
                ResourceHelper.bindResource(games.get(i).getIconResource());
                setTextureSize(16);
                drawImage(gui, icon, 0, 0);
                resetTextureSize();
                ResourceHelper.bindResource(TEXTURE);

			}

            if (games.size() > GAMES_PER_PAGE) {
                drawArrow(gui, LEFT_ARROW, x, y, true);
                drawArrow(gui, RIGHT_ARROW, x, y, false);
            }
		}
	}

    private void drawArrow(GuiVehicle gui, int[] bounds, int x, int y, boolean left) {
        int id = (left ? 0 : 1) + (inRect(x, y, bounds) ? 2 : 0);
        int srcX = ARROW_SRC_X + (ARROW_WIDTH + 1) * id;

        drawImage(gui, bounds, srcX, ARROW_SRC_Y);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		if (isGameActive()) {
			drawStringOnMouseOver(gui, "Exit", x, y, EXIT_AREA); //TODO localize
			currentGame.drawMouseOver(gui, x, y);
		}
	}	
	


    private static final int[] EXIT_AREA = new int[] {455, 6, 16, 16};
    private static final int[] LIST_AREA = new int[] {15, 20, 170, 88};


	
	private int[] getButtonBoundsArea(int i) {
		return getButtonArea(i, false);
	}
	
	private int[] getButtonGraphicArea(int i) {
		return getButtonArea(i, true);
	}	
	
	private int[] getButtonArea(int i, boolean graphic) {
		return new int[] {LIST_AREA[0] + 2, LIST_AREA[1] + 2 + i * 21, 166, graphic ? 21 : 20};
	}


	private int[] getButtonTextArea(int i) {
		int[] button = getButtonGraphicArea(i);
		
		
		return new int[] {button[0] + 24, button[1] + 6, button[2], 8};
	}
	
	private int[] getButtonIconArea(int i) {
		int[] button = getButtonGraphicArea(i);
		
		return new int[] {button[0] + 2, button[1] + 2, 16, 16}; 
	}
	
	@Override
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (isGameActive()) {
			if (button == 0 && inRect(x,y, EXIT_AREA)) {
				currentGame.unload(gui);
				currentGame = null;
			}else{
				currentGame.mouseClicked(gui, x, y, button);
			}
		}else{
			if (button == 0) {
                int start = currentPage * GAMES_PER_PAGE;
                int end = Math.min(start + GAMES_PER_PAGE, games.size());
                for (int i = start; i < end; i++) {
					if (inRect(x,y, getButtonBoundsArea(i - start))) {
						currentGame = games.get(i);
						currentGame.load(gui);
						break;
					}
				}

                if (games.size() > GAMES_PER_PAGE) {
                    int pages = (int)Math.ceil((float) games.size() / GAMES_PER_PAGE);
                    if (inRect(x, y, LEFT_ARROW)) {
                        currentPage--;
                        if(currentPage < 0) {
                            currentPage = pages - 1;
                        }
                    }else if(inRect(x, y, RIGHT_ARROW)) {
                        currentPage++;
                        if (currentPage >= pages) {
                            currentPage = 0;
                        }
                    }
                }
			}
		}
	}
	
	@Override
	public void mouseMovedOrUp(GuiVehicle gui,int x, int y, int button) {
		if (isGameActive()) {
			currentGame.mouseMovedOrUp(gui, x, y, button);
		}
	}
	
	@Override
	public void keyPress(GuiVehicle gui, char character, int extraInformation) {
		if (isGameActive()) {
			currentGame.keyPress(gui, character, extraInformation);
		}		
	}

	@Override
	protected void save(NBTTagCompound tagCompound) {
		for(ArcadeGame game : games) {
            NBTTagCompound gameCompound = new NBTTagCompound();
			game.save(gameCompound);
            tagCompound.setTag(getGameName(game), gameCompound);
		}
	}	
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		for(ArcadeGame game : games) {
            String name = getGameName(game);
            if (tagCompound.hasKey(name)) {
			    game.load(tagCompound.getCompoundTag(name));
            }
		}
	}	

	@Override
	protected void receivePacket(DataReader dr, EntityPlayer player) {
        ArcadeGame.delegateReceivedPacket(games, dr, player);
	}		
	
	@Override
	public int numberOfGuiData() {
        int count = 0;
        for (ArcadeGame game : games) {
            game.setGuiDataOffset(count);
            count += game.numberOfGuiData();
        }
        return count;
	}	
	
	@Override
	protected void checkGuiData(Object[] info) {
		for(ArcadeGame game : games) {
			game.checkGuiData(info);
		}						
	}
	
	@Override
	public void receiveGuiData(int id, short data) {
        ArcadeGame.delegateReceivedGuiData(games, id, data);
	}	
	
	@Override
	public boolean disableStandardKeyFunctionality() {
        return currentGame != null && currentGame.disableStandardKeyFunctionality();

    }


    @Override
    public DataWriter getDataWriter() {
        return super.getDataWriter();
    }
}
