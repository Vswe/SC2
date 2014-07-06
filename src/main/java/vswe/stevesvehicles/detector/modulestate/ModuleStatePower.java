package vswe.stevesvehicles.detector.modulestate;

import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.common.addon.ModulePowerObserver;
import vswe.stevesvehicles.vehicle.VehicleBase;


public class ModuleStatePower extends ModuleState {
    private int colorId;
    public ModuleStatePower(String unlocalizedName, int colorId) {
        super(unlocalizedName);
        this.colorId = colorId;
    }

    @Override
    public boolean isValid(VehicleBase vehicle) {
        for (ModuleBase moduleBase : vehicle.getModules()) {
            if (moduleBase instanceof ModulePowerObserver) {
                return ((ModulePowerObserver)moduleBase).isAreaActive(colorId);
            }
        }
        return false;
    }
}
