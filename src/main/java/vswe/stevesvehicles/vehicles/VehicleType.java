package vswe.stevesvehicles.vehicles;


import vswe.stevesvehicles.vehicles.entities.IVehicleEntity;

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
}
