package vswe.stevesvehicles.old.Modules.Addons;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.old.Modules.IActivatorModule;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModuleShield extends ModuleAddon implements IActivatorModule {
	public ModuleShield(EntityModularCart cart) {
		super(cart);
	}

	protected boolean shieldSetting() {
		return getShieldStatus();
	}

	public float getShieldDistance() {
		return shieldDistance;
	}

	public float getShieldAngle() {
		return shieldAngle;
	}

	public boolean hasShield() {
		return shield;
	}


	private boolean shield = true;
	private float shieldDistance = 18;
	private float shieldAngle;


    public void update()
    {
        super.update();

		if (hasShield() &&  !getCart().hasFuelForModule() && !getCart().worldObj.isRemote) {
			setShieldStatus(false);
		}
		
		if (shield) {
			getCart().extinguish();
		}

		if (!getShieldStatus() && shieldDistance > 0) {
			shieldDistance -= 0.25F;
			if (shieldDistance <= 0) {
				shield = false;
			}
		}else if (getShieldStatus()  && shieldDistance < 18) {
			shieldDistance += 0.25F;
			shield = true;
		}

		if (shield) {
			shieldAngle = (float)((shieldAngle + 0.125F) % (Math.PI * 100));
		}
    }

	public boolean receiveDamage(DamageSource source, int val) {
		return !hasShield();
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
		return 75;
	}

	@Override
	public int guiHeight() {
		return 35;
	}

	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui,getModuleName(), 8, 6, 0x404040);
	}

	public void setShieldStatus(boolean val) {
		if (!isPlaceholder()) {
			updateDw(0, (byte)(val ? 1 : 0));
		}
	}
	
	
	private boolean getShieldStatus() {
		if (isPlaceholder()) {
			return getSimInfo().getShieldActive();
		}else{		
			return getDw(0) != 0;
		}
	}
	

	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource("/gui/shield.png");

		int imageID = getShieldStatus() ? 1 : 0;
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
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		drawStringOnMouseOver(gui, getStateName(), x,y,buttonRect);
	}


	private String getStateName() {
        return Localization.MODULES.ADDONS.SHIELD.translate(getShieldStatus() ? "1" : "0");
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
			updateDw(0, getShieldStatus() ? 0 : 1);
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
		addDw(0,(byte)0);
	}	
	
	public int getConsumption(boolean isMoving) {
		return hasShield() ? 20 : super.getConsumption(isMoving);
	}	
	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		tagCompound.setBoolean(generateNBTName("Shield",id), getShieldStatus());
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		setShieldStatus(tagCompound.getBoolean(generateNBTName("Shield",id)));		
	}		
	
	public void doActivate(int id) {
		setShieldStatus(true);
	}
	public void doDeActivate(int id) {
		setShieldStatus(false);
	}
	public boolean isActive(int id) {
		return getShieldStatus();
	}		
	
}