package vswe.stevesvehicles.module.common.addon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.module.IActivatorModule;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModuleInvisible extends ModuleAddon implements IActivatorModule {
	public ModuleInvisible(VehicleBase vehicleBase) {
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
		return 90;
	}

	@Override
	public int guiHeight() {
		return 35;
	}

	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, getModuleName(), 8, 6, 0x404040);
	}

	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource("/gui/invis.png");

		int imageID = isVisible() ? 1 : 0;
		int borderID = 0;
		if (inRect(x,y, BUTTON_RECT)) {
			borderID = 1;			
		}

		drawImage(gui, BUTTON_RECT, 0, BUTTON_RECT[3] * borderID);

		int srcY = BUTTON_RECT[3] * 2 + imageID * (BUTTON_RECT[3] - 2);
		drawImage(gui, BUTTON_RECT[0] + 1, BUTTON_RECT[1] + 1, 0, srcY, BUTTON_RECT[2] - 2, BUTTON_RECT[3] - 2);
	}

	private static final int[] BUTTON_RECT = new int[] {20,20, 24, 12};

	@Override
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		drawStringOnMouseOver(gui, getStateName(), x,y, BUTTON_RECT);
	}

	
	@Override 
	public void update() {
		super.update();
		if (!isVisible() && !getVehicle().hasFuelForModule() && !getVehicle().getWorld().isRemote) {
			setIsVisible(true);
		}
	}
	
	private boolean isVisible() {
		if (isPlaceholder()) {
			return !getSimInfo().getInvisActive();
		}else{
			return getDw(0) != 0;
		}
	}

	private String getStateName() {
        return Localization.MODULES.ADDONS.INVISIBILITY.translate(isVisible() ? "0" : "1");
	}

	@Override
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (button == 0) {
			if (inRect(x,y, BUTTON_RECT)) {
				sendPacket(0);
			}
		}
	}

	@Override
	protected void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id == 0) {
			setIsVisible(!isVisible());
		}
	}

	public void setIsVisible(boolean val) {
		updateDw(0, val ? 1 : 0);
	}
	
	@Override
	public int numberOfPackets() {
		return 1;
	}

    @Override
    public boolean shouldVehicleRender() {
        return isVisible();
    }

    @Override
	public int numberOfDataWatchers() {
		return 1;
	}
	
	@Override
	public void initDw() {
		addDw(0,(byte)1);
	}
	

	
	public int getConsumption(boolean isMoving) {
		return isVisible() ? super.getConsumption(isMoving) : 3;
	}
	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		tagCompound.setBoolean("Invisible", !isVisible());
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		setIsVisible(!tagCompound.getBoolean("Invisible"));
	}	

	public void doActivate(int id) {
		setIsVisible(false);
	}
	public void doDeActivate(int id) {
		setIsVisible(true);
	}
	public boolean isActive(int id) {
		return !isVisible();
	}
}