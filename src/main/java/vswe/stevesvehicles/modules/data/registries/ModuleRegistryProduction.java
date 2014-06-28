package vswe.stevesvehicles.modules.data.registries;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import vswe.stevesvehicles.modules.data.ModuleData;
import vswe.stevesvehicles.modules.data.ModuleDataGroup;
import vswe.stevesvehicles.modules.data.ModuleRegistry;
import vswe.stevesvehicles.old.Modules.Addons.ModuleColorizer;
import vswe.stevesvehicles.old.Modules.Addons.ModuleCrafter;
import vswe.stevesvehicles.old.Modules.Addons.ModuleCrafterAdv;
import vswe.stevesvehicles.old.Modules.Addons.ModuleSmelter;
import vswe.stevesvehicles.old.Modules.Addons.ModuleSmelterAdv;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleMilker;
import vswe.stevesvehicles.vehicles.VehicleRegistry;


import static vswe.stevesvehicles.old.Helpers.ComponentTypes.*;

public class ModuleRegistryProduction extends ModuleRegistry {

    public ModuleRegistryProduction() {
        super("steves_vehicles_production");

        ModuleData crafter = new ModuleData("crafter", ModuleCrafter.class, 22);
        crafter.addShapelessRecipe(SIMPLE_PCB, Blocks.crafting_table);

        crafter.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        crafter.setAllowDuplicate(true);
        register(crafter);


        ModuleData smelter = new ModuleData("smelter", ModuleSmelter.class, 22);
        smelter.addShapelessRecipe(SIMPLE_PCB, Blocks.furnace);

        smelter.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        smelter.setAllowDuplicate(true);
        register(smelter);


        ModuleData crafterAdvanced = new ModuleData("advanced_crafter", ModuleCrafterAdv.class, 42);
        crafterAdvanced.addShapedRecipe(    Items.redstone,         SIMPLE_PCB,       Items.redstone,
                                            SIMPLE_PCB,             crafter,          SIMPLE_PCB,
                                            Items.redstone,         SIMPLE_PCB,       Items.redstone);

        crafterAdvanced.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        crafterAdvanced.setAllowDuplicate(true);
        register(crafterAdvanced);


        ModuleData smelterAdvanced = new ModuleData("advanced_crafter", ModuleSmelterAdv.class, 42);
        smelterAdvanced.addShapedRecipe(    Items.redstone,         SIMPLE_PCB,       Items.redstone,
                                            SIMPLE_PCB,             smelter,          SIMPLE_PCB,
                                            Items.redstone,         SIMPLE_PCB,       Items.redstone);

        smelterAdvanced.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        smelterAdvanced.setAllowDuplicate(true);
        register(smelterAdvanced);


        ModuleData milker = new ModuleData("milker", ModuleMilker.class, 26);
        smelterAdvanced.addShapedRecipe(    Items.redstone,         SIMPLE_PCB,       Items.redstone,
                SIMPLE_PCB,             smelter,          SIMPLE_PCB,
                Items.redstone,         SIMPLE_PCB,       Items.redstone);

        smelterAdvanced.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        milker.addRequirement(ModuleDataGroup.getGroup(ModuleRegistryTravel.CAGE_KEY));
        register(smelterAdvanced);
    }
}
