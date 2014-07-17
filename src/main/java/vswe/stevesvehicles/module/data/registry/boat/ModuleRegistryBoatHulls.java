package vswe.stevesvehicles.module.data.registry.boat;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.client.rendering.models.boat.ModelHull;

import vswe.stevesvehicles.client.rendering.models.common.ModelHullTop;
import vswe.stevesvehicles.module.common.hull.HullCreative;
import vswe.stevesvehicles.module.common.hull.HullGalgadorian;
import vswe.stevesvehicles.module.common.hull.HullReinforced;
import vswe.stevesvehicles.module.common.hull.HullStandard;
import vswe.stevesvehicles.module.common.hull.HullWood;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.module.data.ModuleDataHull;
import vswe.stevesvehicles.module.data.registry.ModuleRegistry;
import vswe.stevesvehicles.vehicle.VehicleRegistry;



public class ModuleRegistryBoatHulls extends ModuleRegistry {
    public ModuleRegistryBoatHulls() {
        super("boat.hulls");

        ModuleData wood = new ModuleDataHull("wooden_hull", HullWood.class, 50, 1, 2, 0, 15) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/boat/wooden_hull.png")));
                addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/cart/hullModelWoodenTop.png")));
            }
        };


        wood.addVehicles(VehicleRegistry.BOAT);
        register(wood);



        ModuleData standard = new ModuleDataHull("standard_hull", HullStandard.class, 200, 3, 4, 6, 50) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/boat/standard_hull.png")));
                addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/cart/hullModelStandardTop.png")));
            }
        };


        standard.addVehicles(VehicleRegistry.BOAT);
        register(standard);



        ModuleData reinforced = new ModuleDataHull("reinforced_hull", HullReinforced.class, 500, 5, 6, 12, 150) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/boat/reinforced_hull.png")));
                addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/cart/hullModelLargeTop.png")));
            }
        };


        reinforced.addVehicles(VehicleRegistry.BOAT);
        register(reinforced);



        ModuleData galgadorian = new ModuleDataHull("galgadorian_hull", HullGalgadorian.class, 1000, 5,  6, 12, 150) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/boat/galgadorian_hull.png")));
                addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/cart/hullModelGalgadorianTop.png")));
            }
        };

        galgadorian.addVehicles(VehicleRegistry.BOAT);
        register(galgadorian);


        ModuleData creative = new ModuleDataHull("creative_hull", HullCreative.class, 10000, 5, 6, 12, 150) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/boat/creative_hull.png")));
                addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/cart/hullModelCreativeTop.png")));
            }
        };

        creative.addVehicles(VehicleRegistry.BOAT);
        register(creative);

        //TODO add recipes
    }



}
