package vswe.stevesvehicles.modules.data.registries.carts;


import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.modules.data.ModuleData;
import vswe.stevesvehicles.modules.data.ModuleDataGroup;
import vswe.stevesvehicles.modules.data.ModuleRegistry;
import vswe.stevesvehicles.modules.data.registries.ModuleRegistryTanks;
import vswe.stevesvehicles.old.Modules.Addons.Plants.ModuleModTrees;
import vswe.stevesvehicles.old.Modules.Addons.Plants.ModuleNetherwart;
import vswe.stevesvehicles.old.Modules.Addons.Plants.ModulePlantSize;
import vswe.stevesvehicles.old.Modules.Workers.ModuleFertilizer;
import vswe.stevesvehicles.old.Modules.Workers.ModuleHydrater;
import vswe.stevesvehicles.vehicles.VehicleRegistry;

import static vswe.stevesvehicles.old.Helpers.ComponentTypes.*;

import static vswe.stevesvehicles.old.Helpers.ComponentTypes.EMPTY_DISK;

public class ModuleRegistryCultivationUtil extends ModuleRegistry {
    public ModuleRegistryCultivationUtil() {
        super("steves_carts_cultivation");

        loadFarmingUtil();
        loadWoodCuttingUtil();
    }

    private void loadFarmingUtil() {
        ModuleDataGroup farmers = ModuleDataGroup.getGroup(ModuleRegistryTools.FARM_KEY);

        ModuleData netherWart = new ModuleData("crop_nether_wart", ModuleNetherwart.class, 20);
        netherWart.addShapedRecipeWithSize(1, 2,
                Items.nether_wart,
                EMPTY_DISK);


        netherWart.addVehicles(VehicleRegistry.CART);
        netherWart.addRequirement(farmers);
        register(netherWart);



        ModuleData hydrator = new ModuleData("hydrator", ModuleHydrater.class, 6);
        hydrator.addShapedRecipeWithSize(3, 2,
                Items.iron_ingot,       Items.glass_bottle,         Items.iron_ingot,
                null,                   Blocks.iron_bars,           null);


        hydrator.addVehicles(VehicleRegistry.CART);
        hydrator.addRequirement(ModuleDataGroup.getGroup(ModuleRegistryTanks.TANK_KEY));
        register(hydrator);



        ModuleData fertilizer = new ModuleData("fertilizer", ModuleFertilizer.class, 10);
        fertilizer.addShapedRecipe(     new ItemStack(Items.dye, 1, 15),        null,                   new ItemStack(Items.dye, 1, 15),
                                        Items.glass_bottle,                     Items.leather,          Items.glass_bottle,
                                        Items.leather,                          SIMPLE_PCB,             Items.leather);


        fertilizer.addVehicles(VehicleRegistry.CART);
        register(fertilizer);
    }

    private void loadWoodCuttingUtil() {
        ModuleDataGroup cutters = ModuleDataGroup.getGroup(ModuleRegistryTools.WOOD_KEY);

        ModuleData exotic = new ModuleData("tree_exotic", ModuleModTrees.class, 30);
        exotic.addShapedRecipeWithSize(1, 2,
                Blocks.sapling,
                EMPTY_DISK);


        exotic.addVehicles(VehicleRegistry.CART);
        exotic.addRequirement(cutters);
        register(exotic);


        ModuleData range = new ModuleData("planter_ranger_extender", ModulePlantSize.class, 20);
        range.addShapedRecipe(Items.redstone, ADVANCED_PCB, Items.redstone,
                null, Blocks.sapling, null,
                SIMPLE_PCB, Blocks.sapling, SIMPLE_PCB);


        range.addVehicles(VehicleRegistry.CART);
        range.addRequirement(cutters);
        register(range);
    }

}
