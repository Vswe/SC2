package vswe.stevesvehicles.modules.data.registries;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Items;
import vswe.stevesvehicles.client.rendering.models.ModelExtractingChests;
import vswe.stevesvehicles.client.rendering.models.ModelFrontChest;
import vswe.stevesvehicles.client.rendering.models.ModelSideChests;
import vswe.stevesvehicles.client.rendering.models.ModelTopChest;
import vswe.stevesvehicles.modules.data.ModuleData;
import vswe.stevesvehicles.modules.data.ModuleRegistry;
import vswe.stevesvehicles.modules.data.ModuleSide;
import vswe.stevesvehicles.old.Modules.Storages.Chests.ModuleExtractingChests;
import vswe.stevesvehicles.old.Modules.Storages.Chests.ModuleFrontChest;
import vswe.stevesvehicles.old.Modules.Storages.Chests.ModuleSideChests;
import vswe.stevesvehicles.old.Modules.Storages.Chests.ModuleTopChest;
import vswe.stevesvehicles.vehicles.VehicleRegistry;

import static vswe.stevesvehicles.old.Helpers.ComponentTypes.*;


public class ModuleRegistryChests extends ModuleRegistry {
    public ModuleRegistryChests() {
        super("steves_vehicles_chests");

        ModuleData side = new ModuleData("side_chests", ModuleSideChests.class, 3) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("SideChest", new ModelSideChests());
            }
        };

        side.addShapedRecipe(   HUGE_CHEST_PANE,        CHEST_PANE,         HUGE_CHEST_PANE,
                                LARGE_CHEST_PANE,       CHEST_LOCK,         LARGE_CHEST_PANE,
                                HUGE_CHEST_PANE,        CHEST_PANE,         HUGE_CHEST_PANE);


        side.addSides(ModuleSide.LEFT, ModuleSide.RIGHT);
        side.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(side);



        ModuleData top = new ModuleData("top_chest", ModuleTopChest.class, 6) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                removeModel("Top");
                addModel("TopChest", new ModelTopChest());
            }
        };

        top.addShapedRecipe(    HUGE_CHEST_PANE,        HUGE_CHEST_PANE,        HUGE_CHEST_PANE,
                                CHEST_PANE,             CHEST_LOCK,             CHEST_PANE,
                                HUGE_CHEST_PANE,        HUGE_CHEST_PANE,        HUGE_CHEST_PANE);


        top.addSides(ModuleSide.TOP);
        top.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(top);



        ModuleData front = new ModuleData("front_chest", ModuleFrontChest.class, 5) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("FrontChest", new ModelFrontChest());
                setModelMultiplier(0.68F);
            }
        };

        front.addShapedRecipe(  CHEST_PANE,             LARGE_CHEST_PANE,           CHEST_PANE,
                                CHEST_PANE,             CHEST_LOCK,                 CHEST_PANE,
                                LARGE_CHEST_PANE,       LARGE_CHEST_PANE,           LARGE_CHEST_PANE);


        front.addSides(ModuleSide.FRONT);
        front.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(front);


        ModuleData internal = new ModuleData("internal_storage", ModuleFrontChest.class, 5);
        internal.addShapedRecipe(   CHEST_PANE,       CHEST_PANE,       CHEST_PANE,
                                    CHEST_PANE,       CHEST_LOCK,       CHEST_PANE,
                                    CHEST_PANE,       CHEST_PANE,       CHEST_PANE);


        internal.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(internal);


        ModuleData extracting = new ModuleData("extracting_chests", ModuleExtractingChests.class, 75) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("SideChest", new ModelExtractingChests());
            }
        };

        extracting.addShapedRecipe(     HUGE_IRON_PANE,        HUGE_IRON_PANE,          HUGE_IRON_PANE,
                                        LARGE_IRON_PANE,       CHEST_LOCK,              LARGE_IRON_PANE,
                                        HUGE_DYNAMIC_PANE,     LARGE_DYNAMIC_PANE,      HUGE_DYNAMIC_PANE);


        extracting.addSides(ModuleSide.LEFT, ModuleSide.RIGHT, ModuleSide.CENTER);
        extracting.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(extracting);

    }
}
