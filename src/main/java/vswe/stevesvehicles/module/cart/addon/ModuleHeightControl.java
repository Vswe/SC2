package vswe.stevesvehicles.module.cart.addon;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.module.common.addon.ModuleAddon;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.HeightControlOre;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;

public class ModuleHeightControl extends ModuleAddon {
	public ModuleHeightControl(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	@Override
	public boolean hasSlots() {
		return false;
	}

	@Override
	public boolean hasGui(){
		return true;
	}

	@Override
	public int guiWidth() {
		return Math.max(100, ORE_MAP_X + 5 +  HeightControlOre.ores.size() * 4);
	}

	@Override
	public int guiHeight() {
		return 65;
	}

	private static final int LEVEL_NUMBER_BOX_X = 8;
	private static final int LEVEL_NUMBER_BOX_Y = 18;
	private static final int[] ARROW_UP = new int[] {9, 36, 17, 9};
	private static final int[] ARROW_MIDDLE = new int[] {9, 46, 17, 6};
	private static final int[] ARROW_DOWN = new int[] {9, 53, 17, 9};
	private static final int ORE_MAP_X = 40;
	private static final int ORE_MAP_Y = 18;

	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, getModuleName(), 8, 6, 0x404040);

        String s = String.valueOf(getYTarget());
        int x = LEVEL_NUMBER_BOX_X + 6;
        int color = 0xFFFFFF;

        if (getYTarget() >= 100)
        {
            x -= 4;
        }
        else if (getYTarget() < 10)
        {
            x += 3;

            if (getYTarget() < 5)
            {
                color = 0xFF0000;
            }
        }

        drawString(gui,s, x, LEVEL_NUMBER_BOX_Y + 5, color);
	}

		
	
	@Override
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource("/gui/heightcontrol.png");

		//draw the box for the numbers
		drawImage(gui, LEVEL_NUMBER_BOX_X, LEVEL_NUMBER_BOX_Y, 4, 36, 21, 15);

		//draw the controls
		drawImage(gui, ARROW_UP, 4, 12);
		drawImage(gui, ARROW_MIDDLE, 4, 21);
		drawImage(gui, ARROW_DOWN, 4, 27);

		//draw the ores map
		for (int i = 0; i < HeightControlOre.ores.size(); i++)
        {
			HeightControlOre ore = HeightControlOre.ores.get(i);

            for (int j = 0; j < 11; j++)
            {
                int altitude = getYTarget() - j + 5;
                boolean empty = !(ore.spanLowest <= altitude && altitude <= ore.spanHighest);
                boolean high = ore.bestLowest <= altitude && altitude <= ore.bestHighest;
                int srcY;
				int srcX;

                if (empty)
                {
                    srcY = 0;
					srcX = 0;
                }else{
					srcX = ore.srcX;
                    srcY = ore.srcY;

					if (high) {
						srcY += 4;
					}

					/*if (!ore.useDefaultTexture) {
						loadTexture(gui, ore.specialTexture, true);
					}*/
				}

                drawImage(gui, ORE_MAP_X + i * 4, ORE_MAP_Y + j * 4, srcX, srcY, 4, 4);

				/*if (!ore.useDefaultTexture && !empty) {
					loadTexture(gui,"heightcontrol.png");
				}*/
            }
        }

		//draw the markers
		if (getYTarget() != getVehicle().y()) {
			drawMarker(gui,5,false);
			//drawTexturedModalRect(j + 110 - 1, k + 75 + 5 * 4 - 1, 180, 0, 26, 6);
		}
        int pos = getYTarget() + 5 - getVehicle().y();

        if (pos >= 0 && pos < 11){
			drawMarker(gui,pos,true);
            //drawTexturedModalRect(j + 110 - 1, k + 75 + pos * 4 - 1, 180, 6, 26, 6);
        }
	}

	private void drawMarker(GuiVehicle gui, int pos, boolean isTargetLevel) {
		int srcX = 4;
		int srcY = isTargetLevel ? 6 : 0;

		drawImage(gui, ORE_MAP_X - 1, ORE_MAP_Y + pos * 4 - 1, srcX, srcY, 1, 6);
		for (int i = 0; i < HeightControlOre.ores.size();i++) {
			drawImage(gui, ORE_MAP_X + i * 4, ORE_MAP_Y + pos * 4 - 1, srcX + 1, srcY, 4, 6);
		}
		drawImage(gui, ORE_MAP_X + HeightControlOre.ores.size() * 4, ORE_MAP_Y + pos * 4 - 1, srcX + 5, srcY, 1, 6);
	}

	@Override
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (button == 0) {
			byte packetData = 0;
			if (inRect(x,y, ARROW_MIDDLE)) {
				//set bit 0 to 1
				packetData |= 1;
			}else {
				if (inRect(x,y, ARROW_UP)) {
					//set bit 1 to 0
                    packetData &= ~1;
				}else if (inRect(x,y, ARROW_DOWN)) {
					//set bit 1 to 1
					packetData |= 2;
				}else{
					return;
				}
				if (GuiScreen.isShiftKeyDown()) {
					//set bit 2 to 1
					packetData |= 4;
				}
			}

			sendPacket(0, packetData);
		}
	}

	@Override
	protected void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id == 0) {
			byte info = data[0];

			if ((info & 1) != 0) {
				setYTarget(getVehicle().y());
			}else{
				int multiplier;
				int dif;
				if ((info & 2) == 0) {
					multiplier = 1;
				}else{
					multiplier = -1;
				}

				if ((info & 4) == 0) {
					dif = 1;
				}else{
					dif = 10;
				}

				int targetY = getYTarget();

				targetY += multiplier * dif;
				if (targetY < 0) {
					targetY = 0;
				}else if (targetY > 255) {
					targetY = 255;
				}

				setYTarget(targetY);
			}
		}
	}

	@Override
	public int numberOfPackets() {
		return 1;
	}

	@Override
	public int numberOfDataWatchers() {
		return 1;
	}

	@Override
	public void initDw() {
		addDw(0, getVehicle().y());
	}

	public void setYTarget(int val) {
		updateDw(0,val);
	}

	@Override
	public int getYTarget() {
		if (isPlaceholder()) {
			return 64;
		}
	
		int data = getDw(0);
		if (data < 0) {
			data+=256;
		}
		return data;
	}
	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		tagCompound.setShort("Height", (short)getYTarget());
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		setYTarget(tagCompound.getShort("Height"));
	}	
}