package vswe.stevesvehicles.old.ModuleData;
import vswe.stevesvehicles.old.Helpers.Localization;

import java.util.ArrayList;

public class ModuleDataGroup {
	private Localization.MODULE_INFO name;
	private ArrayList<ModuleData> modules;
	private int count;
	public ModuleDataGroup(Localization.MODULE_INFO name) {
		this.name = name;
		count = 1;
		modules = new ArrayList<ModuleData>();
	}	

	public String getName() {
		return name.translate(String.valueOf(getCount()));
	}
	
	public ArrayList<ModuleData> getModules() {
		return modules;
	}	
	
	public int getCount() {
		return count;
	}
	
	public ModuleDataGroup add(ModuleData module) {
		modules.add(module);
		
		return this;
	}
	
	public ModuleDataGroup setCount(int count) {
		this.count = count;
		
		return this;
	}	
	
	public ModuleDataGroup copy() {
		ModuleDataGroup newObj = new ModuleDataGroup(name).setCount(getCount());
		for (ModuleData obj : getModules()) {
			newObj.add(obj);
		}
		return newObj;
	}
	
	public ModuleDataGroup copy(int count) {
		ModuleDataGroup newObj = new ModuleDataGroup(name).setCount(count);
		for (ModuleData obj : getModules()) {
			newObj.add(obj);
		}
		return newObj;
	}
	
	public String getCountName() {
		switch (count) {
			case 1:
				return Localization.MODULE_INFO.MODULE_COUNT_1.translate();
			case 2:
				return Localization.MODULE_INFO.MODULE_COUNT_2.translate();
			case 3:
				return Localization.MODULE_INFO.MODULE_COUNT_3.translate();
			default:
				return "???";
		}
	}
	
	
	public static ModuleDataGroup getCombinedGroup(Localization.MODULE_INFO name,  ModuleDataGroup group1, ModuleDataGroup group2) {
		ModuleDataGroup newgroup = group1.copy();
		

		newgroup.add(group2);
		
		
		newgroup.name = name;
		return newgroup;
	}

	public void add(ModuleDataGroup group) {
		for (ModuleData obj : group.getModules()) {
			add(obj);
		}
	}
	
}