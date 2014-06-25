package vswe.stevesvehicles.old.Modules.Engines;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.vehicles.entities.EntityModularCart;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ModuleSolarBase extends ModuleEngine {
	private int light;
	private boolean maxLight;
	private int panelCoolDown;
	private boolean down = true;
	private boolean upState;

	public ModuleSolarBase(EntityModularCart cart) {
		super(cart);
	}

	@Override
	public boolean hasSlots() {
		return false;
	}

	//called to update the module's actions. Called by the cart's update code.
	@Override
	public void update() {
		super.update();
		updateSolarModel();
	}

	@Override
    protected void loadFuel(){
		updateLight();
		updateDataForModel();
		chargeSolar();
	}

	@Override
	public int getTotalFuel() {
		return getFuelLevel();
	}	
	@Override
	public float[] getGuiBarColor() {
		return new float[] {1F,1F,0F};
	}
	
	
	private void updateLight() {
		light = getCart().worldObj.getBlockLightValue(getCart().x(), getCart().y(), getCart().z());

		if (light == 15 && !getCart().worldObj.canBlockSeeTheSky(getCart().x(), getCart().y()+1, getCart().z()))
		{
			light = 14;
		}
	}

	private void updateDataForModel() {
		if (isPlaceholder()) {
			light = getSimInfo().getMaxLight() ? 15 : 14;
		}else{				
			if (getCart().worldObj.isRemote) {
				light = getDw(1);
			}else{
				updateDw(1,(byte)light);
			}
		}
		
		maxLight = light == 15;
		if (!upState && light == 15) {
			light = 14;
		}
	}

	private void chargeSolar() {

        if (light == 15 && getCart().worldObj.rand.nextInt(8) < 4)
        {
            setFuelLevel(getFuelLevel() + getGenSpeed());

            if (getFuelLevel() > getMaxCapacity())
            {
                setFuelLevel(getMaxCapacity());
            }
        }
	}


	public int getLight() {
		return light;
	}
	
	

	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, Localization.MODULES.ENGINES.SOLAR.translate(), 8, 6, 0x404040);
        String strfuel = Localization.MODULES.ENGINES.NO_POWER.translate();

        if (getFuelLevel() > 0)
        {
            strfuel = Localization.MODULES.ENGINES.POWER.translate(String.valueOf(getFuelLevel()));
        }

        drawString(gui,strfuel, 8, 42, 0x404040);
	}

	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		super.drawBackground(gui,x,y);

		ResourceHelper.bindResource("/gui/solar.png");

	    int lightWidth = light * 3;

        if (light == 15)
        {
            lightWidth += 2;
        }

		drawImage(gui, 9, 20, 0, 0, 54 , 18);
        drawImage(gui, 9+6, 20+1, 0, 18, lightWidth , 16);
	}

	@Override
	public int numberOfDataWatchers() {
		return super.numberOfDataWatchers() + 2;
	}

	
	
	@Override
	public void initDw() {
		super.initDw();
		addDw(1,(byte)0);
		addDw(2,(byte)0);
		

	}

	protected boolean isGoingDown() {
		return down;
	}
	
	public void updateSolarModel()
    {
		if (getCart().worldObj.isRemote) {
			updateDataForModel();
		}

		panelCoolDown += maxLight ? 1 : -1;
		if (down && panelCoolDown < 0) {
			panelCoolDown = 0;
		}else if(!down && panelCoolDown > 0) {
			panelCoolDown = 0;
		}else if(Math.abs(panelCoolDown) > 20) {
			panelCoolDown = 0;
			down = !down;
		}		
		
		upState = updatePanels();
		
		if (!getCart().worldObj.isRemote) {
			updateDw(2, upState ? 1 : 0);
		}
	}

	@Override
	public int numberOfGuiData() {
		return 2;
	}

	@Override
	protected void checkGuiData(Object[] info) {
		//System.out.println("Server " + getFuelLevel());
		updateGuiData(info, 0, (short)(getFuelLevel() & 65535));
		updateGuiData(info, 1,(short)((getFuelLevel() >> 16) & 65535));
	}
	@Override
	public void receiveGuiData(int id, short data) {

		if (id == 0) {
			int dataint = data;
			if (dataint < 0) {
				dataint += 65536;
			}
			setFuelLevel((getFuelLevel() & -65536) | dataint);
		}else if (id == 1) {
			setFuelLevel((getFuelLevel() & 65535) | (data << 16));
		}

	}
	

	protected abstract int getMaxCapacity();
	protected abstract int getGenSpeed();
	protected abstract boolean updatePanels();
	

	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		super.Save(tagCompound, id);
		tagCompound.setInteger(generateNBTName("Fuel",id), getFuelLevel());
		tagCompound.setBoolean(generateNBTName("Up",id), upState);
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		super.Load(tagCompound, id);
		setFuelLevel(tagCompound.getInteger(generateNBTName("Fuel",id)));
		upState = tagCompound.getBoolean(generateNBTName("Up",id));
	}
	
}