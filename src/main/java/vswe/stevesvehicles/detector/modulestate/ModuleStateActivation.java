package vswe.stevesvehicles.detector.modulestate;


import vswe.stevesvehicles.module.IActivatorModule;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleStateActivation extends ModuleState {
    private Class<? extends IActivatorModule> moduleClass;
    private int id;
    public ModuleStateActivation(String unlocalizedName, Class<? extends IActivatorModule> moduleClass, int id) {
        super(unlocalizedName);
        this.moduleClass = moduleClass;
        this.id = id;
    }

    @Override
    public boolean isValid(VehicleBase vehicle) {
        for (ModuleBase moduleBase : vehicle.getModules()) {
            if (moduleClass.isAssignableFrom(moduleBase.getClass())) {
                return ((IActivatorModule)moduleBase).isActive(id);
            }
        }
        return false;
    }
}
