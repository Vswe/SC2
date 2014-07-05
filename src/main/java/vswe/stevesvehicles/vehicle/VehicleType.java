package vswe.stevesvehicles.vehicle;


import vswe.stevesvehicles.vehicle.entity.IVehicleEntity;

public class VehicleType {
    private Class<? extends IVehicleEntity> clazz;
    private String unlocalizedName;

    public VehicleType(Class<? extends IVehicleEntity> clazz, String unlocalizedName) {
        this.clazz = clazz;
        this.unlocalizedName = "steves_vehicles:item.vehicle:" + unlocalizedName + ".name";
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public Class<? extends IVehicleEntity> getClazz() {
        return clazz;
    }
}
