package vswe.stevesvehicles.module.common.addon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import vswe.stevesvehicles.client.gui.screen.GuiVehicle;
import vswe.stevesvehicles.localization.entry.module.LocalizationVisual;
import vswe.stevesvehicles.network.DataReader;
import vswe.stevesvehicles.network.DataWriter;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.client.ResourceHelper;

public class ModuleColorizer extends ModuleAddon {
	public ModuleColorizer(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	@Override 
	public boolean hasGui() {
		return true;
	}
	
	@Override 
	public boolean hasSlots() {
		return false;
	}	
	
	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, getModuleName(), 8, 6, 0x404040);
	}

	@Override
	public int guiWidth() {
		return 125;
	}
	
	@Override
	public int guiHeight() {
		return 75;
	}	
	
	private static final int MARKER_OFFSET_X = 10;
	private static final int SCROLL_WIDTH = 64;
	private int[] getMovableMarker(int i) {
		return new int[] {MARKER_OFFSET_X + (int)(SCROLL_WIDTH *  (getColorVal(i) / 255F)) - 2, 17 + i * 20, 4, 13};
	}
	private int[] getArea(int i) {
		return new int[] {MARKER_OFFSET_X, 20 + i * 20, SCROLL_WIDTH,7};
	}	
	private int markerMoving = -1;
	

    private static final int BOX_SRC_X = 6;
    private static final int BOX_SRC_Y = 9;
    private static final int BAR_SRC_X = 1;
    private static final int BAR_SRC_Y = 1;
    private static final int MARKER_SRC_X = 1;
    private static final int MARKER_SRC_Y = 9;

    private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/gui/color.png");

	@Override
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource(TEXTURE);

		for (int i = 0; i < 3; i++) {
			drawMarker(gui, i);
		}

		float[] color = getColor();
		GL11.glColor4f(color[0], color[1], color[2], 1.0F);
		drawImage(gui, SCROLL_WIDTH + 25, 29, BOX_SRC_X, BOX_SRC_Y, 28, 28);
		GL11.glColor4f(1F, 1F, 1F, 1.0F);
	}
	
	@Override
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		String[] colorNames = new String[] {LocalizationVisual.RED.translate(), LocalizationVisual.GREEN.translate(), LocalizationVisual.BLUE.translate()};
		for (int i = 0; i < 3; i++) {
            if (markerMoving == i || (markerMoving == -1 && inRect(x, y, getArea(i)))) {
                drawStringOnMouseOver(gui, colorNames[i]  + ": " + getColorVal(i), x, y);
            }
		}
	}


	private void drawMarker(GuiVehicle gui, int id) {
		float[] colorArea = new float[3];
		float[] colorMarker = new float[3];
		for (int i = 0; i < 3; i++) {
			if (i == id) {
				colorArea[i] = 0.7F;
				colorMarker[i] = 1F;
			}else{
				colorArea[i] = 0.2F;
				colorMarker[i] = 0F;
			}
		}
		GL11.glColor4f(colorArea[0], colorArea[1], colorArea[2], 1.0F);
		drawImage(gui, getArea(id), BAR_SRC_X, BAR_SRC_Y);
		GL11.glColor4f(colorMarker[0], colorMarker[1], colorMarker[2], 1.0F);
		drawImage(gui, getMovableMarker(id), MARKER_SRC_X, MARKER_SRC_Y);
		GL11.glColor4f(1F, 1F, 1F, 1.0F);
	}
	
	@Override
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (button == 0) {
			for (int i = 0; i < 3; i++) {
				if (inRect(x,y, getMovableMarker(i)) || inRect(x, y ,getArea(i))) {
					markerMoving = i;
                    moveMarker(x);
				}
			}
		}
	}

	@Override
	public void mouseMovedOrUp(GuiVehicle gui,int x, int y, int button) {
		if(markerMoving != -1){
            moveMarker(x);
		}

        if (button != -1) {
            markerMoving = -1;
        }
	}

    private void moveMarker(int x) {
        int tempColor = (int)((x - MARKER_OFFSET_X)/(SCROLL_WIDTH /255F));

        if (tempColor < 0) {
            tempColor = 0;
        }else if (tempColor > 255) {
            tempColor = 255;
        }

        DataWriter dw = getDataWriter();
        dw.writeByte(markerMoving);
        dw.writeByte(tempColor);
        sendPacketToServer(dw);
    }
	
	
	@Override
	public int numberOfDataWatchers() {
		return 3;
	}

	@Override
	public void initDw() {
		addDw(0,255);
		addDw(1,255);
		addDw(2,255);
	}


	@Override
	protected void receivePacket(DataReader dr, EntityPlayer player) {
        int id = dr.readByte();
		if (id >= 0 && id < 3) {
			setColorVal(id, dr.readByte());
		}
	}
	
	public int getColorVal(int i) {
		if (isPlaceholder()) {
			return 255;
		}
		
		int tempVal = getDw(i);
		if (tempVal < 0) {
			tempVal += 256;
		}
		return tempVal;
	}
	
	public void setColorVal(int id, int val) {
		updateDw(id, val);
	}

	private float getColorComponent(int i) {
		return getColorVal(i) / 255F;
	}

	@Override
	public float[] getColor() {
		return new float[] {getColorComponent(0), getColorComponent(1), getColorComponent(2)};
	}
	
	@Override
	protected void save(NBTTagCompound tagCompound) {
		tagCompound.setByte("Red", (byte)getColorVal(0));
		tagCompound.setByte("Green", (byte)getColorVal(1));
		tagCompound.setByte("Blue", (byte)getColorVal(2));
	}
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		setColorVal(0, tagCompound.getByte("Red"));
		setColorVal(1, tagCompound.getByte("Green"));
		setColorVal(2, tagCompound.getByte("Blue"));
	}	
	
}