package vswe.stevescarts.Modules.Addons;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.IActivatorModule;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModuleInvisible extends ModuleAddon implements IActivatorModule {
	public ModuleInvisible(MinecartModular cart) {
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
		return 55;
	}

	@Override
	public int guiHeight() {
		return 35;
	}

	@Override
	public void drawForeground(GuiMinecart gui) {
	    drawString(gui, getModuleName(), 8, 6, 0x404040);
	}

	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiMinecart gui, int x, int y) {
		ResourceHelper.bindResource("/gui/invis.png");

		int imageID = isVisible() ? 1 : 0;
		int borderID = 0;
		if (inRect(x,y, buttonRect)) {
			borderID = 1;			
		}

		drawImage(gui,buttonRect, 0, buttonRect[3] * borderID);

		int srcY = buttonRect[3] * 2 + imageID * (buttonRect[3] - 2);
		drawImage(gui, buttonRect[0] + 1, buttonRect[1] + 1, 0, srcY, buttonRect[2] - 2, buttonRect[3] - 2);
	}

	private int[] buttonRect = new int[] {20,20, 24, 12};

	@Override
	public void drawMouseOver(GuiMinecart gui, int x, int y) {
		drawStringOnMouseOver(gui, getStateName(), x,y,buttonRect);
	}

	
	@Override 
	public void update() {
		super.update();
		if (!isVisible() && !getCart().hasFuelForModule() && !getCart().worldObj.isRemote) {
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
	public void mouseClicked(GuiMinecart gui, int x, int y, int button) {
		if (button == 0) {
			if (inRect(x,y, buttonRect)) {
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
	public boolean shouldCartRender() {
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
		tagCompound.setBoolean(generateNBTName("Invis",id), !isVisible());
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		setIsVisible(!tagCompound.getBoolean(generateNBTName("Invis",id)));
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