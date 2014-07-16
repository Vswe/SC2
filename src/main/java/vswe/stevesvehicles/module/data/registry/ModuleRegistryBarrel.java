package vswe.stevesvehicles.module.data.registry;



import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import vswe.stevesvehicles.module.common.storage.barrel.ModuleBarrelBasic;
import vswe.stevesvehicles.module.common.storage.barrel.ModuleBarrelBig;
import vswe.stevesvehicles.module.common.storage.barrel.ModuleBarrelMedium;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.vehicle.VehicleRegistry;

import static vswe.stevesvehicles.item.ComponentTypes.*;

public class ModuleRegistryBarrel extends ModuleRegistry {
    private static final String PLANK = "plankWood";

    public ModuleRegistryBarrel() {
        super("common.barrel");

        ModuleData basic = new ModuleData("basic_barrel", ModuleBarrelBasic.class, 10);
        basic.addShapedRecipe(  PLANK,      SIMPLE_PCB,     PLANK,
                                PLANK,      Blocks.chest,   PLANK,
                                PLANK,      PLANK,          PLANK);

                basic.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        basic.setAllowDuplicate(true);
        register(basic);

        ModuleData normal = new ModuleData("normal_barrel", ModuleBarrelMedium.class, 30);
        normal.addShapedRecipe( Items.iron_ingot,      SIMPLE_PCB,          Items.iron_ingot,
                                Items.iron_ingot,      basic,               Items.iron_ingot,
                                Items.iron_ingot,      Items.iron_ingot,    Items.iron_ingot);

        normal.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        normal.setAllowDuplicate(true);
        register(normal);

        ModuleData large = new ModuleData("large_barrel", ModuleBarrelBig.class, 50);
        large.addShapedRecipe(  Items.gold_ingot,      SIMPLE_PCB,          Items.gold_ingot,
                                Items.gold_ingot,      normal,              Items.gold_ingot,
                                Items.gold_ingot,      Items.gold_ingot,    Items.gold_ingot);

        large.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        large.setAllowDuplicate(true);
        register(large);
    }
}
