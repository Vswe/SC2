package vswe.stevesvehicles.modules.data;
import vswe.stevesvehicles.old.Helpers.Localization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleDataGroup {
	private Localization.MODULE_INFO name;
	private List<ModuleData> modules;
	private int count;
    private List<ModuleDataGroup> clones;
	private ModuleDataGroup(Localization.MODULE_INFO name) {
		this.name = name;
		count = 1;
		modules = new ArrayList<ModuleData>();
	}


	public String getName() {
		return name == null ? null : name.translate(String.valueOf(getCount()));
	}
	
	public List<ModuleData> getModules() {
		return modules;
	}	
	
	public int getCount() {
		return count;
	}
	
	public ModuleDataGroup add(ModuleData module) {
		modules.add(module);
        if (clones != null) {
            for (ModuleDataGroup clone : clones) {
                clone.add(module);
            }
        }
		
		return this;
	}
	
	public ModuleDataGroup setCount(int count) {
		this.count = count;
		
		return this;
	}	
	
	public ModuleDataGroup copy(String key) {
        return copy(key, getCount());
	}
	
	public ModuleDataGroup copy(String key, int count) {
		ModuleDataGroup newObj = getUnlinkedCopy(key, count);
        addClone(newObj);
		return newObj;
	}

    public ModuleDataGroup getUnlinkedCopy(String key, int count) {
        ModuleDataGroup newObj = createGroup(key, name).setCount(count);
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

	public static ModuleDataGroup getCombinedGroup(String key, Localization.MODULE_INFO name,  ModuleDataGroup mainGroup, ModuleDataGroup ... extraGroups) {
		ModuleDataGroup newGroup = mainGroup.copy(key);
        mainGroup.addClone(newGroup);

        for (ModuleDataGroup extraGroup : extraGroups) {
            newGroup.add(extraGroup);
            extraGroup.addClone(newGroup);
        }


		newGroup.name = name;
		return newGroup;
	}

    private void addClone(ModuleDataGroup group) {
        if (clones == null) {
            clones = new ArrayList<ModuleDataGroup>();
        }

        clones.add(group);
    }

	public void add(ModuleDataGroup group) {
        group.addClone(this);

		for (ModuleData obj : group.getModules()) {
			add(obj);
		}
	}

    private static Map<String, ModuleDataGroup> groups = new HashMap<String, ModuleDataGroup>();
    public static ModuleDataGroup createGroup(String key, Localization.MODULE_INFO name) {
        if (groups.containsKey(key)) {
            ModuleDataGroup group = groups.get(key);
            if (group.name == null && name != null) {
                group.name = name;
            }
            return group;
        }else{
            ModuleDataGroup group = new ModuleDataGroup(name);
            groups.put(key, group);
            return group;
        }
    }

    public static ModuleDataGroup getGroup(String key) {
        return createGroup(key, null);
    }

}