package vswe.stevesvehicles.module.data.registry;


import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.module.data.registry.cart.ModuleRegistryCartCleaning;
import vswe.stevesvehicles.module.data.registry.cart.ModuleRegistryCartCultivationUtil;
import vswe.stevesvehicles.module.data.registry.cart.ModuleRegistryCartDrillUtility;
import vswe.stevesvehicles.module.data.registry.cart.ModuleRegistryCartHulls;
import vswe.stevesvehicles.module.data.registry.cart.ModuleRegistryCartRails;
import vswe.stevesvehicles.module.data.registry.cart.ModuleRegistryCartTools;
import vswe.stevesvehicles.module.data.registry.cart.ModuleRegistryCartTravel;
import vswe.stevesvehicles.registry.IRegistry;
import vswe.stevesvehicles.registry.RegistryLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleRegistry implements IRegistry<ModuleData> {

    private static void preInit() {
        loader = new RegistryLoader<ModuleRegistry, ModuleData>();
        allModules = new ArrayList<ModuleData>();
    }

    public static void init() {
        add(new ModuleRegistryCartHulls());
        add(new ModuleRegistryEngines());
        add(new ModuleRegistryCartTools());
        add(new ModuleRegistryChests());
        add(new ModuleRegistryTanks());
        add(new ModuleRegistryBarrel());
        add(new ModuleRegistryShooters());
        add(new ModuleRegistryCartDrillUtility());
        add(new ModuleRegistryCartRails());
        add(new ModuleRegistryCartCultivationUtil());
        add(new ModuleRegistryTravel());
        add(new ModuleRegistryVisual());
        add(new ModuleRegistryCartTravel());
        add(new ModuleRegistryTemperature());
        add(new ModuleRegistryCartCleaning());
        add(new ModuleRegistryUtility());
        add(new ModuleRegistryCake());
        add(new ModuleRegistryIndependence());
        add(new ModuleRegistryProduction());


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
        if(loader == null) {
            preInit();
        }

        loader.add(registry);
    }

    public void register(ModuleData moduleData) {
        if (loader == null) {
            preInit();
        }

        if (modules.containsKey(moduleData.getRawUnlocalizedName())) {
            System.err.println("A module with this raw name has already been registered in this registry. Failed to register a second module with the raw name " + moduleData.getRawUnlocalizedName() + " in registry with code " + getCode());
        }else{
            modules.put(moduleData.getRawUnlocalizedName(), moduleData);
            allModules.add(moduleData);
            moduleData.setFullRawUnlocalizedName(getFullCode(moduleData));
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

    public static ModuleData getModuleFromName(String name) {
        return loader.getObjectFromName(name);
    }

    public static List<ModuleData> getAllModules() {
        return allModules;
    }

    public static int getIdFromModule(ModuleData module) {
        return loader.getIdFromObject(module);
    }
}
