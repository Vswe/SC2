package vswe.stevesvehicles.module.common.addon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;
import vswe.stevesvehicles.client.gui.screen.GuiVehicle;
import vswe.stevesvehicles.localization.entry.module.LocalizationVisual;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;


public class ModuleColorRandomizer extends ModuleAddon {
	private static final int[] BUTTON = new int[] {10, 26, 16, 16};
	private int cooldown;

	public ModuleColorRandomizer(VehicleBase vehicleBase) {
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
		return 100;
	}

	@Override
	public int guiHeight() {
		return 50;
	}

	@Override
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource("/gui/color_randomizer.png");

		float[] color = getColor();
		GL11.glColor4f(color[0], color[1], color[2], 1.0F);
		drawImage(gui, 50, 20, 1, 18, 28, 28);

		GL11.glColor4f(1, 1, 1, 1);
		if (inRect(x, y, BUTTON)) {
			drawImage(gui, 10, 26, 35, 1, 16, 16);
		} else {
			drawImage(gui, 10, 26, 18, 1, 16, 16);
		}
		drawImage(gui, 10, 26, 1, 1 ,16, 16);
	}

	@Override
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		if (inRect(x, y, BUTTON)) {
			String randomizeString = LocalizationVisual.RANDOMIZE.translate();
			drawStringOnMouseOver(gui, randomizeString, x, y, BUTTON);
		}
	}

	@Override
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (button == 0) {
			if (inRect(x,y, BUTTON)) {
				sendPacket(0);
			}
		}
	}

	@Override
	public void activatedByRail(int x, int y, int z, boolean active) {
		if (active && cooldown == 0) {
			randomizeColor();
			cooldown = 5;
		}
	}

	@Override
	public void update() {
		if (cooldown > 0) {
			cooldown--;
		}
	}

	private void randomizeColor() {
		int red = getVehicle().getRandom().nextInt(256);
		int green = getVehicle().getRandom().nextInt(256);
		int blue = getVehicle().getRandom().nextInt(256);

		setColorVal(0, (byte) red);
		setColorVal(1, (byte) green);
		setColorVal(2, (byte) blue);
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
		if (id == 0) {
			randomizeColor();
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
		setColorVal(0,tagCompound.getByte("Red"));
		setColorVal(1,tagCompound.getByte("Green"));
		setColorVal(2,tagCompound.getByte("Blue"));
	}

}
