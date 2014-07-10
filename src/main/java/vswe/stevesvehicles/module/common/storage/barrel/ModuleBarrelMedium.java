package vswe.stevesvehicles.module.common.storage.barrel;

import vswe.stevesvehicles.vehicle.VehicleBase;


public class ModuleBarrelMedium extends ModuleBarrel {
    public ModuleBarrelMedium(VehicleBase vehicleBase) {
        super(vehicleBase);
    }

    @Override
    protected int getStackCount() {
        return 256;
    }
}
