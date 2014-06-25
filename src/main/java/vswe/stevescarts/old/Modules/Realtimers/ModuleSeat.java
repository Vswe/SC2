package vswe.stevescarts.old.Modules.Realtimers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.client.interfaces.GuiVehicle;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.Helpers.Localization;
import vswe.stevescarts.old.Helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModuleSeat extends ModuleBase {
	public ModuleSeat(EntityModularCart cart) {
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
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui,getModuleName(), 8, 6, 0x404040);
	}

	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource("/gui/chair.png");

		int imageID = getState();
		int borderID = 0;
		if (inRect(x,y, buttonRect)) {
			if (imageID == 0) {
				borderID = 2;
			}else{
				borderID = 1;
			}
		}

		drawImage(gui,buttonRect, 0, buttonRect[3] * borderID);

		int srcY = buttonRect[3] * 3 + imageID * (buttonRect[3] - 2);
		drawImage(gui, buttonRect[0] + 1, buttonRect[1] + 1, 0, srcY, buttonRect[2] - 2, buttonRect[3] - 2);
	}

	private int[] buttonRect = new int[] {20,20, 24, 12};

	@Override
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		drawStringOnMouseOver(gui, getStateName(), x,y,buttonRect);
	}

	private int getState() {
		if (getCart().riddenByEntity == null) {
			return 1;
		}else if(getCart().riddenByEntity == getClientPlayer()) {
			return 2;
		}else {
			return 0;
		}
	}

	private String getStateName() {
        return Localization.MODULES.ATTACHMENTS.SEAT_MESSAGE.translate(String.valueOf(getState()));
	}

	@Override
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (button == 0) {
			if (inRect(x,y, buttonRect)) {
				sendPacket(0);
			}
		}
	}

	@Override
	protected void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id == 0) {
			if (player != null) { 
				if (getCart().riddenByEntity == null) {
					player.mountEntity(getCart());
				}else if (getCart().riddenByEntity == player){
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

		if (getCart().riddenByEntity != null) {
			relative = false;
			chairAngle = (float)(Math.PI + Math.PI * getCart().riddenByEntity.rotationYaw / 180F);
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
	
	/*
	@Override
	public boolean onInteractFirst(EntityPlayer entityplayer) {
		if (getCart().riddenByEntity == null) {
			if (!getCart().worldObj.isRemote) {
				entityplayer.mountEntity(getCart());
			}
			return true;
		}else if (getCart().riddenByEntity == entityplayer){
			if (!getCart().worldObj.isRemote) {
				entityplayer.mountEntity(null);
			}
			return true;
		}
		return false;
	}*/
	

	
}