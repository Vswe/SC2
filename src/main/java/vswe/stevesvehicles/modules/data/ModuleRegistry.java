package vswe.stevesvehicles.modules.data;


import vswe.stevesvehicles.modules.data.registries.ModuleRegistryCartHulls;
import vswe.stevesvehicles.modules.data.registries.ModuleRegistryCartTools;
import vswe.stevesvehicles.modules.data.registries.ModuleRegistryChests;
import vswe.stevesvehicles.modules.data.registries.ModuleRegistryEngines;
import vswe.stevesvehicles.modules.data.registries.ModuleRegistryTanks;
import vswe.stevesvehicles.registries.IRegistry;
import vswe.stevesvehicles.registries.RegistryLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleRegistry implements IRegistry<ModuleData> {

    public static void init() {
        loader = new RegistryLoader<ModuleRegistry, ModuleData>();
        allModules = new ArrayList<ModuleData>();
        add(new ModuleRegistryCartHulls());
        add(new ModuleRegistryEngines());
        add(new ModuleRegistryCartTools());
        add(new ModuleRegistryChests());
        add(new ModuleRegistryTanks());
    }

    private static List<ModuleData> allModules;
    private static RegistryLoader<ModuleRegistry, ModuleData> loader;
    private Map<String, ModuleData> modules;

    private final String code;

    public ModuleRegistry(String code) {
        if (code.contains(":")) {
            System.err.println("The code can't contain colons. Any colons have been replaced with underscores.");
        }
        this.code = code.replace(":", "_");
        modules = new HashMap<String, ModuleData>();
    }

    @Override
    public final String getCode() {
        return code;
    }

    public static void add(ModuleRegistry registry) {
        loader.add(registry);
    }

    public void register(ModuleData moduleData) {
        if (modules.containsKey(moduleData.getRawUnlocalizedName())) {
            System.err.println("A module with this raw name has already been registered in this registry. Failed to register a second module with the raw name " + moduleData.getRawUnlocalizedName() + " in registry with code " + getCode());
        }else{
            modules.put(moduleData.getRawUnlocalizedName(), moduleData);
            allModules.add(moduleData);
        }
    }

    @Override
    public String getFullCode(ModuleData moduleData) {
        return getCode() + ":" + moduleData.getRawUnlocalizedName();
    }

    @Override
    public Collection<ModuleData> getElements() {
        return modules.values();
    }


    public static ModuleData getModuleFromId(int id) {
        return loader.getObjectFromId(id);
    }

    public static List<ModuleData> getAllModules() {
        return allModules;
    }

    public static int getIdFromModule(ModuleData module) {
        return loader.getIdFromObject(module);
    }
}
