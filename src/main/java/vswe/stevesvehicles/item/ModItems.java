package vswe.stevesvehicles.item;


import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.config.Configuration;
import vswe.stevesvehicles.module.data.registry.ModuleRegistry;
import vswe.stevesvehicles.block.ModBlocks;
import vswe.stevesvehicles.tileentity.detector.DetectorType;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.StevesVehicles;

import static vswe.stevesvehicles.item.ComponentTypes.*;

import java.util.HashMap;

public final class ModItems {

    public static ItemVehicles vehicles;
    public static ItemCartComponent component;
    public static ItemVehicleModule modules;
    public static ItemUpgrade upgrades;
    public static ItemBlockStorage storage;
    public static ItemBlockDetector detectors;


    private static final String VEHICLE_NAME = "ModularVehicle";
    private static final String COMPONENTS_NAME = "ModuleComponents";
    private static final String MODULES_NAME = "CartModule";

    private static HashMap<String,Boolean> validModules = new HashMap<String,Boolean>();

    public static void preBlockInit(Configuration config) {
        (vehicles = new ItemVehicles()).setUnlocalizedName(StevesVehicles.localStart + VEHICLE_NAME);
        component = new ItemCartComponent();
        modules = new ItemVehicleModule();

        GameRegistry.registerItem(vehicles, VEHICLE_NAME);
        GameRegistry.registerItem(component, COMPONENTS_NAME);
        GameRegistry.registerItem(modules, MODULES_NAME);


        for (ModuleData module : ModuleRegistry.getAllModules()) {
            if (!module.getIsLocked()) {
                validModules.put(module.getFullRawUnlocalizedName(), config.get("EnabledModules", module.getName().replace(" ", "").replace(":","_"), module.getEnabledByDefault()).getBoolean(true));
            }
        }

        for (int i = 0; i < ItemCartComponent.size(); i++) {
            ItemStack subComponent = new ItemStack(component,1,i);
            GameRegistry.registerCustomItemStack(subComponent.getUnlocalizedName(), subComponent);
        }

    }

    public static void postBlockInit(Configuration config) {
        detectors = (ItemBlockDetector) new ItemStack(ModBlocks.DETECTOR_UNIT.getBlock()).getItem();
        upgrades = (ItemUpgrade) new ItemStack(ModBlocks.UPGRADE.getBlock()).getItem();
        storage = (ItemBlockStorage) new ItemStack(ModBlocks.STORAGE.getBlock()).getItem();



        for (int i = 0; i < ItemBlockStorage.blocks.length; i++) {
            ItemStack storage = new ItemStack(ModItems.storage, 1, i);
            GameRegistry.registerCustomItemStack(storage.getUnlocalizedName(), storage);
        }

        for (DetectorType type : DetectorType.values()) {
            ItemStack stack = new ItemStack(detectors, 1, type.getMeta());
            GameRegistry.registerCustomItemStack(stack.getUnlocalizedName(), stack);
        }
    }


    private static final String PLANK = "plankWood";
    private static final String WOOD = "logWood";
    private static final String RED = "dyeRed";
    private static final String GREEN = "dyeGreen";
    private static final String BLUE = "dyeBlue";
    private static final String ORANGE = "dyeOrange";
    private static final String YELLOW = "dyeYellow";
    private static final String SAPLING = "treeSapling";

    public static void addRecipes() {
        for (ModuleData module : ModuleRegistry.getAllModules()) {
            if (!module.getIsLocked() && validModules.get(module.getFullRawUnlocalizedName())) {
                module.loadRecipes();
            }
        }



        WOODEN_WHEELS.addShapedRecipe(  null,           Items.stick,     null,
                                        Items.stick,    PLANK,           Items.stick,
                                        null,           Items.stick,     null);


        IRON_WHEELS.addShapedRecipe(    null,            Items.stick,       null,
                                        Items.stick,    Items.iron_ingot,   Items.stick,
                                        null,           Items.stick,        null);



        RED_PIGMENT.addShapedRecipe(    null,   Items.glowstone_dust,   null,
                                        RED,    RED,                    RED,
                                        null,   Items.glowstone_dust,   null);

        GREEN_PIGMENT.addShapedRecipe(  null,   Items.glowstone_dust,   null,
                                        GREEN,  GREEN,                  GREEN,
                                        null,   Items.glowstone_dust,   null);

        BLUE_PIGMENT.addShapedRecipe(   null,   Items.glowstone_dust,   null,
                                        BLUE,   BLUE,                   BLUE,
                                        null,   Items.glowstone_dust,   null);


        GLASS_O_MAGIC.addShapedRecipe(  Blocks.glass_pane,      Items.fermented_spider_eye,     Blocks.glass_pane,
                                        Blocks.glass_pane,      Items.redstone,                 Blocks.glass_pane,
                                        Blocks.glass_pane,      Blocks.glass_pane,              Blocks.glass_pane);


        FUSE.addShapedRecipeWithSizeAndCount(1, 3, 12,
                Items.string,
                Items.string,
                Items.string);


        DYNAMITE.addShapedRecipeWithSize(1, 3,
                FUSE,
                Items.gunpowder,
                Items.gunpowder);



        SIMPLE_PCB.addShapedRecipe( Items.iron_ingot,   Items.redstone,     Items.iron_ingot,
                                    Items.redstone,     Items.gold_ingot,   Items.redstone,
                                    Items.iron_ingot,   Items.redstone,     Items.iron_ingot);

        SIMPLE_PCB.addShapedRecipe( Items.redstone,     Items.iron_ingot,   Items.redstone,
                                    Items.iron_ingot,   Items.gold_ingot,   Items.iron_ingot,
                                    Items.redstone,     Items.iron_ingot,   Items.redstone);


        GRAPHICAL_INTERFACE.addShapedRecipe(    Items.gold_ingot,   Items.diamond,          Items.gold_ingot,
                                                Blocks.glass_pane,  SIMPLE_PCB,             Blocks.glass_pane,
                                                Items.redstone,     Blocks.glass_pane,      Items.redstone);


        RAW_HANDLE.addShapedRecipe( null,               null,               Items.iron_ingot,
                                    null,               Items.iron_ingot,   null,
                                    Items.iron_ingot,   null,               null);
        FurnaceRecipes.smelting().func_151394_a(RAW_HANDLE.getItemStack(), REFINED_HANDLE.getItemStack(), 0F);


        SPEED_HANDLE.addShapedRecipe(   null,               null,               BLUE,
                                        Items.gold_ingot,   REFINED_HANDLE,     null,
                                        Items.redstone,     Items.gold_ingot,   null);


        WHEEL.addShapedRecipe(  Items.iron_ingot,   Items.stick,        Items.iron_ingot,
                                Items.stick,        Items.iron_ingot,   Items.stick,
                                null,               Items.stick,        null);


        SAW_BLADE.addShapedRecipeWithSize(3, 1,
                Items.iron_ingot, Items.iron_ingot, Items.diamond);


        ADVANCED_PCB.addShapedRecipe(Items.redstone, Items.iron_ingot, Items.redstone,
                SIMPLE_PCB, Items.iron_ingot, SIMPLE_PCB,
                Items.redstone, Items.iron_ingot, Items.redstone);


        WOOD_CUTTING_CORE.addShapedRecipe(  SAPLING,    SAPLING,        SAPLING,
                                            SAPLING,    ADVANCED_PCB,   SAPLING,
                                            SAPLING,    SAPLING,        SAPLING);


        RAW_HARDENER.addShapelessRecipeWithCount(4, Items.diamond, Blocks.obsidian, Blocks.obsidian, Blocks.obsidian, Blocks.obsidian);

        FurnaceRecipes.smelting().func_151394_a(RAW_HARDENER.getItemStack(), REFINED_HARDENER.getItemStack(), 0F);


        HARDENED_MESH.addShapedRecipe(  Blocks.iron_bars,   REFINED_HARDENER,   Blocks.iron_bars,
                                        REFINED_HARDENER,   Blocks.iron_bars,   REFINED_HARDENER,
                                        Blocks.iron_bars,   REFINED_HARDENER,   Blocks.iron_bars);



        STABILIZED_METAL.addShapedRecipeWithSizeAndCount(3, 3, 5,
                Items.iron_ingot,   HARDENED_MESH,      Items.iron_ingot,
                Items.iron_ingot,   Items.iron_ingot,   Items.iron_ingot,
                REFINED_HARDENER,   REFINED_HARDENER,   REFINED_HARDENER);

        FurnaceRecipes.smelting().func_151394_a(STABILIZED_METAL.getItemStack(), REINFORCED_METAL.getItemStack(), 0F);



        REINFORCED_WHEELS.addShapedRecipe(  null,               Items.iron_ingot,   null,
                                            Items.iron_ingot,   REINFORCED_METAL,   Items.iron_ingot,
                                            null,               Items.iron_ingot,   null);


        PIPE.addShapedRecipeWithSize(3, 2,
                Blocks.stone,       Blocks.stone,   Blocks.stone,
                Items.iron_ingot,   null,           null);


        SHOOTING_STATION.addShapedRecipe(   Items.redstone,     null,               Items.redstone,
                                            Items.redstone,     Items.gold_ingot,   Items.redstone,
                                            Blocks.dispenser,   SIMPLE_PCB,         Blocks.dispenser);


        ENTITY_SCANNER.addShapedRecipe( Items.gold_ingot,   SIMPLE_PCB,     Items.gold_ingot,
                                        Items.redstone,     ADVANCED_PCB,   Items.redstone,
                                        Items.redstone,     null,           Items.redstone);


        ENTITY_ANALYZER.addShapedRecipe(    Items.iron_ingot,   Items.redstone,     Items.iron_ingot,
                                            Items.iron_ingot,   SIMPLE_PCB,         Items.iron_ingot,
                                            Items.iron_ingot,   Items.iron_ingot,   Items.iron_ingot);



        EMPTY_DISK.addShapedRecipeWithSize(1, 2,
                Items.redstone,
                SIMPLE_PCB);


        TRI_TORCH.addShapedRecipeWithSize(3, 1,
                Blocks.torch, Blocks.torch, Blocks.torch);




        CHEST_LOCK.addShapedRecipeWithSizeAndCount(1, 2, 8,
                Items.iron_ingot,
                Blocks.stone);

        CHEST_LOCK.addShapedRecipeWithSizeAndCount(1, 2, 8,
                Blocks.stone,
                Items.iron_ingot);


        CLEANING_FAN.addShapedRecipe(   Blocks.iron_bars,   Items.redstone,     Blocks.iron_bars,
                                        Items.redstone,     null,               Items.redstone,
                                        Blocks.iron_bars,   Items.redstone,     Blocks.iron_bars);


        CLEANING_CORE.addShapedRecipe(  CLEANING_FAN,       Items.iron_ingot,   CLEANING_FAN,
                                        CLEANING_TUBE,      CLEANING_TUBE,      CLEANING_TUBE,
                                        Items.iron_ingot,   CLEANING_TUBE,      Items.iron_ingot);


        CLEANING_TUBE.addShapedRecipeWithSizeAndCount(3, 3, 2,
                ORANGE,     Items.iron_ingot,   ORANGE,
                ORANGE,     Items.iron_ingot,   ORANGE,
                ORANGE,     Items.iron_ingot,   ORANGE);



        SOLAR_PANEL.addShapelessRecipe(Items.glowstone_dust, Items.glowstone_dust, Items.redstone, Items.iron_ingot);


        EYE_OF_GALGADOR.addShapedRecipe(Items.magma_cream, Items.fermented_spider_eye, Items.magma_cream,
                Items.ghast_tear, Items.ender_eye, Items.ghast_tear,
                Items.magma_cream, Items.fermented_spider_eye, Items.magma_cream);



        LUMP_OF_GALGADOR.addShapedRecipeWithSizeAndCount(3, 3, 2,
                Items.glowstone_dust,   Blocks.diamond_block,   Items.glowstone_dust,
                EYE_OF_GALGADOR,        Items.glowstone_dust,   EYE_OF_GALGADOR,
                STABILIZED_METAL,       EYE_OF_GALGADOR,        STABILIZED_METAL);


        FurnaceRecipes.smelting().func_151394_a(LUMP_OF_GALGADOR.getItemStack(), GALGADORIAN_METAL.getItemStack(), 0F);


        LARGE_LUMP_OF_GALGADOR.addShapedRecipe( LUMP_OF_GALGADOR,   LUMP_OF_GALGADOR,   LUMP_OF_GALGADOR,
                                                LUMP_OF_GALGADOR,   LUMP_OF_GALGADOR,   LUMP_OF_GALGADOR,
                                                LUMP_OF_GALGADOR,   LUMP_OF_GALGADOR,   LUMP_OF_GALGADOR);



        FurnaceRecipes.smelting().func_151394_a(LARGE_LUMP_OF_GALGADOR.getItemStack(), ENHANCED_GALGADORIAN_METAL.getItemStack(), 0F);


        RED_GIFT_RIBBON.addShapedRecipe(    Items.string,   Items.string,   Items.string,
                                            Items.string,   RED,            Items.string,
                                            Items.string,   Items.string,   Items.string);



        YELLOW_GIFT_RIBBON.addShapedRecipe(     Items.string,   Items.string,   Items.string,
                                                Items.string,   YELLOW,            Items.string,
                                                Items.string,   Items.string,   Items.string);

        ItemStack redWool = new ItemStack(Blocks.wool, 1, 14);
        ItemStack whiteWool = new ItemStack(Blocks.wool, 1, 0);
        WARM_HAT.addShapedRecipe(   null,       redWool,            whiteWool,
                                    redWool,    Items.emerald,      redWool,
                                    redWool,    redWool,            redWool);


        SOCK.addShapedRecipe(   redWool,    redWool,    Items.cookie,
                                redWool,    redWool,    Items.milk_bucket,
                                redWool,    redWool,    redWool);


        ADVANCED_SOLAR_PANEL.addShapedRecipe(   SOLAR_PANEL,        null,           SOLAR_PANEL,
                                                Items.iron_ingot,   SIMPLE_PCB,     Items.iron_ingot,
                                                SOLAR_PANEL,        null,           SOLAR_PANEL);

        ADVANCED_SOLAR_PANEL.addShapedRecipe(   SOLAR_PANEL,        Items.iron_ingot,           SOLAR_PANEL,
                                                null,               SIMPLE_PCB,                 null,
                                                SOLAR_PANEL,        Items.iron_ingot,           SOLAR_PANEL);




        BLANK_UPGRADE.addShapedRecipeWithSizeAndCount(3, 3, 2,
                Items.iron_ingot, Items.iron_ingot, Items.iron_ingot,
                Items.redstone, Blocks.obsidian, Items.redstone,
                Items.iron_ingot, SIMPLE_PCB, Items.iron_ingot);



        TANK_VALVE.addShapedRecipeWithSizeAndCount(3, 3, 8,
                null,               Items.iron_ingot,       null,
                Items.iron_ingot,   Blocks.iron_bars,       Items.iron_ingot,
                null,               Items.iron_ingot,       null);



        LIQUID_CLEANING_CORE.addShapedRecipe(CLEANING_FAN, Items.iron_ingot, CLEANING_FAN,
                LIQUID_CLEANING_TUBE, LIQUID_CLEANING_TUBE, LIQUID_CLEANING_TUBE,
                Items.iron_ingot, LIQUID_CLEANING_TUBE, Items.iron_ingot);

        LIQUID_CLEANING_TUBE.addShapedRecipeWithSizeAndCount(3, 3, 2,
                GREEN, Items.iron_ingot, GREEN,
                GREEN, Items.iron_ingot, GREEN,
                GREEN, Items.iron_ingot, GREEN);



        EXPLOSIVE_EASTER_EGG.addShapedRecipeWithSizeAndCount(3, 3, 16,
                Items.gunpowder,    Items.gunpowder,    Items.gunpowder,
                Items.gunpowder,    Items.egg,          Items.gunpowder,
                Items.gunpowder,    GREEN,              Items.gunpowder);


        BURNING_EASTER_EGG.addShapedRecipeWithSizeAndCount(3, 3, 16,
                Items.blaze_powder,     Items.blaze_rod,    Items.blaze_powder,
                Items.blaze_powder,     Items.egg,          Items.blaze_powder,
                RED,                    Items.magma_cream,  YELLOW);




        GLISTERING_EASTER_EGG.addShapedRecipeWithSizeAndCount(3, 3, 16,
                Items.gold_nugget,  Items.gold_nugget,   Items.gold_nugget,
                Items.gold_nugget,  Items.egg,           Items.gold_nugget,
                Items.gold_nugget,  BLUE,                Items.gold_nugget);



        ItemStack chocolate = new ItemStack(Items.dye, 1, 3);
        CHOCOLATE_EASTER_EGG.addShapedRecipeWithSizeAndCount(3, 3, 16,
                chocolate,  Items.sugar,    chocolate,
                chocolate,  Items.egg,      chocolate,
                chocolate,  Items.sugar,    chocolate);


        BASKET.addShapedRecipe( Items.stick,    Items.stick,    Items.stick,
                                Items.stick,    null,           Items.stick,
                                PLANK,          PLANK,          PLANK);




        HARDENED_SAW_BLADE.addShapedRecipeWithSize(3, 1,
                Items.iron_ingot, Items.iron_ingot, REINFORCED_METAL);

        GALGADORIAN_SAW_BLADE.addShapedRecipeWithSize(3, 1,
                Items.iron_ingot, Items.iron_ingot, GALGADORIAN_METAL);


        GALGADORIAN_WHEELS.addShapedRecipe( null,               REINFORCED_METAL,   null,
                                            REINFORCED_METAL,   GALGADORIAN_METAL,  REINFORCED_METAL,
                                            null,               REINFORCED_METAL,   null);



        IRON_BLADE.addShapedRecipeWithSizeAndCount(3, 3, 4,
                null, Items.shears, null,
                Items.iron_ingot, Items.iron_ingot, Items.iron_ingot,
                null, Items.iron_ingot, null);


        BLADE_ARM.addShapedRecipe(  IRON_BLADE,     null,               BLADE_ARM,
                                    null,           Items.iron_ingot,   null,
                                    IRON_BLADE,     null,               IRON_BLADE);



        ItemBlockStorage.loadRecipes();
    }


    private ModItems() {}
}
