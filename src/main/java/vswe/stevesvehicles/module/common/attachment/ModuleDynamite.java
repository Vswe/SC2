package vswe.stevesvehicles.module.common.attachment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.client.gui.GuiVehicle;
import vswe.stevesvehicles.localization.entry.module.LocalizationIndependence;
import vswe.stevesvehicles.module.cart.attachment.ModuleAttachment;
import vswe.stevesvehicles.old.Helpers.ComponentTypes;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.container.slots.SlotBase;
import vswe.stevesvehicles.container.slots.SlotExplosion;

public class ModuleDynamite extends ModuleAttachment {
	public ModuleDynamite(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, LocalizationIndependence.EXPLOSIVES_TITLE.translate(), 8, 6, 0x404040);
	}

	@Override
	protected SlotBase getSlot(int slotId, int x, int y) {
		return new SlotExplosion(getVehicle().getVehicleEntity(), slotId, 8 + x * 18, 23 + y * 18);
	}

	@Override
	public boolean hasGui() {
		return true;
	}

	@Override
	protected int getInventoryWidth() {
		return 1;
	}

	@Override
	public void activatedByRail(int x, int y, int z, boolean active) {
		if (active && getFuse() == 0) {
			prime();
		}
	}
	


	@Override
	public void update() {
        super.update();

		if (isPlaceholder()) {
			if (getFuse() == 0 && getSimInfo().getShouldExplode()) {
				setFuse(1);
			}else if(getFuse() != 0 && !getSimInfo().getShouldExplode()) {
				setFuse(0);
			}
		}
		
		
        if (getFuse() > 0) {
            setFuse(getFuse()+1);

            if (getFuse() == getFuseLength()) {
                explode();
				if (!isPlaceholder()) {
					getVehicle().getEntity().setDead();
				}
            }
        }
    }

	@Override
	public int guiWidth() {
		return super.guiWidth() + 136;
	}

	private boolean markerMoving;
	private int fuseStartX = super.guiWidth() + 5;
	private int fuseStartY = 27;
	private int[] getMovableMarker() {
		return new int[] {fuseStartX + (int)(105 *  (1F - getFuseLength() / (float) MAX_FUSE_LENGTH)), fuseStartY, 4, 10};
	}


	
	@Override
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource("/gui/explosions.png");

		drawImage(gui, fuseStartX, fuseStartY + 3, 12, 0, 105 , 4);
		drawImage(gui, fuseStartX + 105, fuseStartY - 4, 0, 10 , 16, 16);
		drawImage(gui, fuseStartX + (int)(105 *  (1F - (getFuseLength()-getFuse()) / (float) MAX_FUSE_LENGTH)), fuseStartY,  (isPrimed() ? 8 : 4), 0, 4, 10);
		drawImage(gui, getMovableMarker(), 0, 0);
	}

	@Override
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (button == 0) {
			if (getFuse() == 0 && inRect(x,y, getMovableMarker())) {
				markerMoving = true;
			}
		}
	}

	@Override
	public void mouseMovedOrUp(GuiVehicle gui,int x, int y, int button) {
		if (getFuse() != 0) {
			markerMoving = false;
		}else if(markerMoving){
            int tempFuse = MAX_FUSE_LENGTH - (int)((x - fuseStartX)/(105F/ MAX_FUSE_LENGTH));

            if (tempFuse < 2) {
                tempFuse = 2;
            }else if (tempFuse > MAX_FUSE_LENGTH) {
               tempFuse = MAX_FUSE_LENGTH;
            }

			sendPacket(0, (byte)tempFuse);
		}

        if (button != -1) {
            markerMoving = false;
        }
	}

	private boolean isPrimed() {
		return (getFuse() / 5) % 2 == 0 && getFuse() != 0;
	}

    private void explode() {
		if (isPlaceholder()) {
			setFuse(1);
		}else{
			float f = explosionSize();
			setStack(0,null);

			getVehicle().getWorld().createExplosion(null, getVehicle().getEntity().posX, getVehicle().getEntity().posY, getVehicle().getEntity().posZ, f, true/*true means real, false otherwise*/);
		}
    }

	@Override
	public void onInventoryChanged() {
		super.onInventoryChanged();

		createExplosives();
	}

    @Override
	public boolean dropOnDeath() {
		return getFuse() == 0;
	}

    @Override
    public void onDeath() {
        if (getFuse() > 0 && getFuse() < getFuseLength()) {
            explode();
        }
    }

	public float explosionSize() {
		if (isPlaceholder()) {
			return getSimInfo().getExplosionSize() / 2.5F;
		}else{
			return getDw(2) / 2.5F;
		}
	}

	public void createExplosives() {
		if (isPlaceholder() || getVehicle().getWorld().isRemote) {
			return;
		}
	
        int f = 8;
		if (ComponentTypes.DYNAMITE.isStackOfType(getStack(0))) {
			f += getStack(0).stackSize * 2;
		}
		

		updateDw(2, (byte)f);
	}

	@Override
	public int numberOfDataWatchers() {
		return 3;
	}

	@Override
	public void initDw() {
		addDw(0,0);
		addDw(1,70);
		addDw(2,8);
	}

	public int getFuse() {
		if (isPlaceholder()) {
			return getSimInfo().fuse;
		}else{
			int val = getDw(0);
			if (val < 0) {
				return val + 256;
			}else{
				return val;
			}
		}
	}

	private void setFuse(int val) {
		if (isPlaceholder()) {
			getSimInfo().fuse = val;
		}else{
			updateDw(0, (byte)val);
		}
	}

	public void setFuseLength(int val) {
		if (val > MAX_FUSE_LENGTH) {
			val = MAX_FUSE_LENGTH;
		}

		updateDw(1, (byte)val);
	}

	public int getFuseLength() {
		if (isPlaceholder()) {
			return getSimInfo().getFuseLength();
		}else{	
			int val = getDw(1);
			if (val < 0) {
				return val + 256;
			}else{
				return val;
			}
		}
	}

	public void prime() {
		setFuse(1);
	}

	private static final int MAX_FUSE_LENGTH = 150;

	@Override
	public int numberOfPackets() {
		return 1;
	}

	@Override
	protected void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id == 0) {
			setFuseLength(data[0]);
		}
	}

	
	@Override
	protected void save(NBTTagCompound tagCompound) {
		tagCompound.setShort("FuseLength", (short)getFuseLength());
		tagCompound.setShort("Fuse", (short)getFuse());
	}
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		setFuseLength(tagCompound.getShort("FuseLength"));
		setFuse(tagCompound.getShort("Fuse"));
		
		createExplosives();		
	}		
}