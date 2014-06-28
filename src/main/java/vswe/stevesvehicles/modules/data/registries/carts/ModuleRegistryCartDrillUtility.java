package vswe.stevesvehicles.modules.data.registries.carts;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import vswe.stevesvehicles.client.rendering.models.ModelLiquidSensors;
import vswe.stevesvehicles.modules.data.ModuleData;
import vswe.stevesvehicles.modules.data.ModuleDataGroup;
import vswe.stevesvehicles.modules.data.registries.ModuleRegistryTanks;
import vswe.stevesvehicles.old.Modules.Addons.ModuleCreativeIncinerator;
import vswe.stevesvehicles.old.Modules.Addons.ModuleDrillIntelligence;
import vswe.stevesvehicles.old.Modules.Addons.ModuleIncinerator;
import vswe.stevesvehicles.old.Modules.Addons.ModuleLiquidSensors;
import vswe.stevesvehicles.old.Modules.Addons.ModuleOreTracker;
import vswe.stevesvehicles.vehicles.VehicleRegistry;
import vswe.stevesvehicles.modules.data.ModuleRegistry;

import static vswe.stevesvehicles.old.Helpers.ComponentTypes.*;

public class ModuleRegistryCartDrillUtility extends ModuleRegistry{
    public ModuleRegistryCartDrillUtility() {
        super("steves_carts_drill_utility");

        ModuleData liquidSensors = new ModuleData("liquid_sensors", ModuleLiquidSensors.class, 27) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Sensor", new ModelLiquidSensors());
            }
        };

        liquidSensors.addShapedRecipe(  Items.redstone,         null,               Items.redstone,
                                        Items.lava_bucket,      Items.diamond,      Items.water_bucket,
                                        Items.iron_ingot,       ADVANCED_PCB,       Items.iron_ingot);

        liquidSensors.addVehicles(VehicleRegistry.CART);
        register(liquidSensors);


        ModuleData incinerator = new ModuleData("incinerator", ModuleIncinerator.class, 23);

        incinerator.addShapedRecipe(    Blocks.nether_brick,    Blocks.nether_brick,    Blocks.nether_brick,
                                        Blocks.obsidian,        Blocks.furnace,         Blocks.obsidian,
                                        Blocks.nether_brick,    Blocks.nether_brick,    Blocks.nether_brick);

        incinerator.addVehicles(VehicleRegistry.CART);
        incinerator.addRequirement(ModuleDataGroup.getGroup(ModuleRegistryTanks.TANK_KEY));
        register(liquidSensors);



        ModuleData cheatIncinerator = new ModuleData("creative_incinerator", ModuleCreativeIncinerator.class, 1);
        cheatIncinerator.addVehicles(VehicleRegistry.CART);
        register(cheatIncinerator);


        ModuleData intelligence = new ModuleData("drill_intelligence", ModuleDrillIntelligence.class, 21);

        intelligence.addShapedRecipe(Items.gold_ingot, Items.gold_ingot, Items.gold_ingot,
                Items.iron_ingot, ADVANCED_PCB, Items.iron_ingot,
                ADVANCED_PCB, Items.redstone, ADVANCED_PCB);

        intelligence.addVehicles(VehicleRegistry.CART);
        register(intelligence);


        ModuleData extractor = new ModuleData("ore_extractor", ModuleOreTracker.class, 80);

        extractor.addShapedRecipe(  Blocks.redstone_torch,      null,                   Blocks.redstone_torch,
                                    EYE_OF_GALGADOR,            GALGADORIAN_METAL,      EYE_OF_GALGADOR,
                                    Items.quartz,               ADVANCED_PCB,           Items.quartz);

        extractor.addVehicles(VehicleRegistry.CART);
        register(extractor);
    }


    private ModuleDataGroup drills = ModuleDataGroup.getGroup(ModuleRegistryCartTools.TOOL_KEY);
    @Override
    public void register(ModuleData moduleData) {
        super.register(moduleData);
        moduleData.addRequirement(drills);
    }
}
