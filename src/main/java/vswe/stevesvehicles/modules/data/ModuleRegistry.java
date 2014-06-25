package vswe.stevesvehicles.modules.data;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class ModuleRegistry {
    private static HashMap<Integer, ModuleData> idToModuleMapping = new HashMap<Integer, ModuleData>();
    private static HashMap<String, Integer> nameToIdMapping = new HashMap<String, Integer>();
    private static Map<String, ModuleRegistry> registers = new HashMap<String, ModuleRegistry>();
    private static int nextModuleId;

    private Map<String, ModuleData> modules;

    private final String code;

    public ModuleRegistry(String code) {
        if (code.contains(":")) {
            System.err.println("The code can't contain colons. Any colons have been replaced with underscores.");
        }
        this.code = code.replace(":", "_");
        modules = new HashMap<String, ModuleData>();
    }

    public final String getCode() {
        return code;
    }


    public static void add(ModuleRegistry registry) {
        if (registers.containsKey(registry.getCode())) {
            System.err.println("A registry with this code has already been registered. Failed to register a second registry with code " + registry.getCode());
        }else{
            registers.put(registry.getCode(), registry);
        }
    }

    public void register(ModuleData moduleData) {
        if (modules.containsKey(moduleData.getRawUnlocalizedName())) {
            System.err.println("A module with this raw name has already been registered in this registry. Failed to register a second module with the raw name " + moduleData.getRawUnlocalizedName() + " in registry with code " + getCode());
        }else{
            modules.put(moduleData.getRawUnlocalizedName(), moduleData);
        }
    }

    private String getFullCode(ModuleData moduleData) {
        return getCode() + ":" + moduleData.getRawUnlocalizedName();
    }

    private static final String NBT_NEXT_ID = "NextModuleId";
    private static final String NBT_MODULES = "Modules";
    private static final String NBT_KEY = "K";
    private static final String NBT_VALUE = "V";
    private static final int NBT_COMPOUND_TYPE_ID = 10;

    //TODO call this on world load
    public static void readData(NBTTagCompound compound) {
        nextModuleId = 0;
        idToModuleMapping.clear();
        nameToIdMapping.clear();

        if (compound.hasKey(NBT_NEXT_ID)) {
            nextModuleId = compound.getShort(NBT_NEXT_ID);
            NBTTagList tags = compound.getTagList(NBT_MODULES, NBT_COMPOUND_TYPE_ID);
            for (int i = 0; i < tags.tagCount(); i++) {
                NBTTagCompound moduleCompound = tags.getCompoundTagAt(i);
                String key = moduleCompound.getString(NBT_KEY);
                int value = (int)moduleCompound.getShort(NBT_VALUE);
                System.out.println("Loaded name to id mapping. (K = " + key + ", V = " + value + ")"); //TODO Move to a log file?
                nameToIdMapping.put(key, value);
            }
        }

        for (ModuleRegistry moduleRegistry : registers.values()) {
            for (ModuleData module : moduleRegistry.modules.values()) {
                String code = moduleRegistry.getFullCode(module);
                Integer id = nameToIdMapping.get(code);
                if (id == null) {
                    id = nextModuleId++;
                    nameToIdMapping.put(code, id);
                    System.out.println("Added new name to id mapping. (K = " + code + ", V = " + id + ")"); //TODO Move to a log file?
                }

                idToModuleMapping.put(id, module);
            }
        }
    }



    //TODO call this on world save
    public static void writeData(NBTTagCompound compound) {
        compound.setShort(NBT_NEXT_ID, (short)nextModuleId);
        NBTTagList list = new NBTTagList();

        for (Map.Entry<String, Integer> entry : nameToIdMapping.entrySet()) {
            if (idToModuleMapping.containsKey(entry.getValue())) {
                NBTTagCompound moduleCompound = new NBTTagCompound();
                moduleCompound.setString(NBT_KEY, entry.getKey());
                moduleCompound.setShort(NBT_VALUE, (short)(int)entry.getValue());
                list.appendTag(moduleCompound);
            }
        }

        compound.setTag(NBT_MODULES, list);
    }
}
