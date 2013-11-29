package vswe.stevescarts.ModuleData;
import java.util.ArrayList;

public class ModuleDataGroup {
	private String name;
	private ArrayList<ModuleData> modules;
	private int count;
	public ModuleDataGroup(String name) {
		this.name = name;
		count = 1;
		modules = new ArrayList<ModuleData>();
	}	

	public String getName() {
		return name;
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
		ModuleDataGroup newObj = new ModuleDataGroup(getName()).setCount(getCount());
		for (ModuleData obj : getModules()) {
			newObj.add(obj);
		}
		return newObj;
	}
	
	public ModuleDataGroup copy(int count) {
		ModuleDataGroup newObj = new ModuleDataGroup(getName()).setCount(count);
		for (ModuleData obj : getModules()) {
			newObj.add(obj);
		}
		return newObj;
	}
	
	public String getCountName() {
		switch (count) {
			case 1:
				return "one";
			case 2:
				return "two";
			case 3:
				return "three";
			case 4:
				return "four";
			case 5:
				return "five";
			case 6:
				return "six";
			case 7:
				return "seven";
			case 8:
				return "eight";
			case 9:
				return "nine";
			default:
				return "unknown";
		}
	}
	
	
	public static ModuleDataGroup getCombinedGroup(String name,  ModuleDataGroup group1, ModuleDataGroup group2) {
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