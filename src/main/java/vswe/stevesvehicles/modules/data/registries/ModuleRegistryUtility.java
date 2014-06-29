package vswe.stevesvehicles.modules.data.registries;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import vswe.stevesvehicles.modules.data.ModuleData;
import vswe.stevesvehicles.modules.data.ModuleDataGroup;
import vswe.stevesvehicles.modules.data.ModuleRegistry;
import vswe.stevesvehicles.modules.data.registries.carts.ModuleRegistryCartTools;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Modules.Addons.ModuleCreativeSupplies;
import vswe.stevesvehicles.old.Modules.Addons.ModuleEnchants;
import vswe.stevesvehicles.old.Modules.Addons.ModulePowerObserver;
import vswe.stevesvehicles.vehicles.VehicleRegistry;

import static vswe.stevesvehicles.old.Helpers.ComponentTypes.*;


public class ModuleRegistryUtility extends ModuleRegistry {
    public static final String ENCHANTABLE_KEY = "Enchantable";
    public ModuleRegistryUtility() {
        super("steves_vehicle_util");

        ModuleData observer = new ModuleData("power_observer", ModulePowerObserver.class, 12);
        observer.addShapedRecipe(   null,               Blocks.piston,      null,
                                    Items.iron_ingot,   Items.redstone,     Items.iron_ingot,
                                    Items.redstone,     ADVANCED_PCB ,      Items.redstone);

        observer.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        observer.addRequirement(ModuleDataGroup.getGroup(ModuleRegistryEngines.ENGINE_KEY));
        register(observer);



        ModuleData enchanter = new ModuleData("enchanter", ModuleEnchants.class, 72);
        enchanter.addShapedRecipe(  null,               GALGADORIAN_METAL,              null,
                                    Items.book,         Blocks.enchanting_table,        Items.book,
                                    Items.redstone,     ADVANCED_PCB,                   Items.redstone);

        enchanter.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        enchanter.addRequirement(ModuleDataGroup.getCombinedGroup(ENCHANTABLE_KEY, Localization.MODULE_INFO.TOOL_OR_SHOOTER_GROUP,
                ModuleDataGroup.getGroup(ModuleRegistryCartTools.TOOL_KEY),
                ModuleDataGroup.getGroup(ModuleRegistryShooters.SHOOTER_KEY)
        ));
        register(enchanter);

        ModuleData cheat = new ModuleData("creative_supplies", ModuleCreativeSupplies.class, 1);
        register(cheat);
    }
}
