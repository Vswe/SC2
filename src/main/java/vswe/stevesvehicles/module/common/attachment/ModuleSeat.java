package vswe.stevesvehicles.module.common.attachment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevesvehicles.client.gui.GuiVehicle;
import vswe.stevesvehicles.localization.entry.module.LocalizationTravel;
import vswe.stevesvehicles.module.cart.attachment.ModuleAttachment;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModuleSeat extends ModuleAttachment {
	public ModuleSeat(VehicleBase vehicleBase) {
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
		return 55;
	}

	@Override
	public int guiHeight() {
		return 35;
	}

	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui,getModuleName(), 8, 6, 0x404040);
	}

	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource("/gui/chair.png");

		int imageID = getState();
		int borderID = 0;
		if (inRect(x,y, BUTTON_RECT)) {
			if (imageID == 0) {
				borderID = 2;
			}else{
				borderID = 1;
			}
		}

		drawImage(gui, BUTTON_RECT, 0, BUTTON_RECT[3] * borderID);

		int srcY = BUTTON_RECT[3] * 3 + imageID * (BUTTON_RECT[3] - 2);
		drawImage(gui, BUTTON_RECT[0] + 1, BUTTON_RECT[1] + 1, 0, srcY, BUTTON_RECT[2] - 2, BUTTON_RECT[3] - 2);
	}

	private static final int[] BUTTON_RECT = new int[] {20,20, 24, 12};

	@Override
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		drawStringOnMouseOver(gui, getStateName(), x,y, BUTTON_RECT);
	}

	private int getState() {
		if (getVehicle().getEntity().riddenByEntity == null) {
			return 1;
		}else if(getVehicle().getEntity().riddenByEntity == getClientPlayer()) {
			return 2;
		}else {
			return 0;
		}
	}

	private String getStateName() {
        return LocalizationTravel.SEAT_MESSAGE.translate(String.valueOf(getState()));
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
			if (player != null) { 
				if (getVehicle().getEntity().riddenByEntity == null) {
					player.mountEntity(getVehicle().getEntity());
				}else if (getVehicle().getEntity().riddenByEntity == player){
					player.mountEntity(null);
				}
			}
		}
	}

	@Override
	public int numberOfPackets() {
		return 1;
	}

	@Override
	public void update() {
		super.update();

		if (getVehicle().getEntity().riddenByEntity != null) {
			relative = false;
			chairAngle = (float)(Math.PI + Math.PI * getVehicle().getEntity().riddenByEntity.rotationYaw / 180F);
		}else{
			relative = true;
			chairAngle = (float)Math.PI / 2;
		}
	}

	private boolean relative;
	private float chairAngle;

	public float getChairAngle() {
		return chairAngle;
	}

	public boolean useRelativeRender() {
		return relative;
	}

	@Override
	public float mountedOffset(Entity rider) {
		return -0.1F;
	}
	


	
}