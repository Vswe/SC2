package vswe.stevesvehicles.arcade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.client.gui.screen.GuiVehicle;
import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.network.DataReader;
import vswe.stevesvehicles.network.DataWriter;
import vswe.stevesvehicles.network.PacketHandler;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.old.StevesVehicles;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;
import vswe.stevesvehicles.old.Helpers.SoundHandler;
import vswe.stevesvehicles.module.common.attachment.ModuleArcade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.lang.reflect.Constructor;
import java.util.ArrayList;


public abstract class ArcadeGame {
	
	private ModuleArcade module;
	private ILocalizedText name;

	
	public ArcadeGame(ModuleArcade module, ILocalizedText name) {
		this.name = name;
		this.module = module;
	}

	
	public String getName() {
		return name.translate();
	}
	
	public ModuleArcade getModule() {
		return module;
	}
	
	@SideOnly(Side.CLIENT)
	public void update() {
        if (StevesVehicles.instance.useArcadeSounds) {
            ((EntityModularCart)getModule().getVehicle().getEntity()).silent(); //TODO
        }
	}
	
	
	@SideOnly(Side.CLIENT)
	public void drawForeground(GuiVehicle gui) {}
	
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {}
	
	@SideOnly(Side.CLIENT)
	public void drawMouseOver(GuiVehicle gui, int x, int y) {}
	
	@SideOnly(Side.CLIENT)
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {}
	
	@SideOnly(Side.CLIENT)
	public void mouseMovedOrUp(GuiVehicle gui,int x, int y, int button) {}
	
	@SideOnly(Side.CLIENT)
	public void keyPress(GuiVehicle gui, char character, int extraInformation) {}
	
	public void save(NBTTagCompound tagCompound) {}
	public void load(NBTTagCompound tagCompound) {}
	public void receivePacket(DataReader dr, EntityPlayer player) {}
	public void checkGuiData(Object[] info) {}
	public void receiveGuiData(int id, short data) {}


	public boolean disableStandardKeyFunctionality() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public static void playSound(String sound, float volume, float pitch) {
		if (StevesVehicles.instance.useArcadeSounds && sound != null) {
			SoundHandler.playSound(sound, volume, pitch);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void playDefaultSound(String sound, float volume, float pitch) {
		if (StevesVehicles.instance.useArcadeSounds && sound != null) {
			SoundHandler.playDefaultSound(sound, volume, pitch);
		}
	}	
	
	public boolean allowKeyRepeat() {
		return false;
	}


	public void load(GuiVehicle gui) {
		gui.enableKeyRepeat(allowKeyRepeat());
	}
	
	public void unload(GuiVehicle gui) {
		if (allowKeyRepeat()) {
			gui.enableKeyRepeat(false);
		}
	}	
	
	public void drawImageInArea(GuiVehicle gui, int x, int y, int u, int v, int w, int h) {
		drawImageInArea(gui, x, y, u, v, w, h, 5, 4, vswe.stevesvehicles.vehicle.VehicleBase.MODULAR_SPACE_WIDTH,  vswe.stevesvehicles.vehicle.VehicleBase.MODULAR_SPACE_HEIGHT);
	}
	
	public void drawImageInArea(GuiVehicle gui, int x, int y, int u, int v, int w, int h, int x1, int y1, int x2, int y2) {
		if (x < x1) {
			w -= x1 - x;
			u += x1 - x;
			x = x1;
		}else if(x + w > x2) {
			w = x2 - x;
		}
		
		if (y < y1) {
			h -= y1 - y;
			v += y1 - y;
			y = y1;
		}else if(y + h > y2) {
			h = y2 - y;
		}
		
				
		if (w > 0 && h > 0) {		
			getModule().drawImage(gui, 
					x, 
					y, 
					u,
					v,
					w,
					h
				);	
		}		
	}

    private int id;
    public static void createGame(ModuleArcade module, Class<? extends ArcadeGame> gameType, ArrayList<ArcadeGame> games) {
        try {
            Constructor<? extends ArcadeGame> constructor = gameType.getConstructor(ModuleArcade.class);
            Object obj = constructor.newInstance(module);
            ArcadeGame game = (ArcadeGame)obj;
            game.id = games.size();
            games.add(game);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private int guiDataOffset;
    public int numberOfGuiData() {
        return 0;
    }

    public void setGuiDataOffset(int offset) {
        guiDataOffset = offset;
    }

    protected final void updateGuiData(Object[] info, int id, short data) {
        getModule().updateGuiData(info, id + guiDataOffset, data);
    }

    public static void delegateReceivedGuiData(ArrayList<ArcadeGame> games, int id, short data) {
        for(ArcadeGame game : games) {
            if (id < game.numberOfGuiData()) {
                game.receiveGuiData(id, data);
                break;
            }else{
                id -= game.numberOfGuiData();
            }
        }
    }

    public static void delegateReceivedPacket(ArrayList<ArcadeGame> games, DataReader dr, EntityPlayer player) {
        int id = dr.readByte();
        games.get(id).receivePacket(dr, player);
    }

    protected DataWriter getDataWriter() {
        DataWriter dw = getModule().getDataWriter();
        dw.writeByte(this.id);
        return dw;
    }

    protected void sendPacketToServer(DataWriter dw) {
        PacketHandler.sendPacketToServer(dw);
    }

    protected void sendPacketToPlayer(DataWriter dw, EntityPlayer player) {
        PacketHandler.sendPacketToPlayer(dw, player);
    }

    private ResourceLocation texture;
    public ResourceLocation getIconResource() {
        if (texture == null) {
            texture = ResourceHelper.getResource("/gui/arcade/" + getModule().getGameName(this) + ".png");
        }
        return texture;
    }
}
