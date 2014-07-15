package vswe.stevesvehicles.upgrade.registry;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.upgrade.Upgrade;
import vswe.stevesvehicles.upgrade.effect.fuel.CombustionFuel;
import vswe.stevesvehicles.upgrade.effect.fuel.FuelCapacity;
import vswe.stevesvehicles.upgrade.effect.fuel.FuelCost;
import vswe.stevesvehicles.upgrade.effect.fuel.Recharger;
import vswe.stevesvehicles.upgrade.effect.fuel.Solar;
import vswe.stevesvehicles.upgrade.effect.fuel.ThermalFuel;

import static vswe.stevesvehicles.item.ComponentTypes.*;

public class UpgradeRegistryPower extends UpgradeRegistry {
    public UpgradeRegistryPower() {
        super("power");


        Upgrade battery = new Upgrade("batteries");
        battery.addEffect(FuelCapacity.class, 5000);
        battery.addEffect(Recharger.class, 40);

        battery.addShapedRecipe(    Items.redstone,     Items.redstone,                     Items.redstone,
                                    Items.redstone,     new ItemStack(Items.dye, 1, 4),     Items.redstone,
                                    Items.redstone,     BLANK_UPGRADE,                      Items.redstone);

        register(battery);



        Upgrade crystal = new Upgrade("power_crystal");
        crystal.addEffect(FuelCapacity.class, 15000);
        crystal.addEffect(Recharger.class, 150);

        crystal.addShapedRecipe(    Items.redstone,         Items.glowstone_dust,       Items.redstone,
                                    Items.glowstone_dust,   Blocks.lapis_block,         Items.glowstone_dust,
                                    Items.diamond,          battery,                    Items.diamond);

        register(crystal);



        Upgrade co2 = new Upgrade("co2_friendly");
        co2.addEffect(FuelCost.class, 0.5F);

        co2.addShapedRecipe(    null,           Blocks.piston,      null,
                                SIMPLE_PCB,     Blocks.iron_bars,   SIMPLE_PCB,
                                CLEANING_FAN,   BLANK_UPGRADE,      CLEANING_FAN);

        register(co2);



        Upgrade engine = new Upgrade("generic_engine");
        engine.addEffect(CombustionFuel.class);
        engine.addEffect(FuelCost.class, 1.03F);

        engine.addShapedRecipe( null,               SIMPLE_PCB,         null,
                                Blocks.piston,      Blocks.furnace,     Blocks.piston,
                                Items.iron_ingot,   BLANK_UPGRADE,      Items.iron_ingot);

        register(engine);



        Upgrade thermal = new Upgrade("thermal_engine_upgrade");
        thermal.addEffect(ThermalFuel.class);
        thermal.addEffect(FuelCost.class, 1.03F);

        thermal.addShapedRecipe(    Blocks.nether_brick,    ADVANCED_PCB,       Blocks.nether_brick,
                                    Blocks.piston,          Blocks.furnace,     Blocks.piston,
                                    Blocks.obsidian,        BLANK_UPGRADE,      Blocks.obsidian);
        register(thermal);



        Upgrade solar = new Upgrade("solar_panel");
        solar.addEffect(Solar.class);

        solar.addShapedRecipe(  SOLAR_PANEL,        SOLAR_PANEL,        SOLAR_PANEL,
                                Items.redstone,     Items.diamond,      Items.redstone,
                                Items.redstone,     BLANK_UPGRADE,      Items.redstone);

        register(solar);
    }
}
