package vswe.stevesvehicles.modules.data.registries;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import vswe.stevesvehicles.modules.data.ModuleData;
import vswe.stevesvehicles.modules.data.ModuleDataHull;
import vswe.stevesvehicles.modules.data.ModuleRegistry;
import vswe.stevesvehicles.modules.data.ModuleSide;
import vswe.stevesvehicles.modules.hull.*;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.client.rendering.models.ModelHull;
import vswe.stevesvehicles.client.rendering.models.ModelHullTop;
import vswe.stevesvehicles.client.rendering.models.ModelPigHead;
import vswe.stevesvehicles.client.rendering.models.ModelPigHelmet;
import vswe.stevesvehicles.client.rendering.models.ModelPigTail;
import vswe.stevesvehicles.client.rendering.models.ModelPumpkinHull;
import vswe.stevesvehicles.client.rendering.models.ModelPumpkinHullTop;
import vswe.stevesvehicles.modules.hull.HullGalgadorian;
import vswe.stevesvehicles.modules.hull.HullPig;
import vswe.stevesvehicles.modules.hull.HullPumpkin;
import vswe.stevesvehicles.old.StevesVehicles;
import vswe.stevesvehicles.vehicles.VehicleRegistry;

import static vswe.stevesvehicles.old.Helpers.ComponentTypes.*;

public class ModuleRegistryCartHulls extends ModuleRegistry {
    public ModuleRegistryCartHulls() {
        super("steves_carts_hulls");

        ModuleData wood = new ModuleDataHull("wooden_hull", HullWood.class, 50, 1, 0, 15) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelWooden.png")));
                addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelWoodenTop.png")));
            }
        };

        wood.addShapedRecipe(   "planks",        null,          "planks",
                                "planks",       "planks",       "planks",
                                WOODEN_WHEELS,   null,          WOODEN_WHEELS   );

        wood.addVehicles(VehicleRegistry.CART);
        register(wood);



        ModuleData standard = new ModuleDataHull("standard_hull", HullStandard.class, 200, 3, 6, 50) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelStandard.png")));
                addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelStandardTop.png")));
            }
        };

        standard.addShapedRecipe(   Items.iron_ingot,   null,               Items.iron_ingot,
                                    Items.iron_ingot,   Items.iron_ingot,   Items.iron_ingot,
                                    IRON_WHEELS,        null,               IRON_WHEELS);

        standard.addVehicles(VehicleRegistry.CART);
        register(standard);



        ModuleData reinforced = new ModuleDataHull("reinforced_hull", HullReinforced.class, 500, 5, 12, 150) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelLarge.png")));
                addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelLargeTop.png")));
            }
        };

        reinforced.addShapedRecipe( REINFORCED_METAL,   null,               REINFORCED_METAL,
                                    REINFORCED_METAL,   REINFORCED_METAL,   REINFORCED_METAL,
                                    REINFORCED_WHEELS,  null,               REINFORCED_WHEELS);

        reinforced.addVehicles(VehicleRegistry.CART);
        register(reinforced);



        ModuleData galgadorian = new ModuleDataHull("galgadorian_hull", HullGalgadorian.class, 1000, 5, 12, 150) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelGalgadorian.png")));
                addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelGalgadorianTop.png")));
            }
        };

        galgadorian.addShapedRecipe(    GALGADORIAN_METAL,      null,                   GALGADORIAN_METAL,
                GALGADORIAN_METAL,      GALGADORIAN_METAL,      GALGADORIAN_METAL,
                GALGADORIAN_WHEELS,     null,                   GALGADORIAN_WHEELS);

        galgadorian.addVehicles(VehicleRegistry.CART);
        register(galgadorian);



        ModuleData pumpkin = new ModuleDataHull("pumpkin_chariot", HullPumpkin.class, 40, 1, 0, 15) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Hull", new ModelPumpkinHull(ResourceHelper.getResource("/models/hullModelPumpkin.png"), ResourceHelper.getResource("/models/hullModelWooden.png")));
                addModel("Top", new ModelPumpkinHullTop(ResourceHelper.getResource("/models/hullModelPumpkinTop.png"), ResourceHelper.getResource("/models/hullModelWoodenTop.png")));
            }
        };

        pumpkin.addShapedRecipe(    "planks",       null,           "planks",
                                    "planks",       Blocks.pumpkin, "planks",
                                    WOODEN_WHEELS,  null,           WOODEN_WHEELS);

        if (!StevesVehicles.isHalloween) {
            pumpkin.lock();
        }

        pumpkin.addVehicles(VehicleRegistry.CART);
        register(pumpkin);

        ModuleData pig = new ModuleDataHull("mechanical_pig", HullPig.class, 150, 2, 4, 50) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelPig.png")));
                addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelPigTop.png")));
                addModel("Head", new ModelPigHead());
                addModel("Tail", new ModelPigTail());
                addModel("Helmet", new ModelPigHelmet(false));
                addModel("Helmet_Overlay", new ModelPigHelmet(true));
            }
        };

        pig.addSides(ModuleSide.FRONT);
        pig.addMessage(Localization.MODULE_INFO.PIG_MESSAGE);

        pig.addShapedRecipe(    Items.porkchop,     null,               Items.porkchop,
                                Items.porkchop,     Items.porkchop,     Items.porkchop,
                                IRON_WHEELS,        null,               IRON_WHEELS);

        pig.addVehicles(VehicleRegistry.CART);
        register(pig);


        ModuleData creative = new ModuleDataHull("creative_hull", HullCreative.class, 10000, 5, 12, 150) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelCreative.png")));
                addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelCreativeTop.png")));
            }
        };

        creative.addVehicles(VehicleRegistry.CART);
        register(creative);
    }



}
