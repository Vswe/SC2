package vswe.stevesvehicles.detector.modulestate.registry;


import vswe.stevesvehicles.detector.modulestate.ModuleState;
import vswe.stevesvehicles.detector.modulestate.ModuleStateActivation;
import vswe.stevesvehicles.module.IActivatorModule;
import vswe.stevesvehicles.module.cart.tool.ModuleDrill;
import vswe.stevesvehicles.module.common.addon.ModuleChunkLoader;
import vswe.stevesvehicles.module.common.addon.ModuleInvisible;
import vswe.stevesvehicles.module.common.addon.ModuleShield;
import vswe.stevesvehicles.module.common.attachment.ModuleCage;

public class ModuleStateRegistryActivation extends ModuleStateRegistry {
    public ModuleStateRegistryActivation() {
        super("activation");

        createAndRegisterActivation("shield", ModuleShield.class);
        createAndRegisterActivation("chunk", ModuleChunkLoader.class);
        createAndRegisterActivation("invisibility", ModuleInvisible.class);
        createAndRegisterActivation("drill", ModuleDrill.class);
        createAndRegisterActivation("cage", ModuleCage.class);
    }

    private void createAndRegisterActivation(String unlocalizedName, Class<? extends IActivatorModule> moduleClass) {
        ModuleState activation = new ModuleStateActivation(unlocalizedName, moduleClass, 0);
        register(activation);
    }
}
