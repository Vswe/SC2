package vswe.stevesvehicles.tileentity.detector.modulestate;


import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.common.storage.chest.ModuleChest;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleStateChest extends ModuleState {
    private boolean full;
    public ModuleStateChest(String unlocalizedName, boolean full) {
        super(unlocalizedName);
        this.full = full;
    }

    @Override
    public boolean isValid(VehicleBase vehicle) {
        boolean hasModule = false;
        for (ModuleBase moduleBase : vehicle.getModules()) {
            if (moduleBase instanceof ModuleChest) {
                ModuleChest chest = (ModuleChest)moduleBase;

                if (full && !chest.isCompletelyFilled()) {
                    return false;
                }else if (!full && !chest.isCompletelyEmpty()) {
                    return false;
                }

                hasModule = true;
            }
        }
        return hasModule;
    }
}
