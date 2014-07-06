package vswe.stevesvehicles.detector.modulestate;


import vswe.stevesvehicles.module.ISuppliesModule;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleStateSupplies extends ModuleState {
    private Class<? extends ISuppliesModule> moduleClass;
    public ModuleStateSupplies(String unlocalizedName, Class<? extends ISuppliesModule> moduleClass) {
        super(unlocalizedName);
        this.moduleClass = moduleClass;
    }

    @Override
    public boolean isValid(VehicleBase vehicle) {
        for (ModuleBase moduleBase : vehicle.getModules()) {
            if (moduleClass.isAssignableFrom(moduleBase.getClass())) {
                return ((ISuppliesModule)moduleBase).haveSupplies();
            }
        }
        return false;
    }
}
