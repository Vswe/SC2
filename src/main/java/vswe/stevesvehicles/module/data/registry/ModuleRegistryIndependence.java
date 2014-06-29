package vswe.stevesvehicles.module.data.registry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import vswe.stevesvehicles.client.rendering.models.ModelDynamite;
import vswe.stevesvehicles.client.rendering.models.ModelShield;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.module.data.ModuleRegistry;
import vswe.stevesvehicles.module.data.ModuleSide;
import vswe.stevesvehicles.old.Modules.Addons.ModuleChunkLoader;
import vswe.stevesvehicles.old.Modules.Addons.ModuleShield;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleDynamite;
import vswe.stevesvehicles.vehicle.VehicleRegistry;

import static vswe.stevesvehicles.old.Helpers.ComponentTypes.*;


public class ModuleRegistryIndependence extends ModuleRegistry {
    public ModuleRegistryIndependence() {
        super("steves_vehicles_independence");

        ModuleData dynamite = new ModuleData("dynamite_carrier", ModuleDynamite.class, 3) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Tnt",new ModelDynamite());
            }
        };

        dynamite.addShapedRecipe(   null,           DYNAMITE,                   null,
                                    DYNAMITE,       Items.flint_and_steel,      DYNAMITE,
                                    null,           DYNAMITE,                   null);

        dynamite.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        dynamite.addSides(ModuleSide.TOP);
        register(dynamite);



        ModuleData shield = new ModuleData("divine_shield", ModuleShield.class, 60) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Shield", new ModelShield());
                setModelMultiplier(0.68F);
            }
        };

        shield.addShapedRecipe(     Blocks.obsidian,        REFINED_HARDENER,       Blocks.obsidian,
                                    REFINED_HARDENER,       Blocks.diamond_block,   REFINED_HARDENER,
                                    Blocks.obsidian,        REFINED_HARDENER,       Blocks.obsidian);

        shield.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(shield);



        ModuleData chunk = new ModuleData("chunk_loader", ModuleChunkLoader.class, 84);

        chunk.addShapedRecipe(  null,                   Items.ender_pearl,          null,
                                SIMPLE_PCB,             Items.iron_ingot,           SIMPLE_PCB,
                                REINFORCED_METAL,       ADVANCED_PCB,               REINFORCED_METAL);

        chunk.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(chunk);
    }
}
