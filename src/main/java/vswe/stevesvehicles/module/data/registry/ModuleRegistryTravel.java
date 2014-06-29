package vswe.stevesvehicles.module.data.registry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vswe.stevesvehicles.client.rendering.models.ModelCage;
import vswe.stevesvehicles.client.rendering.models.ModelSeat;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.module.data.ModuleDataGroup;
import vswe.stevesvehicles.module.data.ModuleRegistry;
import vswe.stevesvehicles.module.data.ModuleSide;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleArcade;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleCage;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleSeat;
import vswe.stevesvehicles.vehicle.VehicleRegistry;

import static vswe.stevesvehicles.old.Helpers.ComponentTypes.*;

public class ModuleRegistryTravel extends ModuleRegistry {
    public static final String SEAT_KEY = "Seats";
    public static final String CAGE_KEY = "Cages";
    public ModuleRegistryTravel() {
        super("steves_vehicle_travel");

        ModuleDataGroup seats = ModuleDataGroup.createGroup(SEAT_KEY, Localization.MODULE_INFO.AND); //TODO give proper localzied name
        ModuleDataGroup cages = ModuleDataGroup.createGroup(CAGE_KEY, Localization.MODULE_INFO.AND); //TODO give proper localzied name

        ModuleData seat = new ModuleData("seat", ModuleSeat.class, 3) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                removeModel("Top");
                addModel("Chair", new ModelSeat());
            }
        };

        seat.addShapedRecipeWithSize(2, 3,
                null,                                                               "planks",
                null,                                                               "planks",
                new ItemStack(Blocks.wooden_slab, 1, OreDictionary.WILDCARD_VALUE), "planks");

        seat.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        seat.addSides(ModuleSide.CENTER, ModuleSide.TOP);
        seats.add(seat);
        register(seat);



        ModuleData arcade = new ModuleData("steves_arcade", ModuleArcade.class, 10);

        arcade.addShapedRecipe( null,               Blocks.glass_pane,      null,
                                "planks",           SIMPLE_PCB,             "planks",
                                Items.redstone,     "planks",               Items.redstone);

        arcade.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        arcade.addRequirement(seats);
        register(arcade);




        ModuleData cage = new ModuleData("cage", ModuleCage.class, 7) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                removeModel("Top");
                addModel("Cage", new ModelCage(false), false);
                addModel("Cage", new ModelCage(true), true);
                setModelMultiplier(0.65F);
            }
        };

        cage.addShapedRecipe(   Blocks.iron_bars,     Blocks.iron_bars,     Blocks.iron_bars,
                                Blocks.iron_bars,     SIMPLE_PCB,           Blocks.iron_bars,
                                Blocks.iron_bars,     Blocks.iron_bars,     Blocks.iron_bars);

        cage.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        cage.addSides(ModuleSide.CENTER, ModuleSide.TOP);
        cages.add(cage);
        register(cage);
    }
}
