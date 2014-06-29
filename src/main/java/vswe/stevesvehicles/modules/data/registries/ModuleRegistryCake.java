package vswe.stevesvehicles.modules.data.registries;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Items;
import vswe.stevesvehicles.client.rendering.models.ModelCake;
import vswe.stevesvehicles.modules.data.ModuleData;
import vswe.stevesvehicles.modules.data.ModuleRegistry;
import vswe.stevesvehicles.modules.data.ModuleSide;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleCakeServer;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleCakeServerDynamite;
import vswe.stevesvehicles.old.StevesVehicles;
import vswe.stevesvehicles.vehicles.VehicleRegistry;

import static vswe.stevesvehicles.old.Helpers.ComponentTypes.*;


public class ModuleRegistryCake extends ModuleRegistry {
    public ModuleRegistryCake() {
        super("steves_vehicle_cake");

        ModuleData cake = new ModuleData("cake_server", ModuleCakeServer.class, 10) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Cake", new ModelCake());
            }
        };

        cake.addShapedRecipe(   null,           Items.cake,         null,
                                "slabWood",     "slabWood",         "slabWood",
                                null,           SIMPLE_PCB,         null);

        cake.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        cake.addSides(ModuleSide.TOP);
        cake.addMessage(Localization.MODULE_INFO.ALPHA_MESSAGE);
        register(cake);


        ModuleData trick = new ModuleData("trick_or_treat_cake_server", ModuleCakeServerDynamite.class, 15) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Cake", new ModelCake());
            }
        };

        trick.addShapedRecipe(      null,           Items.cake,         null,
                                    "slabWood",     "slabWood",         "slabWood",
                                    DYNAMITE,       SIMPLE_PCB,         DYNAMITE);

        trick.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        trick.addSides(ModuleSide.TOP);
        register(trick);

        if (!StevesVehicles.isHalloween) {
            trick.lock();
        }
    }
}
