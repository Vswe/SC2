package vswe.stevescarts.old.Modules.Addons;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import vswe.stevescarts.client.interfaces.GuiVehicle;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.Helpers.Localization;
import vswe.stevescarts.old.Helpers.ResourceHelper;

public class ModuleColorizer extends ModuleAddon {
	public ModuleColorizer(EntityModularCart cart) {
		super(cart);
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
	
	private int markerOffsetX = 10;
	private int scrollWidth = 64;
	private int[] getMovableMarker(int i) {
		return new int[] {markerOffsetX + (int)(scrollWidth *  (getColorVal(i) / 255F)) - 2, 17 + i * 20, 4, 13};
	}
	private int[] getArea(int i) {
		return new int[] {markerOffsetX, 20 + i * 20,scrollWidth,7};
	}	
	private int markerMoving = -1;
	

	
	@Override
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource("/gui/color.png");

		for (int i = 0; i < 3; i++) {
			drawMarker(gui, x, y, i);
		}

		float[] color = getColor();
		GL11.glColor4f(color[0], color[1], color[2], 1.0F);
		drawImage(gui, scrollWidth + 25, 29, 4, 7, 28, 28);	
		GL11.glColor4f(1F, 1F, 1F, 1.0F);
	}
	
	@Override
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		String[] colorNames = new String[] {Localization.MODULES.ADDONS.COLOR_RED.translate(),Localization.MODULES.ADDONS.COLOR_GREEN.translate(), Localization.MODULES.ADDONS.COLOR_BLUE.translate()};
		for (int i = 0; i < 3; i++) {
			drawStringOnMouseOver(gui, colorNames[i] + ": " + getColorVal(i), x,y, getArea(i));
		}
	}
	
	private void drawMarker(GuiVehicle gui, int x, int y, int id) {
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
		drawImage(gui, getArea(id), 0, 0);
		GL11.glColor4f(colorMarker[0], colorMarker[1], colorMarker[2], 1.0F);
		drawImage(gui, getMovableMarker(id), 0,7);
		GL11.glColor4f(1F, 1F, 1F, 1.0F);
	}
	
	@Override
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (button == 0) {
			for (int i = 0; i < 3; i++) {
				if (inRect(x,y, getMovableMarker(i))) {
					markerMoving = i;
				}
			}
		}
	}

	@Override
	public void mouseMovedOrUp(GuiVehicle gui,int x, int y, int button) {
		if(markerMoving != -1){
            int tempColor = (int)((x - markerOffsetX)/(scrollWidth/255F));

            if (tempColor < 0)
            {
                tempColor = 0;
            }
            else if (tempColor > 255)
            {
               tempColor = 255;
            }

			sendPacket(markerMoving, (byte)tempColor);
		}

        if (button != -1)
        {
            markerMoving = -1;
        }
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
	public int numberOfPackets() {
		return 3;
	}

	@Override
	protected void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id >= 0 && id < 3) {
			setColorVal(id, data[0]);
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
	protected void Save(NBTTagCompound tagCompound, int id) {
		tagCompound.setByte(generateNBTName("Red",id), (byte)getColorVal(0));
		tagCompound.setByte(generateNBTName("Green",id), (byte)getColorVal(1));
		tagCompound.setByte(generateNBTName("Blue",id), (byte)getColorVal(2));
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		setColorVal(0,tagCompound.getByte(generateNBTName("Red",id)));
		setColorVal(1,tagCompound.getByte(generateNBTName("Green",id)));
		setColorVal(2,tagCompound.getByte(generateNBTName("Blue",id)));					
	}	
	
}