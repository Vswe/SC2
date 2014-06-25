package vswe.stevescarts.old.Modules.Realtimers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.client.interfaces.GuiVehicle;
import vswe.stevescarts.old.Helpers.ComponentTypes;
import vswe.stevescarts.old.Helpers.Localization;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.Helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.old.Slots.SlotBase;
import vswe.stevescarts.old.Slots.SlotExplosion;

public class ModuleDynamite extends ModuleBase {
	public ModuleDynamite(EntityModularCart cart) {
		super(cart);
	}

	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, Localization.MODULES.ATTACHMENTS.EXPLOSIVES.translate(), 8, 6, 0x404040);
	}

	@Override
	protected SlotBase getSlot(int slotId, int x, int y) {
		return new SlotExplosion(getCart(),slotId,8+x*18,23+y*18);
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
	public void update()
    {
        super.update();

		if (isPlaceholder()) {
			if (getFuse() == 0 && getSimInfo().getShouldExplode()) {
				setFuse(1);
			}else if(getFuse() != 0 && !getSimInfo().getShouldExplode()) {
				setFuse(0);
			}
		}
		
		
        if (getFuse() > 0)
        {
            setFuse(getFuse()+1);

            if (getFuse() == getFuseLength())
            {
                explode();
				if (!isPlaceholder()) {
					getCart().setDead();				
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
		return new int[] {fuseStartX + (int)(105 *  (1F - getFuseLength() / (float)maxFuseLength)), fuseStartY, 4, 10};
	}


	
	@Override
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource("/gui/explosions.png");

		drawImage(gui, fuseStartX, fuseStartY + 3, 12, 0, 105 , 4);
		drawImage(gui, fuseStartX + 105, fuseStartY - 4, 0, 10 , 16, 16);
		drawImage(gui, fuseStartX + (int)(105 *  (1F - (getFuseLength()-getFuse()) / (float)maxFuseLength)), fuseStartY,  (isPrimed() ? 8 : 4), 0, 4, 10);
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
            int tempfuse = maxFuseLength - (int)((x - fuseStartX)/(105F/maxFuseLength));

            if (tempfuse < 2)
            {
                tempfuse = 2;
            }
            else if (tempfuse > maxFuseLength)
            {
               tempfuse = maxFuseLength;
            }

			sendPacket(0, (byte)tempfuse);
		}

        if (button != -1)
        {
            markerMoving = false;
        }
	}

	private boolean isPrimed() {
		return (getFuse() / 5) % 2 == 0 && getFuse() != 0;
	}

    private void explode()
    {
		if (isPlaceholder()) {
			setFuse(1);
		}else{
			float f = explosionSize();
			setStack(0,null);

			getCart().worldObj.createExplosion(null, getCart().posX, getCart().posY, getCart().posZ, f, true/*true means huge, false otherwise*/);
		}
    }

	@Override
	public void onInventoryChanged() {
		super.onInventoryChanged();

		createExplosives();
	}

	public boolean dropOnDeath() {
		return getFuse() == 0;
	}

    public void onDeath()
    {
        if (getFuse() > 0 && getFuse() < getFuseLength())
        {
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
		if (isPlaceholder() || getCart().worldObj.isRemote) {
			return;
		}
	
        int f = 8;

		/*if (getStack(0) != null && getStack(0).getItem().itemID == Item.gunpowder.itemID)
		{
			f += getStack(0).stackSize * 1;
		}
		else if (getStack(0) != null && getStack(0).getItem().itemID == Block.tnt.blockID)
		{
			f += getStack(0).stackSize * 5;
		}*/
		
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
		if (val > maxFuseLength) {
			val = maxFuseLength;
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

	private final int maxFuseLength = 150;

	protected int getMaxFuse() {
		return maxFuseLength;
	}

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
	protected void Save(NBTTagCompound tagCompound, int id) {
		tagCompound.setShort(generateNBTName("FuseLength",id), (short)getFuseLength());
		tagCompound.setShort(generateNBTName("Fuse",id), (short)getFuse());		
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		setFuseLength(tagCompound.getShort(generateNBTName("FuseLength",id)));
		setFuse(tagCompound.getShort(generateNBTName("Fuse",id)));		
		
		createExplosives();		
	}		
}