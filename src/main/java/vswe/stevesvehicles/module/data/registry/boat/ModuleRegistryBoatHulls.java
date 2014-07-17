package vswe.stevesvehicles.module.data.registry.boat;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.client.rendering.models.boat.ModelHull;
import vswe.stevesvehicles.client.rendering.models.cart.ModelHullTop;

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
                //addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelWoodenTop.png")));
            }
        };


        wood.addVehicles(VehicleRegistry.BOAT);
        register(wood);



        ModuleData standard = new ModuleDataHull("standard_hull", HullStandard.class, 200, 3, 4, 6, 50) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/boat/wooden_hull.png")));
                //addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelStandardTop.png")));
            }
        };


        standard.addVehicles(VehicleRegistry.BOAT);
        register(standard);



        ModuleData reinforced = new ModuleDataHull("reinforced_hull", HullReinforced.class, 500, 5, 6, 12, 150) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/boat/wooden_hull.png")));
                //addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelLargeTop.png")));
            }
        };


        reinforced.addVehicles(VehicleRegistry.BOAT);
        register(reinforced);

        //TODO add more hulls
        //TODO add recipes
    }



}
