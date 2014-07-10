package vswe.stevesvehicles.module.data.registry;



import vswe.stevesvehicles.module.common.storage.barrel.ModuleBarrelBasic;
import vswe.stevesvehicles.module.common.storage.barrel.ModuleBarrelBig;
import vswe.stevesvehicles.module.common.storage.barrel.ModuleBarrelMedium;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.vehicle.VehicleRegistry;

//TODO add recipes and module costs

public class ModuleRegistryBarrel extends ModuleRegistry {
    public ModuleRegistryBarrel() {
        super("common.barrel");

        ModuleData basic = new ModuleData("basic_barrel", ModuleBarrelBasic.class, 1);
        basic.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        basic.setAllowDuplicate(true);
        register(basic);

        ModuleData normal = new ModuleData("normal_barrel", ModuleBarrelMedium.class, 1);
        normal.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        normal.setAllowDuplicate(true);
        register(normal);

        ModuleData large = new ModuleData("large_barrel", ModuleBarrelBig.class, 1);
        large.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        large.setAllowDuplicate(true);
        register(large);
    }
}
