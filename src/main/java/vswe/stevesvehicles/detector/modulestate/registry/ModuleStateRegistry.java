package vswe.stevesvehicles.detector.modulestate.registry;



import vswe.stevesvehicles.detector.modulestate.ModuleState;
import vswe.stevesvehicles.registry.IRegistry;
import vswe.stevesvehicles.registry.RegistryLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleStateRegistry implements IRegistry<ModuleState> {

    private static void preInit() {
        loader = new RegistryLoader<ModuleStateRegistry, ModuleState>();
        allStates = new ArrayList<ModuleState>();
    }

    public static void init() {
        add(new ModuleStateRegistrySupplies());
        add(new ModuleStateRegistryActivation());
        add(new ModuleStateRegistryStorage());
        add(new ModuleStateRegistryPower());
        add(new ModuleStateRegistryPassenger());
    }

    private static List<ModuleState> allStates;
    private static RegistryLoader<ModuleStateRegistry, ModuleState> loader;
    private Map<String, ModuleState> states;

    private final String code;

    public ModuleStateRegistry(String code) {
        if (code.contains(":")) {
            System.err.println("The code can't contain colons. Any colons have been replaced with underscores.");
        }
        this.code = code.replace(":", "_");
        states = new HashMap<String, ModuleState>();
    }

    @Override
    public final String getCode() {
        return code;
    }

    public static void add(ModuleStateRegistry registry) {
        if(loader == null) {
            preInit();
        }

        loader.add(registry);
    }

    public void register(ModuleState state) {
        if (loader == null) {
            preInit();
        }

        if (states.containsKey(state.getRawUnlocalizedName())) {
            System.err.println("A module state with this raw name has already been registered in this registry. Failed to register a second module state with the raw name " + state.getRawUnlocalizedName() + " in registry with code " + getCode());
        }else{
            states.put(state.getRawUnlocalizedName(), state);
            allStates.add(state);
            state.setFullUnlocalizedName(getFullCode(state));
        }
    }

    @Override
    public String getFullCode(ModuleState state) {
        return getCode() + ":" + state.getRawUnlocalizedName();
    }

    @Override
    public Collection<ModuleState> getElements() {
        return states.values();
    }


    public static ModuleState getStateFromId(int id) {
        return loader.getObjectFromId(id);
    }

    public static List<ModuleState> getAllStates() {
        return allStates;
    }

    public static int getIdFromState(ModuleState state) {
        return loader.getIdFromObject(state);
    }
}
