package vswe.stevesvehicles.upgrade.registry;


import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.upgrade.Upgrade;
import vswe.stevesvehicles.upgrade.effect.assembly.WorkEfficiency;
import vswe.stevesvehicles.upgrade.effect.fuel.FuelCost;
import vswe.stevesvehicles.upgrade.effect.fuel.FuelCostFree;
import vswe.stevesvehicles.upgrade.effect.time.TimeFlat;
import vswe.stevesvehicles.upgrade.effect.time.TimeFlatCart;
import vswe.stevesvehicles.upgrade.effect.time.TimeFlatRemoved;

import static vswe.stevesvehicles.old.Helpers.ComponentTypes.*;

public class UpgradeRegistryProduction extends UpgradeRegistry {
    public UpgradeRegistryProduction() {
        super("steves_vehicle_production");

        Upgrade knowledge = new Upgrade("module_knowledge");
        knowledge.addEffect(TimeFlat.class, -750);
        knowledge.addEffect(TimeFlatCart.class, -5000);

        knowledge.addShapedRecipe(  Items.book,         Blocks.bookshelf,           Items.book,
                                    Blocks.bookshelf,   Blocks.enchanting_table,    Blocks.bookshelf,
                                    Items.iron_ingot,   BLANK_UPGRADE,              Items.iron_ingot);

        register(knowledge);



        Upgrade espionage = new Upgrade("industrial_espionage");
        espionage.addEffect(TimeFlat.class, -2500);
        espionage.addEffect(TimeFlatCart.class, -14000);

        espionage.addShapedRecipe(  Blocks.bookshelf,       Items.iron_ingot,   Blocks.bookshelf,
                                    Items.glowstone_dust,   REINFORCED_METAL,   Items.glowstone_dust,
                                    REINFORCED_METAL,       knowledge,          REINFORCED_METAL);

        register(espionage);



        Upgrade experienced = new Upgrade("experienced_assembler");
        experienced.addEffect(WorkEfficiency.class, 1.0F);
        experienced.addEffect(FuelCost.class, 2F);

        experienced.addShapedRecipe(    SIMPLE_PCB,             Items.book,         SIMPLE_PCB,
                                        Items.iron_ingot,       ADVANCED_PCB,       Items.iron_ingot,
                                        Items.iron_ingot,       BLANK_UPGRADE,      Items.iron_ingot);

        register(experienced);



        Upgrade era = new Upgrade("new_era");
        era.addEffect(WorkEfficiency.class, 2.5F);
        era.addEffect(FuelCost.class, 6F);

        era.addShapedRecipe(    EYE_OF_GALGADOR,    Items.book,     EYE_OF_GALGADOR,
                                Items.iron_ingot,    SIMPLE_PCB,    Items.iron_ingot,
                                EYE_OF_GALGADOR,    experienced,    EYE_OF_GALGADOR);

        register(era);



        Upgrade demolisher = new Upgrade("quick_demolisher");
        demolisher.addEffect(TimeFlatRemoved.class, -8000);

        demolisher.addShapedRecipe(     Blocks.obsidian,    Items.iron_ingot,       Blocks.obsidian,
                                        Items.iron_ingot,   Blocks.iron_block,      Items.iron_ingot,
                                        Blocks.obsidian,    BLANK_UPGRADE,          Blocks.obsidian);

        register(demolisher);



        Upgrade entropy = new Upgrade("entropy");
        entropy.addEffect(TimeFlatRemoved.class, -32000);
        entropy.addEffect(TimeFlat.class, 500);

        entropy.addShapedRecipe(Blocks.lapis_block,     Items.iron_ingot,       null,
                                Items.redstone,         EYE_OF_GALGADOR,        Items.redstone,
                                null,                   demolisher,             Blocks.lapis_block);

        register(entropy);



        Upgrade cheat = new Upgrade("creative_mode");
        cheat.addEffect(WorkEfficiency.class, 10000F);
        cheat.addEffect(FuelCostFree.class);

        register(cheat);
    }
}
