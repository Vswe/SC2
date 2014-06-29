package vswe.stevesvehicles.module.cart.attachment;

import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.vehicle.VehicleBase;



//This module is not part of Steve's Carts 2 and is never used in SV yet. Would be nice if attachment modules extend this.


public class ModuleAttachment extends ModuleBase {
    /**
     * Creates a new instance of this module, the module will be created at the given vehicle.
     *
     * @param vehicle The vehicle this module is created on
     */
    public ModuleAttachment(VehicleBase vehicle) {
        super(vehicle);
    }
}
