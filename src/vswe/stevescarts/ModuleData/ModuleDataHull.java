package vswe.stevescarts.ModuleData;
import java.util.List;

import vswe.stevescarts.Helpers.ColorHelper;
import vswe.stevescarts.Modules.ModuleBase;


public class ModuleDataHull extends ModuleData {

	private int modularCapacity;
	private int engineMaxCount;
	private int addonMaxCount;
	private int complexityMax;

	public ModuleDataHull(int id, String name, Class<? extends ModuleBase> moduleClass) {
		super( id, name, moduleClass, 0);
	}
		
	public ModuleDataHull setCapacity(int val) {
		modularCapacity = val;
	
		return this;
	}
	
	public ModuleDataHull setEngineMax(int val) {
		engineMaxCount = val;
	
		return this;
	}	
	
	public ModuleDataHull setAddonMax(int val) {
		addonMaxCount = val;
	
		return this;
	}	

	public ModuleDataHull setComplexityMax(int val) {
		complexityMax = val;
		
		return this;
	}	
	
	public int getEngineMax() {
		return engineMaxCount;
	}
	
	public int getAddonMax() {
		return addonMaxCount;
	}	
	
	public int getCapacity() {
		return modularCapacity;
	}
	
	public int getComplexityMax() {
		return complexityMax;
	}
		
	@Override		
	public void addSpecificInformation(List list) {
		list.add(ColorHelper.YELLOW + "Modular capacity: " + modularCapacity);
		list.add(ColorHelper.PURPLE + "Module complexity cap: " + complexityMax);		
		list.add(ColorHelper.ORANGE + "Max engines count: " + engineMaxCount);
		list.add(ColorHelper.GREEN + "Max Addon count: " + addonMaxCount);
	}	
	


	
	
}