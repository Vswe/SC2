package vswe.stevesvehicles.module.common.engine;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import vswe.stevesvehicles.client.gui.GuiVehicle;
import vswe.stevesvehicles.localization.entry.module.LocalizationEngine;
import vswe.stevesvehicles.vehicle.VehicleBase;

public abstract class ModuleThermalBase extends ModuleEngine {
	public ModuleThermalBase(VehicleBase vehicleBase) {
		super(vehicleBase);
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
        return super.hasFuel(consumption) && (!requiresCoolant() || getCoolantLevel() >= consumption);
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
    protected void loadFuel() {
		int consumption =  getVehicle().getConsumption(true) * 2;
	
        //if there's no fuel left it's time to re-fill
        while (getFuelLevel() <= consumption) {
			int amount = getVehicle().drain(FluidRegistry.LAVA, RELOAD_LIQUID_SIZE, false);
			if (amount > 0) {
				getVehicle().drain(FluidRegistry.LAVA, amount, true);
				setFuelLevel(getFuelLevel() + amount * getEfficiency());
			}else{
				break;
			}
        }
		
		//if there's no coolant left it's time to re-fill
        while (requiresCoolant() && getCoolantLevel() <= consumption) {
			int amount = getVehicle().drain(FluidRegistry.WATER, RELOAD_LIQUID_SIZE, false);
			if (amount > 0) {
				getVehicle().drain(FluidRegistry.WATER, amount, true);
				setCoolantLevel(getCoolantLevel() + amount * getCoolantEfficiency());
			}else{
				break;
			}
        }		
    }

	@Override
	public int getTotalFuel() {
		int totalFuel = getFuelLevel() + getVehicle().drain(FluidRegistry.LAVA, Integer.MAX_VALUE, false) * getEfficiency();
				
		if (requiresCoolant()) {
			int totalCoolant = getCoolantLevel() + getVehicle().drain(FluidRegistry.WATER, Integer.MAX_VALUE, false) * getCoolantEfficiency();
			return Math.min(totalCoolant, totalFuel);
		}else{
			return totalFuel;
		}
	}
	
	@Override
	public float[] getGuiBarColor() {
		return new float[] {1F,0F,0F};
	}	
	
	
	
	@Override
	public void smoke(){

    }


	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, LocalizationEngine.THERMAL_TITLE.translate(), 8, 6, 0x404040);
		String str;
		int consumption = getVehicle().getConsumption();
		if (consumption == 0) {
			consumption = 1;
		}

        if (getFuelLevel() >= consumption && (!requiresCoolant() || getCoolantLevel() >= consumption)) {
			str = LocalizationEngine.THERMAL_POWERED.translate();
		}else if (getFuelLevel() >= consumption){
			str = LocalizationEngine.THERMAL_NO_WATER.translate();
		}else{
			str = LocalizationEngine.THERMAL_NO_LAVA.translate();
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
	protected void save(NBTTagCompound tagCompound) {
		super.save(tagCompound);
		tagCompound.setShort("Fuel", (short)getFuelLevel());
		if (requiresCoolant()) {
			tagCompound.setShort("Coolant", (short)getCoolantLevel());
		}	
	}
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		super.load(tagCompound);
		setFuelLevel(tagCompound.getShort("Fuel"));
		if (requiresCoolant()) {
			setCoolantLevel(tagCompound.getShort("Coolant"));
		}
	}	
	

}