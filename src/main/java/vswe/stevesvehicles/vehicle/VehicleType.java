package vswe.stevesvehicles.vehicle;


import vswe.stevesvehicles.vehicle.entity.IVehicleEntity;

public class VehicleType {
    private Class<? extends IVehicleEntity> clazz;
    private String name;

    public VehicleType(Class<? extends IVehicleEntity> clazz, String name) {
        this.clazz = clazz;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Class<? extends IVehicleEntity> getClazz() {
        return clazz;
    }
}
