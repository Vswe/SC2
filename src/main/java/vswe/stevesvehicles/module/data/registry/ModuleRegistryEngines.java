package vswe.stevesvehicles.module.data.registry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import vswe.stevesvehicles.client.rendering.models.ModelCompactSolarPanel;
import vswe.stevesvehicles.client.rendering.models.ModelEngineFrame;
import vswe.stevesvehicles.client.rendering.models.ModelEngineInside;
import vswe.stevesvehicles.client.rendering.models.ModelSolarPanelBase;
import vswe.stevesvehicles.client.rendering.models.ModelSolarPanelHeads;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.module.data.ModuleDataGroup;
import vswe.stevesvehicles.module.data.ModuleRegistry;
import vswe.stevesvehicles.module.data.ModuleSide;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Modules.Engines.ModuleCheatEngine;
import vswe.stevesvehicles.old.Modules.Engines.ModuleCoalStandard;
import vswe.stevesvehicles.old.Modules.Engines.ModuleCoalTiny;
import vswe.stevesvehicles.old.Modules.Engines.ModuleSolarBasic;
import vswe.stevesvehicles.old.Modules.Engines.ModuleSolarCompact;
import vswe.stevesvehicles.old.Modules.Engines.ModuleSolarStandard;
import vswe.stevesvehicles.old.Modules.Engines.ModuleThermalAdvanced;
import vswe.stevesvehicles.old.Modules.Engines.ModuleThermalStandard;
import vswe.stevesvehicles.vehicle.VehicleRegistry;

import static vswe.stevesvehicles.old.Helpers.ComponentTypes.*;


public class ModuleRegistryEngines extends ModuleRegistry {
    public ModuleRegistryEngines() {
        super("steves_vehicles_engines");

        ModuleData coalSmall = new ModuleData("tiny_coal_engine", ModuleCoalTiny.class, 2) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Engine", new ModelEngineFrame());
                addModel("Fire", new ModelEngineInside());
            }
        };

        coalSmall.addShapedRecipeWithSize(3, 2,
                Items.iron_ingot,   Blocks.furnace,     Items.iron_ingot,
                null,               Blocks.piston,      null);

        coalSmall.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(coalSmall);

        ModuleData solarSmall = new ModuleData("basic_solar_engine", ModuleSolarBasic.class, 12) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("SolarPanelBase",new ModelSolarPanelBase());
                addModel("SolarPanels", new ModelSolarPanelHeads(2));
                removeModel("Top");
            }
        };

        solarSmall.addShapedRecipe( SOLAR_PANEL,        Items.iron_ingot,       SOLAR_PANEL,
                                    Items.iron_ingot,   SIMPLE_PCB,             Items.iron_ingot,
                                    null,               Blocks.piston,          null);


        solarSmall.addSides(ModuleSide.CENTER, ModuleSide.TOP);
        solarSmall.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(solarSmall);

        ModuleData thermalSmall = new ModuleData("thermal_engine", ModuleThermalStandard.class, 28);
        thermalSmall.addShapedRecipe(   Blocks.nether_brick,        Blocks.nether_brick,       Blocks.nether_brick,
                                        Blocks.obsidian,   Blocks.furnace,             Blocks.obsidian,
                                        Blocks.piston,               null,          Blocks.piston);


        thermalSmall.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        thermalSmall.addRequirement(ModuleDataGroup.getGroup(ModuleRegistryTanks.TANK_KEY));
        register(thermalSmall);

        ModuleData coalLarge = new ModuleData("coal_engine", ModuleCoalStandard.class, 15) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Engine", new ModelEngineFrame());
                addModel("Fire", new ModelEngineInside());
            }
        };

        coalLarge.addShapedRecipe(  Items.iron_ingot,       Items.iron_ingot,       Items.iron_ingot,
                                    Items.iron_ingot,       Blocks.furnace,         Items.iron_ingot,
                                    Blocks.piston,          null,                   Blocks.piston);

        coalLarge.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(coalLarge);
        ModuleData.addNemesis(coalLarge, coalSmall);


        ModuleData solarLarge = new ModuleData("solar_engine", ModuleSolarStandard.class, 20) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("SolarPanelBase",new ModelSolarPanelBase());
                addModel("SolarPanels", new ModelSolarPanelHeads(4));
                removeModel("Top");
            }
        };

        solarLarge.addShapedRecipe( Items.iron_ingot,   SOLAR_PANEL,    Items.iron_ingot,
                                    SOLAR_PANEL,        ADVANCED_PCB,   SOLAR_PANEL,
                                    Blocks.piston,      SOLAR_PANEL,    Blocks.piston);


        solarLarge.addSides(ModuleSide.CENTER, ModuleSide.TOP);
        solarLarge.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(solarLarge);
        ModuleData.addNemesis(solarLarge, solarSmall);
        engines.add(solarLarge);


        ModuleData thermalLarge = new ModuleData("advanced_thermal_engine", ModuleThermalAdvanced.class, 58);
        thermalLarge.addShapedRecipe(   Blocks.nether_brick,    Blocks.nether_brick,    Blocks.nether_brick,
                                        REINFORCED_METAL,       thermalSmall,           REINFORCED_METAL,
                                        Blocks.piston,          null,                   Blocks.piston);


        thermalLarge.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        thermalLarge.addRequirement(ModuleDataGroup.getGroup(ModuleRegistryTanks.TANK_KEY).copy(ModuleRegistryTanks.TANK_KEY + 2, 2));
        register(thermalLarge);
        ModuleData.addNemesis(thermalLarge, thermalSmall);

        ModuleData solarAdvanced = new ModuleData("compact_solar_engine", ModuleSolarCompact.class, 32) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("SolarPanelSide", new ModelCompactSolarPanel());
            }
        };

        solarAdvanced.addShapedRecipe(  ADVANCED_SOLAR_PANEL,       Items.iron_ingot,       ADVANCED_SOLAR_PANEL,
                                        ADVANCED_PCB,               Items.redstone,         ADVANCED_PCB,
                                        Blocks.piston,              Items.iron_ingot,       Blocks.piston);


        solarAdvanced.addSides(ModuleSide.RIGHT, ModuleSide.LEFT);
        solarAdvanced.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(solarAdvanced);

        ModuleData cheat = new ModuleData("creative_engine", ModuleCheatEngine.class, 1);
        register(cheat);
    }

    public static final String ENGINE_KEY = "Engines";
    private ModuleDataGroup engines = ModuleDataGroup.createGroup(ENGINE_KEY, Localization.MODULE_INFO.ENGINE_GROUP);
    @Override
    public void register(ModuleData moduleData) {
        super.register(moduleData);
        engines.add(moduleData);
    }
}
