package vswe.stevesvehicles.tileentity.detector.modulestate.registry;


import vswe.stevesvehicles.tileentity.detector.modulestate.ModuleState;
import vswe.stevesvehicles.tileentity.detector.modulestate.ModuleStateSupplies;
import vswe.stevesvehicles.module.ISuppliesModule;
import vswe.stevesvehicles.module.cart.attachment.ModuleBridge;
import vswe.stevesvehicles.module.cart.attachment.ModuleFertilizer;
import vswe.stevesvehicles.module.cart.attachment.ModuleRailer;
import vswe.stevesvehicles.module.cart.attachment.ModuleTorch;
import vswe.stevesvehicles.module.cart.tool.ModuleFarmer;
import vswe.stevesvehicles.module.cart.tool.ModuleWoodcutter;
import vswe.stevesvehicles.module.common.attachment.ModuleCakeServer;
import vswe.stevesvehicles.module.common.attachment.ModuleShooter;

public class ModuleStateRegistrySupplies extends ModuleStateRegistry {
    public ModuleStateRegistrySupplies() {
        super("supplies");

        createAndRegisterSupplies("rail", ModuleRailer.class);
        createAndRegisterSupplies("torch", ModuleTorch.class);
        createAndRegisterSupplies("sapling", ModuleWoodcutter.class);
        createAndRegisterSupplies("seed", ModuleFarmer.class);

        createAndRegisterSupplies("bridge", ModuleBridge.class);
        createAndRegisterSupplies("arrow", ModuleShooter.class);
        createAndRegisterSupplies("fertilizer", ModuleFertilizer.class);
        createAndRegisterSupplies("cake", ModuleCakeServer.class);
    }
    
    private void createAndRegisterSupplies(String unlocalizedName, Class<? extends ISuppliesModule> moduleClass) {
        ModuleState supplies = new ModuleStateSupplies(unlocalizedName, moduleClass);
        register(supplies);
    }
}
