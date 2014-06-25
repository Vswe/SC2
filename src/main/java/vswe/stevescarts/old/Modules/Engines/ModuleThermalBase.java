package vswe.stevescarts.old.Modules.Engines;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import vswe.stevescarts.client.interfaces.GuiVehicle;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.Helpers.Localization;

public abstract class ModuleThermalBase extends ModuleEngine {
	public ModuleThermalBase(EntityModularCart cart) {
		super(cart);
	}

	private short coolantLevel;
	
	private int getCoolantLevel() {
		return coolantLevel;
	}
	
	private void setCoolantLevel(int val) {
		 coolantLevel = (short)val;
	}
	
	@Override
	protected void initPriorityButton() {
		priorityButton = new int[] {72,17,16,16};	
	}
	
	private static final int RELOAD_LIQUID_SIZE = 1;
	
	protected abstract int getEfficiency();
	protected abstract int getCoolantEfficiency();
	
	private boolean requiresCoolant() {
		return getCoolantEfficiency() > 0;
	}

	@Override
	public int guiHeight() {
		return 40;
	}	
	
	@Override
	public boolean hasFuel(int consumption) {
		if (super.hasFuel(consumption)) {
			return !requiresCoolant() || getCoolantLevel() >= consumption;
		}else{
			return false;
		}
	}
	
	@Override
	public void consumeFuel(int consumption) {
		super.consumeFuel(consumption);
		setCoolantLevel(getCoolantLevel() - consumption);
	}
	
    /**
    Load new fuel, this is called all the time but has an if statement checking if it's necessary to re-fill fuel.
    **/
	@Override
    protected void loadFuel()
    {
		int consumption =  getCart().getConsumption(true) * 2;
	
        //if there's no fuel left it's time to re-fill
        while (getFuelLevel() <= consumption) {
			int amount = getCart().drain(FluidRegistry.LAVA, RELOAD_LIQUID_SIZE, false);
			if (amount > 0) {
				getCart().drain(FluidRegistry.LAVA, amount, true);
				setFuelLevel(getFuelLevel() + amount * getEfficiency());
			}else{
				break;
			}
        }
		
		//if there's no coolant left it's time to re-fill
        while (requiresCoolant() && getCoolantLevel() <= consumption) {
			int amount = getCart().drain(FluidRegistry.WATER, RELOAD_LIQUID_SIZE, false);
			if (amount > 0) {
				getCart().drain(FluidRegistry.WATER, amount, true);
				setCoolantLevel(getCoolantLevel() + amount * getCoolantEfficiency());
			}else{
				break;
			}
        }		
    }

	@Override
	public int getTotalFuel() {
		int totalfuel = getFuelLevel() + getCart().drain(FluidRegistry.LAVA, Integer.MAX_VALUE, false) * getEfficiency();
				
		if (requiresCoolant()) {
			int totalcoolant = getCoolantLevel() + getCart().drain(FluidRegistry.WATER, Integer.MAX_VALUE, false) * getCoolantEfficiency();
			return Math.min(totalcoolant, totalfuel);
		}else{
			return totalfuel;
		}
	}
	
	@Override
	public float[] getGuiBarColor() {
		return new float[] {1F,0F,0F};
	}	
	
	
	
	@Override
	public void smoke()
    {
        /*double oX = 0.0;
        double oZ = 0.0;

		if (getCart().motionX != 0)
		{
			oX =  (getCart().motionX > 0 ? -1 : 1);
		}

		if (getCart().motionZ != 0)
		{
			oZ =  (getCart().motionZ > 0 ? -1 : 1);
		}

        if (getCart().rand.nextInt(2) == 0) {
			getCart().worldObj.spawnParticle("largesmoke", getCart().posX + oX * 0.85, getCart().posY + 0.12D, getCart().posZ + oZ * 0.85, 0.0D, 0.0D, 0.0D);
		}*/
    }


	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, Localization.MODULES.ENGINES.THERMAL.translate(), 8, 6, 0x404040);
		String str;
		int consumption = getCart().getConsumption();
		if (consumption == 0) {
			consumption = 1;
		}

        if (getFuelLevel() >= consumption && (!requiresCoolant() || getCoolantLevel() >= consumption)) {
			str = Localization.MODULES.ENGINES.POWERED.translate();
		}else if (getFuelLevel() >= consumption){
			str = Localization.MODULES.ENGINES.NO_WATER.translate();
		}else{
			str = Localization.MODULES.ENGINES.NO_LAVA.translate();
		}
		
		drawString(gui, str, 8, 22, 0x404040);
	}

	@Override
	public boolean hasSlots() {
		return false;
	}
	
	@Override
	public int numberOfGuiData() {
		return 2;
	}

	@Override
	protected void checkGuiData(Object[] info) {
		updateGuiData(info, 0, (short)getFuelLevel());
		if (requiresCoolant()) {
			updateGuiData(info, 1, (short)getCoolantLevel());
		}
	}
	
	@Override
	public void receiveGuiData(int id, short data) {
		if (id == 0) {
			setFuelLevel(data);
		}else if(id == 1) {
			setCoolantLevel(data);
		}
	}

	
	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		super.Save(tagCompound, id);
		tagCompound.setShort(generateNBTName("Fuel",id), (short)getFuelLevel());
		if (requiresCoolant()) {
			tagCompound.setShort(generateNBTName("Coolant",id), (short)getCoolantLevel());
		}	
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		super.Load(tagCompound, id);
		setFuelLevel(tagCompound.getShort(generateNBTName("Fuel",id)));
		if (requiresCoolant()) {
			setCoolantLevel(tagCompound.getShort(generateNBTName("Coolant",id)));
		}
	}	
	

}