package vswe.stevesvehicles.modules.data.registries.carts;


import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import vswe.stevesvehicles.modules.data.ModuleData;
import vswe.stevesvehicles.modules.data.ModuleDataGroup;
import vswe.stevesvehicles.modules.data.ModuleRegistry;
import vswe.stevesvehicles.old.Modules.Addons.Plants.ModuleModTrees;
import vswe.stevesvehicles.old.Modules.Addons.Plants.ModuleNetherwart;
import vswe.stevesvehicles.old.Modules.Addons.Plants.ModulePlantSize;
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
