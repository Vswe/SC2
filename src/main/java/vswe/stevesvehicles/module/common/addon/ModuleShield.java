package vswe.stevesvehicles.module.common.addon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import vswe.stevesvehicles.client.gui.GuiVehicle;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.module.IActivatorModule;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModuleShield extends ModuleAddon implements IActivatorModule {
	public ModuleShield(VehicleBase vehicleBase) {
		super(vehicleBase);
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


    private static final int MAX_SHIELD_DISTANCE = 18;
    private static final int MIN_SHIELD_DISTANCE = 0;
    private static final float SHIED_DISTANCE_SPEED = 0.25F;

	private boolean shield = true;
	private float shieldDistance = MAX_SHIELD_DISTANCE;
	private float shieldAngle;


    public void update() {
        super.update();

		if (hasShield() &&  !getVehicle().hasFuelForModule() && !getVehicle().getWorld().isRemote) {
			setShieldStatus(false);
		}
		
		if (shield) {
			getVehicle().getEntity().extinguish();
		}

		if (!getShieldStatus() && shieldDistance > MIN_SHIELD_DISTANCE) {
			shieldDistance -= SHIED_DISTANCE_SPEED;
			if (shieldDistance <= MIN_SHIELD_DISTANCE) {
				shield = false;
			}
		}else if (getShieldStatus()  && shieldDistance < MAX_SHIELD_DISTANCE) {
			shieldDistance += SHIED_DISTANCE_SPEED;
			shield = true;
		}

		if (shield) {
			shieldAngle = (float)((shieldAngle + 0.125F) % (Math.PI * 100));
		}
    }

    @Override
    public boolean receiveDamage(DamageSource source, float val) {
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
		if (inRect(x,y, BUTTON_RECT)) {
			borderID = 1;			
		}

		drawImage(gui, BUTTON_RECT, 0, BUTTON_RECT[3] * borderID);

		int srcY = BUTTON_RECT[3] * 2 + imageID * (BUTTON_RECT[3] - 2);
		drawImage(gui, BUTTON_RECT[0] + 1, BUTTON_RECT[1] + 1, 0, srcY, BUTTON_RECT[2] - 2, BUTTON_RECT[3] - 2);
	}

	private static final int[] BUTTON_RECT = new int[] {20, 20, 24, 12};

	@Override
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		drawStringOnMouseOver(gui, getStateName(), x,y, BUTTON_RECT);
	}


	private String getStateName() {
        return Localization.MODULES.ADDONS.SHIELD.translate(getShieldStatus() ? "1" : "0");
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

    @Override
	public int getConsumption(boolean isMoving) {
		return hasShield() ? 20 : super.getConsumption(isMoving);
	}	
	
	@Override
	protected void save(NBTTagCompound tagCompound) {
		tagCompound.setBoolean("Shield", getShieldStatus());
	}
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		setShieldStatus(tagCompound.getBoolean("Shield"));
	}		

    @Override
	public void doActivate(int id) {
		setShieldStatus(true);
	}
    @Override
	public void doDeActivate(int id) {
		setShieldStatus(false);
	}
    @Override
	public boolean isActive(int id) {
		return getShieldStatus();
	}		
	
}