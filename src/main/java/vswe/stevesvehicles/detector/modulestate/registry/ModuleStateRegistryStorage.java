package vswe.stevesvehicles.detector.modulestate.registry;


import vswe.stevesvehicles.detector.modulestate.ModuleState;
import vswe.stevesvehicles.detector.modulestate.ModuleStateChest;
import vswe.stevesvehicles.detector.modulestate.ModuleStateTank;

public class ModuleStateRegistryStorage extends ModuleStateRegistry {
    public ModuleStateRegistryStorage() {
        super("storage");

        ModuleState fullChest = new ModuleStateChest("chest_full", true);
        register(fullChest);

        ModuleState emptyChest = new ModuleStateChest("chest_empty", false);
        register(emptyChest);

        ModuleState fullTank = new ModuleStateTank("tank_full", ModuleStateTank.Mode.FULL);
        register(fullTank);

        ModuleState emptyTank = new ModuleStateTank("tank_empty", ModuleStateTank.Mode.EMPTY);
        register(emptyTank);

        ModuleState spareTank = new ModuleStateTank("tank_spare", ModuleStateTank.Mode.SPARE);
        register(spareTank);
    }
}
