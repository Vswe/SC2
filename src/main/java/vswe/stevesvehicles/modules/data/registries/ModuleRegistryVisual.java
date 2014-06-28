package vswe.stevesvehicles.modules.data.registries;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import vswe.stevesvehicles.client.rendering.models.ModelNote;
import vswe.stevesvehicles.modules.data.ModuleData;
import vswe.stevesvehicles.modules.data.ModuleRegistry;
import vswe.stevesvehicles.modules.data.ModuleSide;
import vswe.stevesvehicles.old.Modules.Addons.ModuleColorRandomizer;
import vswe.stevesvehicles.old.Modules.Addons.ModuleColorizer;
import vswe.stevesvehicles.old.Modules.Addons.ModuleInvisible;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleFirework;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleNote;
import vswe.stevesvehicles.vehicles.VehicleRegistry;

import static vswe.stevesvehicles.old.Helpers.ComponentTypes.*;

public class ModuleRegistryVisual extends ModuleRegistry {
    public ModuleRegistryVisual() {
        super("steves_vehicles_visual");


        ModuleData colorizer = new ModuleData("colorizer", ModuleColorizer.class, 15);
        colorizer.addShapedRecipe(    RED_PIGMENT,        GREEN_PIGMENT,      BLUE_PIGMENT,
                                    Items.iron_ingot,   Items.redstone,     Items.iron_ingot,
                                    null,               Items.iron_ingot,   null);

        colorizer.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(colorizer);


        ModuleData randomizer = new ModuleData("color_randomizer", ModuleColorRandomizer.class, 20);
        randomizer.addShapelessRecipe(colorizer, SIMPLE_PCB);

        randomizer.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(randomizer);

        ModuleData.addNemesis(colorizer, randomizer);

        ModuleData invisibility = new ModuleData("invisibility_core", ModuleInvisible.class, 21);
        invisibility.addShapedRecipe(   null,               GLASS_O_MAGIC,          null,
                                        Items.iron_ingot,   Items.redstone,         Items.iron_ingot,
                                        null,               Items.iron_ingot,       null);

        invisibility.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(invisibility);


        ModuleData firework = new ModuleData("firework_display", ModuleFirework.class, 30);
        firework.addShapedRecipe(       Blocks.iron_bars,           Blocks.dispenser,           Blocks.iron_bars,
                                        Blocks.crafting_table,      FUSE,                       Blocks.crafting_table,
                                        SIMPLE_PCB,                 Items.flint_and_steel,      SIMPLE_PCB);

        firework.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(firework);


        ModuleData note = new ModuleData("note_sequencer", ModuleNote.class, 30) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                setModelMultiplier(0.65F);
                addModel("Speakers", new ModelNote());
            }
        };

        note.addShapedRecipe(   Blocks.noteblock,      null,                Blocks.noteblock,
                                Blocks.noteblock,      Blocks.jukebox,      Blocks.noteblock,
                                "planks",              Items.redstone,      "planks");

        note.addSides(ModuleSide.RIGHT, ModuleSide.LEFT);
        note.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(note);
    }
}
