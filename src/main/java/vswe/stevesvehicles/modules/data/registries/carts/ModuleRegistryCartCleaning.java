package vswe.stevesvehicles.modules.data.registries.carts;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import vswe.stevesvehicles.client.rendering.models.ModelCleaner;
import vswe.stevesvehicles.client.rendering.models.ModelHullTop;
import vswe.stevesvehicles.client.rendering.models.ModelLawnMower;
import vswe.stevesvehicles.client.rendering.models.ModelLiquidDrainer;
import vswe.stevesvehicles.modules.data.ModuleData;
import vswe.stevesvehicles.modules.data.ModuleRegistry;
import vswe.stevesvehicles.modules.data.ModuleSide;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleCleaner;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleExperience;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleFlowerRemover;
import vswe.stevesvehicles.old.Modules.Workers.ModuleLiquidDrainer;
import vswe.stevesvehicles.vehicles.VehicleRegistry;

import static vswe.stevesvehicles.old.Helpers.ComponentTypes.*;



public class ModuleRegistryCartCleaning extends ModuleRegistry {
    public ModuleRegistryCartCleaning() {
        super("steves_carts_cleaning");

        ModuleData cleaner = new ModuleData("cleaning_machine", ModuleCleaner.class, 23) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Top",  new ModelHullTop(ResourceHelper.getResource("/models/cleanerModelTop.png"), false));
                addModel("Cleaner", new ModelCleaner());
            }
        };
        cleaner.addShapedRecipe(    CLEANING_TUBE,      CLEANING_CORE,      CLEANING_TUBE,
                                    CLEANING_TUBE,      null,               CLEANING_TUBE,
                                    CLEANING_TUBE,      null,               CLEANING_TUBE);


        cleaner.addSides(ModuleSide.CENTER);
        cleaner.addVehicles(VehicleRegistry.CART);
        register(cleaner);


        ModuleData liquid = new ModuleData("liquid_cleaner", ModuleLiquidDrainer.class, 30) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Top",  new ModelHullTop(ResourceHelper.getResource("/models/cleanerModelTop.png"), false));
                addModel("Cleaner", new ModelLiquidDrainer());
            }
        };
        liquid.addShapedRecipe(     LIQUID_CLEANING_TUBE,      LIQUID_CLEANING_CORE,        LIQUID_CLEANING_TUBE,
                                    LIQUID_CLEANING_TUBE,      null,                        LIQUID_CLEANING_TUBE,
                                    LIQUID_CLEANING_TUBE,      null,                        LIQUID_CLEANING_TUBE);


        liquid.addSides(ModuleSide.CENTER);
        liquid.addVehicles(VehicleRegistry.CART);
        liquid.lockByDefault(); //TODO remove this when it works properly
        register(liquid);


        //TODO figure out how to do these
        //addNemesis(frontChest, cleaner);
        //addNemesis(frontTank, cleaner);
        //addNemesis(frontTank, liquid);
        //addNemesis(frontChest, liquid);
        //liquid.addParent(liquidsensors)


        ModuleData experience = new ModuleData("experience_bank", ModuleExperience.class, 36);
        experience.addShapedRecipe( null,                   Items.redstone,     null,
                                    Items.glowstone_dust,   Items.emerald,      Items.glowstone_dust,
                                    SIMPLE_PCB,             Blocks.cauldron,    SIMPLE_PCB);


        experience.addVehicles(VehicleRegistry.CART);
        register(experience);
    }
}
