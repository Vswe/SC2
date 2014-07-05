package vswe.stevesvehicles.module.data.registry;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import vswe.stevesvehicles.localization.entry.info.LocalizationGroup;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.module.data.ModuleDataGroup;
import vswe.stevesvehicles.module.data.registry.cart.ModuleRegistryCartTools;
import vswe.stevesvehicles.module.common.addon.ModuleCreativeSupplies;
import vswe.stevesvehicles.module.common.addon.ModuleEnchants;
import vswe.stevesvehicles.module.common.addon.ModulePowerObserver;
import vswe.stevesvehicles.vehicle.VehicleRegistry;

import static vswe.stevesvehicles.old.Helpers.ComponentTypes.*;


public class ModuleRegistryUtility extends ModuleRegistry {
    public static final String ENCHANTABLE_KEY = "Enchantable";
    public ModuleRegistryUtility() {
        super("common.util");

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
        enchanter.addRequirement(ModuleDataGroup.getCombinedGroup(ENCHANTABLE_KEY, LocalizationGroup.TOOL_SHOOTER,
                ModuleDataGroup.getGroup(ModuleRegistryCartTools.TOOL_KEY),
                ModuleDataGroup.getGroup(ModuleRegistryShooters.SHOOTER_KEY)
        ));
        register(enchanter);

        ModuleData cheat = new ModuleData("creative_supplies", ModuleCreativeSupplies.class, 1);
        register(cheat);
    }
}
