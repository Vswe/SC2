package vswe.stevesvehicles.vehicle;


import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import vswe.stevesvehicles.StevesVehicles;
import vswe.stevesvehicles.registry.IRegistry;
import vswe.stevesvehicles.registry.RegistryLoader;
import vswe.stevesvehicles.vehicle.entity.EntityModularBoat;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleRegistry implements IRegistry<VehicleType> {
    public static final VehicleType CART = new VehicleType(EntityModularCart.class, "cart");
    public static final VehicleType BOAT = new VehicleType(EntityModularBoat.class, "boat");

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
    private List<VehicleType> allVehicles;

    private VehicleRegistry() {
        vehicles = new HashMap<String, VehicleType>();
        allVehicles = new ArrayList<VehicleType>();
    }

    @Override
    public final String getCode() {
        return "VehicleRegistry";
    }

    public void register(VehicleType vehicleType) {
        if (vehicles.containsKey(vehicleType.getUnlocalizedName())) {
            System.err.println("A vehicle with this raw name has already been registered. Failed to register a second module with the raw name " + vehicleType.getUnlocalizedName() + ".");
        }else{
            vehicles.put(vehicleType.getUnlocalizedName(), vehicleType);
            allVehicles.add(vehicleType);
            EntityRegistry.registerModEntity((Class<? extends Entity>)vehicleType.getClazz(), vehicleType.getRawUnlocalizedName(), allVehicles.size(), StevesVehicles.instance, 80, 3, true);
        }
    }

    @Override
    public String getFullCode(VehicleType obj) {
        return obj.getUnlocalizedName();
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

    public List<VehicleType> getAllVehicles() {
        return allVehicles;
    }
}
