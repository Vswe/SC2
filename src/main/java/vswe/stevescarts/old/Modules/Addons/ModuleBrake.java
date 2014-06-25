package vswe.stevescarts.old.Modules.Addons;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.Helpers.Localization;
import vswe.stevescarts.old.Helpers.ResourceHelper;
import vswe.stevescarts.old.Interfaces.GuiMinecart;
import vswe.stevescarts.old.Modules.ILeverModule;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModuleBrake extends ModuleAddon implements ILeverModule {
	public ModuleBrake(EntityModularCart cart) {
		super(cart);
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
		return 80;
	}

	@Override
	public int guiHeight() {
		return 35;
	}

	@Override
	public void drawForeground(GuiMinecart gui) {
	    drawString(gui, Localization.MODULES.ADDONS.CONTROL_LEVER.translate(), 8, 6, 0x404040);
	}


	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiMinecart gui, int x, int y) {
		ResourceHelper.bindResource("/gui/lever.png");

		drawButton(gui, x,y, startstopRect, isForceStopping() ? 2 : 1);
		drawButton(gui, x,y, turnbackRect, 0);
	}

	private void drawButton(GuiMinecart gui, int x, int y, int[] coords, int imageID) {
		if (inRect(x,y, coords)) {
			drawImage(gui,coords, 0, coords[3]);
		}else{
			drawImage(gui,coords, 0, 0);
		}

		int srcY = coords[3] * 2 + imageID * (coords[3] - 2);
		drawImage(gui, coords[0] + 1, coords[1] + 1, 0, srcY, coords[2] - 2, coords[3] - 2);
	}

	private int[] startstopRect = new int[] {15,20, 24, 12};
	private int[] turnbackRect = new int[] {startstopRect[0] + startstopRect[2] + 5,startstopRect[1], startstopRect[2], startstopRect[3]};

	@Override
	public boolean stopEngines() {
		return isForceStopping();
	}

	private boolean isForceStopping() {
		if (isPlaceholder())  {
			return getSimInfo().getBrakeActive();
		}else{
			return getDw(0) != 0;
		}
	}

	private void setForceStopping(boolean val) {
		updateDw(0, (byte)(val ? 1 : 0));
	}

	@Override
	public void drawMouseOver(GuiMinecart gui, int x, int y) {
		drawStringOnMouseOver(gui, isForceStopping() ? Localization.MODULES.ADDONS.LEVER_START.translate() : Localization.MODULES.ADDONS.LEVER_STOP.translate(), x,y,startstopRect);
		drawStringOnMouseOver(gui, Localization.MODULES.ADDONS.LEVER_TURN.translate(), x,y,turnbackRect);
	}

	@Override
	public void mouseClicked(GuiMinecart gui, int x, int y, int button) {
		if (button == 0) {
			if (inRect(x,y, startstopRect)) {
				sendPacket(0);
			}else if (inRect(x,y, turnbackRect)) {
				sendPacket(1);
			}
		}
	}

	@Override
	protected void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id == 0) {
			setForceStopping(!isForceStopping());
		}else if(id == 1) {
			turnback();
		}
	}

	@Override
	public int numberOfPackets() {
		return 2;
	}
	
	@Override
	public float getLeverState() {
		if (isForceStopping()) {
			return 0;
		}else{
			return 1;
		}
	}
	

	@Override
	public int numberOfDataWatchers() {
		return 1;
	}

	@Override
	public void initDw() {
		addDw(0,0);
	}

	
	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		tagCompound.setBoolean(generateNBTName("ForceStop",id), isForceStopping());
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		setForceStopping(tagCompound.getBoolean(generateNBTName("ForceStop",id)));
	}		
}