package vswe.stevescarts.Modules.Addons;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;

import java.util.Random;

public class ModuleColorRandomizer extends ModuleAddon {
	private int[] button = new int[] {10, 20, 16, 16};
	private int cooldown;
	private Random random;

	public ModuleColorRandomizer(MinecartModular cart) {
		super(cart);
		random = new Random();
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
	public void drawForeground(GuiMinecart gui) {
		drawString(gui, getModuleName(), 8, 6, 0x404040);
	}

	@Override
	public int guiWidth() {
		return 28;
	}

	@Override
	public int guiHeight() {
		return 38;
	}

	@Override
	public void drawBackground(GuiMinecart gui, int x, int y) {
		ResourceHelper.bindResource("/gui/color_randomizer.png");

		GL11.glColor4f(1, 1, 1, 1);
		drawImage(gui, button, 0, 0);
	}

	@Override
	public void drawMouseOver(GuiMinecart gui, int x, int y) {
		String randomizeString = Localization.MODULES.ADDONS.BUTTON_RANDOMIZE.translate();
		drawStringOnMouseOver(gui, randomizeString, x, y, button);
	}

	@Override
	public void mouseClicked(GuiMinecart gui, int x, int y, int button) {
		if (button == 0) {
			if (inRect(x,y, this.button)) {
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
		int red = random.nextInt(256);
		int green = random.nextInt(256);
		int blue = random.nextInt(256);

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
