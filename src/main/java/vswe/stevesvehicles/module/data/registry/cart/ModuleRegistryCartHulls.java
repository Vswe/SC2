package vswe.stevesvehicles.module.data.registry.cart;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import vswe.stevesvehicles.localization.entry.info.LocalizationMessage;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.module.data.ModuleDataHull;
import vswe.stevesvehicles.module.data.registry.ModuleRegistry;
import vswe.stevesvehicles.module.data.ModuleSide;
import vswe.stevesvehicles.module.common.hull.*;
import vswe.stevesvehicles.holiday.HolidayType;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.client.rendering.models.cart.ModelHull;
import vswe.stevesvehicles.client.rendering.models.common.ModelHullTop;
import vswe.stevesvehicles.client.rendering.models.cart.ModelPigHead;
import vswe.stevesvehicles.client.rendering.models.cart.ModelPigHelmet;
import vswe.stevesvehicles.client.rendering.models.cart.ModelPigTail;
import vswe.stevesvehicles.client.rendering.models.cart.ModelPumpkinHull;
import vswe.stevesvehicles.client.rendering.models.cart.ModelPumpkinHullTop;
import vswe.stevesvehicles.module.common.hull.HullGalgadorian;
import vswe.stevesvehicles.module.common.hull.HullPig;
import vswe.stevesvehicles.module.common.hull.HullPumpkin;
import vswe.stevesvehicles.StevesVehicles;
import vswe.stevesvehicles.vehicle.VehicleRegistry;

import static vswe.stevesvehicles.item.ComponentTypes.*;

public class ModuleRegistryCartHulls extends ModuleRegistry {
    public ModuleRegistryCartHulls() {
        super("cart.hulls");

        ModuleData wood = new ModuleDataHull("wooden_hull", HullWood.class, 50, 1, 2, 0, 15) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/cart/hullModelWooden.png")));
                addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/cart/hullModelWoodenTop.png")));
            }
        };

        wood.addShapedRecipe(   "plankWood",        null,               "plankWood",
                                "plankWood",        "plankWood",        "plankWood",
                                WOODEN_WHEELS,      null,               WOODEN_WHEELS   );

        wood.addVehicles(VehicleRegistry.CART);
        register(wood);



        ModuleData standard = new ModuleDataHull("standard_hull", HullStandard.class, 200, 3, 4, 6, 50) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/cart/hullModelStandard.png")));
                addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/cart/hullModelStandardTop.png")));
            }
        };

        standard.addShapedRecipe(   Items.iron_ingot,   null,               Items.iron_ingot,
                                    Items.iron_ingot,   Items.iron_ingot,   Items.iron_ingot,
                                    IRON_WHEELS,        null,               IRON_WHEELS);

        standard.addVehicles(VehicleRegistry.CART);
        register(standard);



        ModuleData reinforced = new ModuleDataHull("reinforced_hull", HullReinforced.class, 500, 5, 6, 12, 150) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/cart/hullModelLarge.png")));
                addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/cart/hullModelLargeTop.png")));
            }
        };

        reinforced.addShapedRecipe( REINFORCED_METAL,   null,               REINFORCED_METAL,
                                    REINFORCED_METAL,   REINFORCED_METAL,   REINFORCED_METAL,
                                    REINFORCED_WHEELS,  null,               REINFORCED_WHEELS);

        reinforced.addVehicles(VehicleRegistry.CART);
        register(reinforced);



        ModuleData galgadorian = new ModuleDataHull("galgadorian_hull", HullGalgadorian.class, 1000, 5,  6, 12, 150) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/cart/hullModelGalgadorian.png")));
                addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/cart/hullModelGalgadorianTop.png")));
            }
        };

        galgadorian.addShapedRecipe(    GALGADORIAN_METAL,      null,                   GALGADORIAN_METAL,
                GALGADORIAN_METAL,      GALGADORIAN_METAL,      GALGADORIAN_METAL,
                GALGADORIAN_WHEELS,     null,                   GALGADORIAN_WHEELS);

        galgadorian.addVehicles(VehicleRegistry.CART);
        register(galgadorian);



        ModuleData pumpkin = new ModuleDataHull("pumpkin_chariot", HullPumpkin.class, 40, 1, 2, 0, 15) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Hull", new ModelPumpkinHull(ResourceHelper.getResource("/models/cart/hullModelPumpkin.png"), ResourceHelper.getResource("/models/cart/hullModelWooden.png")));
                addModel("Top", new ModelPumpkinHullTop(ResourceHelper.getResource("/models/cart/hullModelPumpkinTop.png"), ResourceHelper.getResource("/models/cart/hullModelWoodenTop.png")));
            }
        };

        pumpkin.addShapedRecipe(    "plankWood",        null,               "plankWood",
                                    "plankWood",        Blocks.pumpkin,     "plankWood",
                                    WOODEN_WHEELS,      null,               WOODEN_WHEELS);

        if (!StevesVehicles.holidays.contains(HolidayType.HALLOWEEN)) {
            pumpkin.lock();
        }

        pumpkin.addVehicles(VehicleRegistry.CART);
        register(pumpkin);

        ModuleData pig = new ModuleDataHull("mechanical_pig", HullPig.class, 150, 2, 4, 4, 50) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/cart/hullModelPig.png")));
                addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/cart/hullModelPigTop.png")));
                addModel("Head", new ModelPigHead());
                addModel("Tail", new ModelPigTail());
                addModel("Helmet", new ModelPigHelmet(false));
                addModel("Helmet_Overlay", new ModelPigHelmet(true));
            }
        };

        pig.addSides(ModuleSide.FRONT);
        pig.addMessage(LocalizationMessage.THUNDER_PIG);

        pig.addShapedRecipe(    Items.porkchop,     null,               Items.porkchop,
                                Items.porkchop,     Items.porkchop,     Items.porkchop,
                                IRON_WHEELS,        null,               IRON_WHEELS);

        pig.addVehicles(VehicleRegistry.CART);
        register(pig);


        ModuleData creative = new ModuleDataHull("creative_hull", HullCreative.class, 10000, 5, 6, 12, 150) {
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
