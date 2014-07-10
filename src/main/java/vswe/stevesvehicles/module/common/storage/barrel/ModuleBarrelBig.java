package vswe.stevesvehicles.module.common.storage.barrel;

import vswe.stevesvehicles.vehicle.VehicleBase;


public class ModuleBarrelBig extends ModuleBarrel {
    public ModuleBarrelBig(VehicleBase vehicleBase) {
        super(vehicleBase);
    }

    @Override
    protected int getStackCount() {
        return 1024;
    }
}
