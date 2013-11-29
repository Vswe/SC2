package vswe.stevescarts.Modules.Storages.Chests;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.Storages.ModuleStorage;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotChest;

public abstract class ModuleChest extends ModuleStorage {
	public ModuleChest(MinecartModular cart) {
		super(cart);
	}

	protected abstract String getChestName();

	//called to update the module's actions. Called by the cart's update code.
	@Override
	public void update() {
		super.update();
		handleChest();
	}

	@Override
	public boolean hasGui(){
		return true;
	}

	@Override
	protected SlotBase getSlot(int slotId, int x, int y) {
		return new SlotChest(getCart(),slotId,8+x*18,16+y*18);
	}

	@Override
	public void drawForeground(GuiMinecart gui) {
	    drawString(gui,getChestName(), 8, 6, 0x404040);
	}

	@Override
	public int guiWidth() {
		return 15 + getInventoryWidth() * 18;
	}

	@Override
	public int guiHeight() {
		return 20 + getInventoryHeight() * 18 ;
	}

	private float chestAngle;

	public float getChestAngle() {
		return chestAngle;
	}
	
	protected boolean lidClosed() {
		return chestAngle <= 0.0F;
	}

	protected float getLidSpeed() {
		return (float)(Math.PI / 20);
	}	
		
	protected float chestFullyOpenAngle() {
		return (float)Math.PI * 7 / 16F;
	}

	protected boolean hasVisualChest() {
		return true;
	}
	
	protected boolean playChestSound() {
		return hasVisualChest();
	}	

	@Override
	public int numberOfDataWatchers() {
		if (hasVisualChest()) {
			return 1;
		}else{
			return 0;
		}
	}

	@Override
	public void initDw() {
		if (hasVisualChest()) {
			addDw(0,0);
		}
	}

	public void openChest() {
		if (hasVisualChest()) {
			updateDw(0,getDw(0)+1);
		}
	}

	public void closeChest() {
		if (hasVisualChest()) {
			updateDw(0,getDw(0)-1);
		}
	}

	protected boolean isChestActive() {
		if (hasVisualChest()) {
			if (isPlaceholder()) {
				return getSimInfo().getChestActive();
			}else{
				return getDw(0) > 0;
			}
		}else{
			return false;
		}
	}

	protected void handleChest() {
		if (!hasVisualChest()) {
			return;
		}

		if (isChestActive() && lidClosed() && playChestSound())
		{
			getCart().worldObj.playSoundEffect(getCart().posX, getCart().posY, getCart().posZ, "random.chestopen", 0.5F, getCart().worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}

		if (isChestActive() && chestAngle < chestFullyOpenAngle()) {
			chestAngle += getLidSpeed();
			if (chestAngle >chestFullyOpenAngle())
			{
				chestAngle = chestFullyOpenAngle();
			}
		}else if (!isChestActive() && !lidClosed())
		{
			float lastAngle = chestAngle;

			chestAngle -= getLidSpeed();

			if (chestAngle < Math.PI * 3 / 8 && lastAngle >= Math.PI * 3 / 8 && playChestSound())
			{
				getCart().worldObj.playSoundEffect(getCart().posX, getCart().posY, getCart().posZ, "random.chestclosed", 0.5F, getCart().worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}

			if (chestAngle < 0.0F)
			{
				chestAngle = 0.0F;
			}
		}
	}
	
	public boolean isCompletelyFilled() {
		for (int i = 0; i < getInventorySize(); i++) {
			if (getStack(i) == null) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isCompletelyEmpty() {
		for (int i = 0; i < getInventorySize(); i++) {
			if (getStack(i) != null) {
				return false;
			}
		}
		return true;
	}	
}