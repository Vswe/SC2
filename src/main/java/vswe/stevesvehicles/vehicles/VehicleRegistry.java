package vswe.stevesvehicles.vehicles;


import vswe.stevesvehicles.registries.IRegistry;
import vswe.stevesvehicles.registries.RegistryLoader;
import vswe.stevesvehicles.vehicles.entities.EntityModularCart;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class VehicleRegistry implements IRegistry<VehicleType> {
    public static final VehicleType CART = new VehicleType(EntityModularCart.class, "Steve's Cart");
    public static final VehicleType BOAT = new VehicleType(EntityModularCart.class, "Steve's Boat");

    public static void init() {
        loader = new RegistryLoader<VehicleRegistry, VehicleType>();
        instance = new VehicleRegistry();
        loader.add(instance);

        instance.register(CART);
        instance.register(BOAT);
    }

    private static RegistryLoader<VehicleRegistry, VehicleType> loader;
    private static VehicleRegistry instance;

    public static VehicleRegistry getInstance() {
        return instance;
    }

    private Map<String, VehicleType> vehicles;

    private VehicleRegistry() {
        vehicles = new HashMap<String, VehicleType>();
    }

    @Override
    public final String getCode() {
        return "VehicleRegistry";
    }

    public void register(VehicleType vehicleType) {
        if (vehicles.containsKey(vehicleType.getName())) {
            System.err.println("A vehicle with this raw name has already been registered. Failed to register a second module with the raw name " + vehicleType.getName() + ".");
        }else{
            vehicles.put(vehicleType.getName(), vehicleType);
        }
    }

    @Override
    public String getFullCode(VehicleType obj) {
        return obj.getName();
    }

    @Override
    public Collection<VehicleType> getElements() {
        return vehicles.values();
    }

    public VehicleType getTypeFromName(String name) {
        return loader.getObjectFromName(name);
    }

    public VehicleType getTypeFromId(int id) {
        return loader.getObjectFromId(id);
    }

    public int getIdFromType(VehicleType vehicle) {
        return loader.getIdFromObject(vehicle);
    }
}
