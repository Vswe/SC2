package vswe.stevesvehicles.modules.data.registries.carts;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import vswe.stevesvehicles.client.rendering.models.ModelBridge;
import vswe.stevesvehicles.client.rendering.models.ModelRailer;
import vswe.stevesvehicles.client.rendering.models.ModelToolPlate;
import vswe.stevesvehicles.client.rendering.models.ModelTorchplacer;
import vswe.stevesvehicles.client.rendering.models.ModelTrackRemover;
import vswe.stevesvehicles.modules.data.ModuleData;
import vswe.stevesvehicles.modules.data.ModuleRegistry;
import vswe.stevesvehicles.modules.data.ModuleSide;
import vswe.stevesvehicles.old.Modules.Addons.ModuleHeightControl;
import vswe.stevesvehicles.old.Modules.Workers.ModuleBridge;
import vswe.stevesvehicles.old.Modules.Workers.ModuleRailer;
import vswe.stevesvehicles.old.Modules.Workers.ModuleRailerLarge;
import vswe.stevesvehicles.old.Modules.Workers.ModuleRemover;
import vswe.stevesvehicles.old.Modules.Workers.ModuleTorch;
import vswe.stevesvehicles.vehicles.VehicleRegistry;

import static vswe.stevesvehicles.old.Helpers.ComponentTypes.*;

public class ModuleRegistryCartRails extends ModuleRegistry {
    public ModuleRegistryCartRails() {
        super("steves_vehicle_rails");

        ModuleData railer = new ModuleData("railer", ModuleRailer.class, 3) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Rails", new ModelRailer(3));
            }
        };

        railer.addShapedRecipe( Blocks.stone,       Blocks.stone,       Blocks.stone,
                                Items.iron_ingot,   Blocks.rail,        Items.iron_ingot,
                                Blocks.stone,       Blocks.stone,       Blocks.stone);

        railer.addVehicles(VehicleRegistry.CART);
        register(railer);



        ModuleData railerLarge = new ModuleData("large_railer", ModuleRailerLarge.class, 17) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Rails", new ModelRailer(6));
            }
        };

        railerLarge.addShapedRecipe(    Items.iron_ingot,       Items.iron_ingot,       Items.iron_ingot,
                                        railer,                 Blocks.rail,            railer,
                                        Blocks.stone,           Blocks.stone,           Blocks.stone);

        railerLarge.addVehicles(VehicleRegistry.CART);
        register(railerLarge);

        ModuleData.addNemesis(railer, railerLarge);


        ModuleData torch = new ModuleData("torch_placer", ModuleTorch.class, 14) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Torch", new ModelTorchplacer());
            }
        };

        torch.addShapedRecipe(  TRI_TORCH,              null,                   TRI_TORCH,
                                Items.iron_ingot,       null,                   Items.iron_ingot,
                                Items.iron_ingot,       Items.iron_ingot,       Items.iron_ingot);

        torch.addSides(ModuleSide.RIGHT, ModuleSide.LEFT);
        torch.addVehicles(VehicleRegistry.CART);
        register(torch);


        ModuleData bridge = new ModuleData("bridge_builder", ModuleBridge.class, 14) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Bridge", new ModelBridge());
                addModel("Plate", new ModelToolPlate());
            }
        };

        bridge.addShapedRecipe(    null,                   Items.redstone,     null,
                                        Blocks.brick_block,     SIMPLE_PCB,         Blocks.brick_block,
                                        null,                   Blocks.piston,      null);

        bridge.addVehicles(VehicleRegistry.CART);
        register(bridge);


        ModuleData remover = new ModuleData("track_remover", ModuleRemover.class, 8) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Remover", new ModelTrackRemover());
                setModelMultiplier(0.60F);
            }
        };

        remover.addShapedRecipe(    Items.iron_ingot,       Items.iron_ingot,       Items.iron_ingot,
                                    Items.iron_ingot,       null,                   Items.iron_ingot,
                                    Items.iron_ingot,       null,                   null);

        remover.addVehicles(VehicleRegistry.CART);
        remover.addSides(ModuleSide.TOP, ModuleSide.BACK);
        register(remover);


        ModuleData height = new ModuleData("height_controller", ModuleHeightControl.class, 20);
        height.addShapedRecipe(     null,              Items.compass,       null,
                                    Items.paper,       SIMPLE_PCB,          Items.paper,
                                    Items.paper,       Items.paper,         Items.paper);

        height.addVehicles(VehicleRegistry.CART);
        height.addSides(ModuleSide.TOP, ModuleSide.BACK);
        register(height);
    }

}
