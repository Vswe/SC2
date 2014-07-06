package vswe.stevesvehicles.vehicle;


import net.minecraft.util.StatCollector;
import vswe.stevesvehicles.vehicle.entity.IVehicleEntity;

public class VehicleType {
    private Class<? extends IVehicleEntity> clazz;
    private String unlocalizedName;

    public VehicleType(Class<? extends IVehicleEntity> clazz, String unlocalizedName) {
        this.clazz = clazz;
        this.unlocalizedName = unlocalizedName;
    }
    public String getUnlocalizedNameForItem() {
        return "steves_vehicles:item.vehicle:" + unlocalizedName;
    }

    public String getUnlocalizedName() {
        return getUnlocalizedNameForItem()  + ".name";
    }

    public Class<? extends IVehicleEntity> getClazz() {
        return clazz;
    }

    public String getName() {
        return StatCollector.translateToLocal(getUnlocalizedName());
    }
}
