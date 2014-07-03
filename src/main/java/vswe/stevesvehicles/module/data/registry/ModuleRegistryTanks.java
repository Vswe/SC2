package vswe.stevesvehicles.module.data.registry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.client.rendering.models.ModelAdvancedTank;
import vswe.stevesvehicles.client.rendering.models.ModelFrontTank;
import vswe.stevesvehicles.client.rendering.models.ModelSideTanks;
import vswe.stevesvehicles.client.rendering.models.ModelTopTank;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.module.data.ModuleDataGroup;
import vswe.stevesvehicles.module.data.ModuleSide;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.module.common.storage.tank.ModuleAdvancedTank;
import vswe.stevesvehicles.module.common.storage.tank.ModuleCheatTank;
import vswe.stevesvehicles.module.common.storage.tank.ModuleFrontTank;
import vswe.stevesvehicles.module.common.storage.tank.ModuleInternalTank;
import vswe.stevesvehicles.module.common.storage.tank.ModuleOpenTank;
import vswe.stevesvehicles.module.common.storage.tank.ModuleSideTanks;
import vswe.stevesvehicles.module.common.storage.tank.ModuleTopTank;
import vswe.stevesvehicles.vehicle.VehicleRegistry;

import static vswe.stevesvehicles.old.Helpers.ComponentTypes.*;

public class ModuleRegistryTanks extends ModuleRegistry {

    public ModuleRegistryTanks() {
        super("steves_vehicles_tanks");

        ModuleData side = new ModuleData("side_tanks", ModuleSideTanks.class, 10) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("SideTanks", new ModelSideTanks());
            }
        };

        side.addShapedRecipe(   HUGE_TANK_PANE,        TANK_PANE,         HUGE_TANK_PANE,
                                LARGE_TANK_PANE,       TANK_VALVE,        LARGE_TANK_PANE,
                                HUGE_TANK_PANE,        TANK_PANE,         HUGE_TANK_PANE);


        side.addSides(ModuleSide.LEFT, ModuleSide.RIGHT);
        side.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(side);



        ModuleData top = new ModuleData("top_tank", ModuleTopTank.class, 22) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("TopTank", new ModelTopTank(false));
            }
        };

        top.addShapedRecipe(    HUGE_TANK_PANE,         HUGE_TANK_PANE,         HUGE_TANK_PANE,
                                TANK_PANE,              TANK_VALVE,             TANK_PANE,
                                HUGE_TANK_PANE,         HUGE_TANK_PANE,         HUGE_TANK_PANE);


        top.addSides(ModuleSide.TOP);
        top.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(top);



        ModuleData front = new ModuleData("front_tank", ModuleFrontTank.class, 15) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                setModelMultiplier(0.68F);
                addModel("FrontTank", new ModelFrontTank());
            }
        };

        front.addShapedRecipe(  TANK_PANE,              LARGE_TANK_PANE,        TANK_PANE,
                                TANK_PANE,              TANK_VALVE,             TANK_PANE,
                                LARGE_TANK_PANE,        LARGE_TANK_PANE,        LARGE_TANK_PANE);


        front.addSides(ModuleSide.FRONT);
        front.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(front);



        ModuleData open = new ModuleData("open_tank", ModuleOpenTank.class, 31) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("TopTank", new ModelTopTank(true));
            }
        };

        open.addShapedRecipe(   TANK_PANE,              null,                   TANK_PANE,
                                TANK_PANE,              TANK_VALVE,             TANK_PANE,
                                HUGE_TANK_PANE,         HUGE_TANK_PANE,         HUGE_TANK_PANE);


        open.addSides(ModuleSide.TOP);
        open.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(open);



        ModuleData internal = new ModuleData("internal_tank", ModuleInternalTank.class, 37);
        internal.addShapedRecipe(   TANK_PANE,        TANK_PANE,        TANK_PANE,
                                    TANK_PANE,        TANK_VALVE,       TANK_PANE,
                                    TANK_PANE,        TANK_PANE,        TANK_PANE);


        internal.setAllowDuplicate(true);
        internal.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(internal);



        ModuleData advanced = new ModuleData("advanced_tank", ModuleAdvancedTank.class, 54) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("LargeTank", new ModelAdvancedTank());
                removeModel("Top")	;
            }
        };

        advanced.addShapedRecipe(   HUGE_TANK_PANE,         HUGE_TANK_PANE,         HUGE_TANK_PANE,
                                    HUGE_TANK_PANE,         TANK_VALVE,             HUGE_TANK_PANE,
                                    HUGE_TANK_PANE,         HUGE_TANK_PANE,         HUGE_TANK_PANE);


        advanced.addSides(ModuleSide.CENTER, ModuleSide.TOP);
        advanced.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(advanced);


        ModuleData cheat = new ModuleData("creative_tank", ModuleCheatTank.class, 1);
        cheat.setAllowDuplicate(true);
        cheat.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        register(cheat);
    }



    public static final String TANK_KEY = "Tanks";
    private ModuleDataGroup tanks = ModuleDataGroup.createGroup(TANK_KEY, Localization.MODULE_INFO.TANK_GROUP);
    @Override
    public void register(ModuleData moduleData) {
        super.register(moduleData);
        tanks.add(moduleData);
    }
}
