package vswe.stevescarts.old.Modules.Engines;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.Helpers.Localization;
import vswe.stevescarts.old.Helpers.ResourceHelper;
import vswe.stevescarts.old.Interfaces.GuiMinecart;
import vswe.stevescarts.old.Modules.ModuleBase;

public abstract class ModuleEngine extends ModuleBase {
	private int fuel;
	protected int[] priorityButton;

	public ModuleEngine(EntityModularCart cart) {
		super(cart);
		initPriorityButton();
	}

	protected void initPriorityButton() {
		priorityButton = new int[] {78,7,16,16};	
	}
	
	//called to update the module's actions. Called by the cart's update code.
	@Override
	public void update() {
		super.update();
		loadFuel();
	}

	//returns if this cart supplies the cart with fuel
	public boolean hasFuel(int comsumption) {
		return getFuelLevel() >= comsumption && !isDisabled();
	}

	public int getFuelLevel() {
		return fuel;
	}

	public void setFuelLevel(int val) {
		fuel = val;
	}

	protected boolean isDisabled() {
		return getPriority()  >= 3 || getPriority() < 0;
	}

	public int getPriority() {
		if (isPlaceholder()) {
			return 0;
		}
	
		int temp = getDw(0);
		if (temp < 0 || temp > 3) {
			temp = 3;
		}
		return temp;
	}

	private void setPriority(int data) {	
		if (data < 0) {
			data = 0;
		}else if (data > 3) {
			data = 3;
		}

		updateDw(0, data);
	}

	public void consumeFuel(int comsumption) {
		setFuelLevel(getFuelLevel() - comsumption);
	}

    protected abstract void loadFuel();

	public void smoke(){}

	
	public abstract int getTotalFuel();
	public abstract float[] getGuiBarColor();
	
	
	@Override
	public boolean hasGui(){
		return true;
	}

	@Override
	public int guiWidth() {
		return 100;
	}

	@Override
	public int guiHeight() {
		return 50;
	}

		
	
	@Override
	public void drawBackground(GuiMinecart gui, int x, int y) {
		ResourceHelper.bindResource("/gui/engine.png");

		int sourceX = 16 * getPriority();
		int sourceY = 0;
		if (inRect(x,y, priorityButton)) {
			sourceY = 16;
		}
		drawImage(gui, priorityButton, sourceX, sourceY);
	}

	@Override
	public void drawMouseOver(GuiMinecart gui, int x, int y) {
		drawStringOnMouseOver(gui, getPriorityText() , x, y , priorityButton);
	}

	private String getPriorityText() {
        if (isDisabled()) {
            return Localization.MODULES.ENGINES.ENGINE_DISABLED.translate();
        }else{
            return Localization.MODULES.ENGINES.ENGINE_PRIORITY.translate(String.valueOf(getPriority()));
        }
	}

	@Override
	public void mouseClicked(GuiMinecart gui, int x, int y, int button) {
		if (inRect(x,y, priorityButton)) {
			if (button == 0 || button == 1) {
				sendPacket(0,(byte)button);
			}
		}
	}
	@Override
	protected void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id == 0) {
			int prio = getPriority();
			prio += data[0] == 0 ? 1 : -1;
			prio %= 4;
			if (prio < 0) {
				prio += 4;
			}
			setPriority(prio);
		}
	}
	@Override
	public int numberOfPackets() {
		return 1;
	}
	
	@Override
	public void initDw() {
		addDw(0,0);
	}	
	@Override
	public int numberOfDataWatchers() {
		return 1;
	}

	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		tagCompound.setByte(generateNBTName("Priority",id),(byte)getPriority());
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		setPriority(tagCompound.getByte(generateNBTName("Priority",id)));
	}

}