package vswe.stevescarts.old.ModuleData;
import java.util.List;

import vswe.stevescarts.old.Helpers.ColorHelper;
import vswe.stevescarts.old.Helpers.Localization;
import vswe.stevescarts.old.Modules.ModuleBase;


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
		list.add(ColorHelper.YELLOW + Localization.MODULE_INFO.MODULAR_CAPACITY.translate(String.valueOf(modularCapacity)));
		list.add(ColorHelper.PURPLE + Localization.MODULE_INFO.COMPLEXITY_CAP.translate(String.valueOf(complexityMax)));
		list.add(ColorHelper.ORANGE + Localization.MODULE_INFO.MAX_ENGINES.translate(String.valueOf(engineMaxCount)));
		list.add(ColorHelper.GREEN + Localization.MODULE_INFO.MAX_ADDONS.translate(String.valueOf(addonMaxCount)));
	}	
	


	
	
}