package vswe.stevesvehicles.module.data.registry;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import vswe.stevesvehicles.client.rendering.models.ModelNote;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.module.data.ModuleSide;
import vswe.stevesvehicles.module.common.addon.ModuleColorRandomizer;
import vswe.stevesvehicles.module.common.addon.ModuleColorizer;
import vswe.stevesvehicles.module.common.addon.ModuleInvisible;
import vswe.stevesvehicles.module.common.addon.ModuleLabel;
import vswe.stevesvehicles.module.common.attachment.ModuleFirework;
import vswe.stevesvehicles.module.common.attachment.ModuleNote;
import vswe.stevesvehicles.vehicle.VehicleRegistry;

import static vswe.stevesvehicles.item.ComponentTypes.*;

public class ModuleRegistryVisual extends ModuleRegistry {
    public ModuleRegistryVisual() {
        super("common.visual");


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
                                "plankWood",           Items.redstone,      "plankWood");

        note.addSides(ModuleSide.RIGHT, ModuleSide.LEFT);
        note.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(note);



        ModuleData info = new ModuleData("information_provider", ModuleLabel.class, 12);
        info.addShapedRecipe(   Blocks.glass_pane,      Blocks.glass_pane,          Blocks.glass_pane,
                                Items.iron_ingot,       Items.glowstone_dust,       Items.iron_ingot,
                                SIMPLE_PCB,             Items.sign,                 SIMPLE_PCB);

        info.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(info);
    }
}
