package vswe.stevesvehicles.tab;


import vswe.stevesvehicles.localization.LocalizedTextSimple;
import vswe.stevesvehicles.vehicle.VehicleType;

public class CreativeTabVehicle extends CreativeTabCustom {
    private VehicleType vehicleType;

    public CreativeTabVehicle(VehicleType vehicleType) {
        super(new LocalizedTextSimple(vehicleType.getUnlocalizedName()));
        this.vehicleType = vehicleType;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }
}
