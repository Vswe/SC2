package vswe.stevesvehicles.vehicle;


import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.vehicle.entity.EntityModularBoat;

public class VehicleBoat extends VehicleBase {
    public VehicleBoat(EntityModularBoat entity) {
        super(entity);
    }

    public VehicleBoat(EntityModularBoat entity, NBTTagCompound info, String name) {
        super(entity, info, name);
    }

    private EntityModularBoat getBoat() {
        return (EntityModularBoat)getEntity();
    }

}
