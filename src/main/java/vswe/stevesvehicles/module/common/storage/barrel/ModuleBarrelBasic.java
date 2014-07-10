package vswe.stevesvehicles.module.common.storage.barrel;

import vswe.stevesvehicles.vehicle.VehicleBase;


public class ModuleBarrelBasic extends ModuleBarrel {
    public ModuleBarrelBasic(VehicleBase vehicleBase) {
        super(vehicleBase);
    }

    @Override
    protected int getStackCount() {
        return 64;
    }
}
