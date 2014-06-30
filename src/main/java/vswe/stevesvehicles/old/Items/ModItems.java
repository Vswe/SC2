package vswe.stevesvehicles.old.Items;


import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.config.Configuration;
import vswe.stevesvehicles.item.ItemVehicles;
import vswe.stevesvehicles.item.ItemVehicleModule;
import vswe.stevesvehicles.module.data.ModuleRegistry;
import vswe.stevesvehicles.old.Blocks.ModBlocks;
import vswe.stevesvehicles.old.Helpers.ComponentTypes;
import vswe.stevesvehicles.old.Helpers.DetectorType;
import vswe.stevesvehicles.old.Helpers.RecipeHelper;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.old.StevesVehicles;
import vswe.stevesvehicles.old.Upgrades.AssemblerUpgrade;

import java.util.HashMap;

public final class ModItems {

    public static ItemVehicles carts;
    public static ItemCartComponent component;
    public static ItemVehicleModule modules;
    public static ItemUpgrade upgrades;
    public static ItemBlockStorage storages;
    public static ItemBlockDetector detectors;


    private static final String CART_NAME = "ModularCart";
    private static final String COMPONENTS_NAME = "ModuleComponents";
    private static final String MODULES_NAME = "CartModule";

    private static HashMap<String,Boolean> validModules = new HashMap<String,Boolean>();

    public static void preBlockInit(Configuration config) {
        (carts = new ItemVehicles()).setUnlocalizedName(StevesVehicles.localStart + CART_NAME);
        component = new ItemCartComponent();
        modules = new ItemVehicleModule();

        GameRegistry.registerItem(carts, CART_NAME);
        GameRegistry.registerItem(component, COMPONENTS_NAME);
        GameRegistry.registerItem(modules, MODULES_NAME);


        //TODO this is being called before all modules have been added, needs to be figured out
        for (ModuleData module : ModuleRegistry.getAllModules()) {
            if (!module.getIsLocked()) {
                validModules.put(module.getFullRawUnlocalizedName(), config.get("EnabledModules", module.getName().replace(" ", "").replace(":","_"), module.getEnabledByDefault()).getBoolean(true));
            }
        }

        for (int i = 0; i < ItemCartComponent.size(); i++) {
            ItemStack subcomponent = new ItemStack(component,1,i);
            GameRegistry.registerCustomItemStack(subcomponent.getUnlocalizedName(), subcomponent);
        }

    }

    public static void postBlockInit(Configuration config) {
        detectors = (ItemBlockDetector) new ItemStack(ModBlocks.DETECTOR_UNIT.getBlock()).getItem();
        upgrades = (ItemUpgrade) new ItemStack(ModBlocks.UPGRADE.getBlock()).getItem();
        storages = (ItemBlockStorage) new ItemStack(ModBlocks.STORAGE.getBlock()).getItem();



        for (int i = 0; i < ItemBlockStorage.blocks.length; i++) {
            ItemStack storage = new ItemStack(storages, 1, i);
            GameRegistry.registerCustomItemStack(storage.getUnlocalizedName(), storage);
        }

        for (AssemblerUpgrade upgrade : AssemblerUpgrade.getUpgradesList()) {
            ItemStack upgradeStack = new ItemStack(upgrades, 1, upgrade.getId());
            GameRegistry.registerCustomItemStack(upgradeStack.getUnlocalizedName(), upgradeStack);
        }

        for (DetectorType type : DetectorType.values()) {
            ItemStack stack = new ItemStack(detectors, 1, type.getMeta());
            GameRegistry.registerCustomItemStack(stack.getUnlocalizedName(), stack);
        }
    }


    public static void addRecipes() {
        for (ModuleData module : ModuleRegistry.getAllModules()) {
            if (!module.getIsLocked()) { //&& validModules.get(module.getFullRawUnlocalizedName())) { //TODO see todo further up
                module.loadRecipes();
            }
        }


        String planks = "plankWood"; //ore dict
        String wood = "logWood"; //ore dict
        String red = "dyeRed";
        String green = "dyeGreen";
        String blue = "dyeBlue";
        String orange = "dyeOrange";
        String yellow = "dyeYellow";


        //wooden wheels
        RecipeHelper.addRecipe(ComponentTypes.WOODEN_WHEELS.getItemStack(), new Object[][]{
                {null, Items.stick, null},
                {Items.stick, planks, Items.stick},
                {null, Items.stick, null}
        });

        //iron wheels
        RecipeHelper.addRecipe(ComponentTypes.IRON_WHEELS.getItemStack(), new Object[][] {
                {null, Items.stick, null},
                {Items.stick, Items.iron_ingot, Items.stick},
                {null, Items.stick, null}
        });




        //color component (RED)
        RecipeHelper.addRecipe(ComponentTypes.RED_PIGMENT.getItemStack(), new Object[][] {
                {null, Items.glowstone_dust, null},
                {red, red, red},
                {null, Items.glowstone_dust, null}
        });

        //color component (GREEN)
        RecipeHelper.addRecipe(ComponentTypes.GREEN_PIGMENT.getItemStack(), new Object[][] {
                {null, Items.glowstone_dust, null},
                {green, green, green},
                {null, Items.glowstone_dust, null}
        });

        //color component (BLUE)
        RecipeHelper.addRecipe(ComponentTypes.BLUE_PIGMENT.getItemStack(), new Object[][] {
                {null, Items.glowstone_dust, null},
                {blue, blue, blue},
                {null, Items.glowstone_dust, null}
        });


        //magic glass
        RecipeHelper.addRecipe(ComponentTypes.GLASS_O_MAGIC.getItemStack(), new Object[][] {
                {Blocks.glass_pane, Items.fermented_spider_eye, Blocks.glass_pane},
                {Blocks.glass_pane, Items.redstone, Blocks.glass_pane},
                {Blocks.glass_pane, Blocks.glass_pane, Blocks.glass_pane}
        });


        //fuse
        RecipeHelper.addRecipe(ComponentTypes.FUSE.getItemStack(12), new Object[][] {
                {Items.string},
                {Items.string},
                {Items.string}
        });

        //dynamite
        RecipeHelper.addRecipe(ComponentTypes.DYNAMITE.getItemStack(), new Object[][] {
                {ComponentTypes.FUSE.getItemStack()},
                {Items.gunpowder},
                {Items.gunpowder}
        });





        //pcb
        RecipeHelper.addRecipe(ComponentTypes.SIMPLE_PCB.getItemStack(), new Object[][] {
                {Items.iron_ingot,Items.redstone,Items.iron_ingot},
                {Items.redstone,Items.gold_ingot,Items.redstone},
                {Items.iron_ingot,Items.redstone,Items.iron_ingot}
        });
        RecipeHelper.addRecipe(ComponentTypes.SIMPLE_PCB.getItemStack(), new Object[][] {
                {Items.redstone,Items.iron_ingot,Items.redstone},
                {Items.iron_ingot,Items.gold_ingot,Items.iron_ingot},
                {Items.redstone,Items.iron_ingot,Items.redstone}
        });

        //screen
        RecipeHelper.addRecipe(ComponentTypes.GRAPHICAL_INTERFACE.getItemStack(), new Object[][] {
                {Items.gold_ingot,Items.diamond,Items.gold_ingot},
                {Blocks.glass_pane,ComponentTypes.SIMPLE_PCB.getItemStack(),Blocks.glass_pane},
                {Items.redstone,Blocks.glass_pane,Items.redstone}
        });

        //unrefined handle
        RecipeHelper.addRecipe(ComponentTypes.RAW_HANDLE.getItemStack(),new Object[][] {{null, null, Items.iron_ingot},
                {null, Items.iron_ingot, null},
                {Items.iron_ingot, null, null}
        });

        FurnaceRecipes.smelting().func_151394_a(ComponentTypes.RAW_HANDLE.getItemStack(), ComponentTypes.REFINED_HANDLE.getItemStack(), 0F);

        //speed handle
        RecipeHelper.addRecipe(ComponentTypes.SPEED_HANDLE.getItemStack(),new Object[][] {{null, null, blue},
                {Items.gold_ingot, ComponentTypes.REFINED_HANDLE.getItemStack(), null},
                {Items.redstone, Items.gold_ingot, null}
        });

        //wheel
        RecipeHelper.addRecipe(ComponentTypes.WHEEL.getItemStack(),new Object[][] {
                {Items.iron_ingot,Items.stick,Items.iron_ingot},
                {Items.stick,Items.iron_ingot,Items.stick},
                {null,Items.stick,null}
        });

        //saw head
        RecipeHelper.addRecipe(ComponentTypes.SAW_BLADE.getItemStack(),new Object[][] {
                {Items.iron_ingot,Items.iron_ingot,Items.diamond}
        });



        //advanced pcb
        RecipeHelper.addRecipe(ComponentTypes.ADVANCED_PCB.getItemStack(),new Object[][] {
                {Items.redstone,Items.iron_ingot,Items.redstone},
                {ComponentTypes.SIMPLE_PCB.getItemStack(),Items.iron_ingot,ComponentTypes.SIMPLE_PCB.getItemStack()},
                {Items.redstone,Items.iron_ingot,Items.redstone},
        });

        //wood cutter
        RecipeHelper.addRecipe(ComponentTypes.WOOD_CUTTING_CORE.getItemStack(),new Object[][] {
                {"treeSapling","treeSapling","treeSapling"},
                {"treeSapling",ComponentTypes.ADVANCED_PCB.getItemStack(),"treeSapling"},
                {"treeSapling","treeSapling","treeSapling"}
        });




        RecipeHelper.addRecipe(ComponentTypes.RAW_HARDENER.getItemStack(2),new Object[][] {
                {Blocks.obsidian,null,Blocks.obsidian},
                {null,Items.diamond,null},
                {Blocks.obsidian,null,Blocks.obsidian}
        });


        FurnaceRecipes.smelting().func_151394_a(ComponentTypes.RAW_HARDENER.getItemStack(), ComponentTypes.REFINED_HARDENER.getItemStack(), 0F);



        //hardened mesh
        RecipeHelper.addRecipe(ComponentTypes.HARDENED_MESH.getItemStack(),new Object[][] {
                {Blocks.iron_bars,ComponentTypes.REFINED_HARDENER.getItemStack(),Blocks.iron_bars},
                {ComponentTypes.REFINED_HARDENER.getItemStack(),Blocks.iron_bars,ComponentTypes.REFINED_HARDENER.getItemStack()},
                {Blocks.iron_bars,ComponentTypes.REFINED_HARDENER.getItemStack(),Blocks.iron_bars}
        });



        //hardened metal
        RecipeHelper.addRecipe(ComponentTypes.STABILIZED_METAL.getItemStack(5),new Object[][] {
                {Items.iron_ingot,ComponentTypes.HARDENED_MESH.getItemStack(),Items.iron_ingot},
                {Items.iron_ingot,Items.iron_ingot,Items.iron_ingot},
                {ComponentTypes.REFINED_HARDENER.getItemStack(),ComponentTypes.REFINED_HARDENER.getItemStack(),ComponentTypes.REFINED_HARDENER.getItemStack()}
        });

        FurnaceRecipes.smelting().func_151394_a(ComponentTypes.STABILIZED_METAL.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack(), 0F);




        //hardened wheels
        RecipeHelper.addRecipe(ComponentTypes.REINFORCED_WHEELS.getItemStack(),new Object[][] {
                {null,Items.iron_ingot,null},
                {Items.iron_ingot,ComponentTypes.REINFORCED_METAL.getItemStack(),Items.iron_ingot},
                {null,Items.iron_ingot,null}
        });




        //pipe
        RecipeHelper.addRecipe(ComponentTypes.PIPE.getItemStack(), new Object[][] {
                {Blocks.stone,Blocks.stone,Blocks.stone},
                {Items.iron_ingot,null,null}
        });

        //shooting core
        RecipeHelper.addRecipe(ComponentTypes.SHOOTING_STATION.getItemStack(), new Object[][] {
                {Items.redstone,null,Items.redstone},
                {Items.redstone,Items.gold_ingot,Items.redstone},
                {Blocks.dispenser,ComponentTypes.SIMPLE_PCB.getItemStack(),Blocks.dispenser}
        });

        //mob scanner
        RecipeHelper.addRecipe(ComponentTypes.ENTITY_SCANNER.getItemStack(), new Object[][] {
                {Items.gold_ingot,ComponentTypes.SIMPLE_PCB.getItemStack(),Items.gold_ingot},
                {Items.redstone,ComponentTypes.ADVANCED_PCB.getItemStack(),Items.redstone},
                {Items.redstone,null,Items.redstone}
        });


        //entity analyzer
        RecipeHelper.addRecipe(ComponentTypes.ENTITY_ANALYZER.getItemStack(), new Object[][] {
                {Items.iron_ingot,Items.redstone,Items.iron_ingot},
                {Items.iron_ingot,ComponentTypes.SIMPLE_PCB.getItemStack(),Items.iron_ingot},
                {Items.iron_ingot,Items.iron_ingot,Items.iron_ingot}
        });

        //empty entity disk
        RecipeHelper.addRecipe(ComponentTypes.EMPTY_DISK.getItemStack(), new Object[][] {
                {Items.redstone},
                {ComponentTypes.SIMPLE_PCB.getItemStack()}
        });



        //tri-torch
        RecipeHelper.addRecipe(ComponentTypes.TRI_TORCH.getItemStack(), new Object[][] {
                {Blocks.torch, Blocks.torch, Blocks.torch}
        });







        //chest thingy
        RecipeHelper.addRecipe(ComponentTypes.CHEST_PANE.getItemStack(32), new Object[][] {
                {planks, planks, planks},
                {wood, planks, wood},
                {planks, planks, planks}
        });


        //big chest thingy
        RecipeHelper.addRecipe(ComponentTypes.LARGE_CHEST_PANE.getItemStack(), new Object[][] {
                {ComponentTypes.CHEST_PANE.getItemStack(), ComponentTypes.CHEST_PANE.getItemStack()},
                {ComponentTypes.CHEST_PANE.getItemStack(), ComponentTypes.CHEST_PANE.getItemStack()}
        });

        //large chest thingy
        RecipeHelper.addRecipe(ComponentTypes.HUGE_CHEST_PANE.getItemStack(), new Object[][] {
                {ComponentTypes.CHEST_PANE.getItemStack(),ComponentTypes.CHEST_PANE.getItemStack(),ComponentTypes.CHEST_PANE.getItemStack()},
                {ComponentTypes.CHEST_PANE.getItemStack(),ComponentTypes.CHEST_PANE.getItemStack(),ComponentTypes.CHEST_PANE.getItemStack()},
                {ComponentTypes.CHEST_PANE.getItemStack(),ComponentTypes.CHEST_PANE.getItemStack(),ComponentTypes.CHEST_PANE.getItemStack()}
        });

        //large chest thingy
        RecipeHelper.addRecipe(ComponentTypes.CHEST_LOCK.getItemStack(8), new Object[][] {
                {Items.iron_ingot},
                {Blocks.stone}
        });
        RecipeHelper.addRecipe(ComponentTypes.CHEST_LOCK.getItemStack(8), new Object[][] {
                {Blocks.stone},
                {Items.iron_ingot}
        });

        //iron chest thingy
        RecipeHelper.addRecipe(ComponentTypes.IRON_PANE.getItemStack(8), new Object[][] {
                {ComponentTypes.CHEST_PANE.getItemStack(),ComponentTypes.CHEST_PANE.getItemStack(),ComponentTypes.CHEST_PANE.getItemStack()},
                {ComponentTypes.CHEST_PANE.getItemStack(),Items.iron_ingot,ComponentTypes.CHEST_PANE.getItemStack()},
                {ComponentTypes.CHEST_PANE.getItemStack(),ComponentTypes.CHEST_PANE.getItemStack(),ComponentTypes.CHEST_PANE.getItemStack()}
        });

        //big iron chest thingy
        RecipeHelper.addRecipe(ComponentTypes.LARGE_IRON_PANE.getItemStack(), new Object[][] {
                {ComponentTypes.IRON_PANE.getItemStack(), ComponentTypes.IRON_PANE.getItemStack()},
                {ComponentTypes.IRON_PANE.getItemStack(), ComponentTypes.IRON_PANE.getItemStack()}
        });

        //large iron chest thingy
        RecipeHelper.addRecipe(ComponentTypes.HUGE_IRON_PANE.getItemStack(), new Object[][] {
                {ComponentTypes.IRON_PANE.getItemStack(),ComponentTypes.IRON_PANE.getItemStack(),ComponentTypes.IRON_PANE.getItemStack()},
                {ComponentTypes.IRON_PANE.getItemStack(),ComponentTypes.IRON_PANE.getItemStack(),ComponentTypes.IRON_PANE.getItemStack()},
                {ComponentTypes.IRON_PANE.getItemStack(),ComponentTypes.IRON_PANE.getItemStack(),ComponentTypes.IRON_PANE.getItemStack()}
        });

        //dynamic chest thingy
        RecipeHelper.addRecipe(ComponentTypes.DYNAMIC_PANE.getItemStack(), new Object[][] {
                {ComponentTypes.IRON_PANE.getItemStack()},
                {Items.redstone}
        });
        RecipeHelper.addRecipe(ComponentTypes.DYNAMIC_PANE.getItemStack(), new Object[][] {
                {Items.redstone},
                {ComponentTypes.IRON_PANE.getItemStack()}
        });

        //big dynamic chest thingy
        RecipeHelper.addRecipe(ComponentTypes.LARGE_DYNAMIC_PANE.getItemStack(), new Object[][] {
                {null,ComponentTypes.DYNAMIC_PANE.getItemStack(), null},
                {ComponentTypes.DYNAMIC_PANE.getItemStack(),Items.redstone, ComponentTypes.DYNAMIC_PANE.getItemStack()},
                {null,ComponentTypes.DYNAMIC_PANE.getItemStack(), null}
        });

        //large dynamic chest thingy
        RecipeHelper.addRecipe(ComponentTypes.HUGE_DYNAMIC_PANE.getItemStack(), new Object[][] {
                {ComponentTypes.DYNAMIC_PANE.getItemStack(),ComponentTypes.DYNAMIC_PANE.getItemStack(),ComponentTypes.DYNAMIC_PANE.getItemStack()},
                {ComponentTypes.DYNAMIC_PANE.getItemStack(),ComponentTypes.SIMPLE_PCB.getItemStack(),ComponentTypes.DYNAMIC_PANE.getItemStack()},
                {ComponentTypes.DYNAMIC_PANE.getItemStack(),ComponentTypes.DYNAMIC_PANE.getItemStack(),ComponentTypes.DYNAMIC_PANE.getItemStack()}
        });




        //cleaning fan
        RecipeHelper.addRecipe(ComponentTypes.CLEANING_FAN.getItemStack(), new Object[][] {
                {Blocks.iron_bars,Items.redstone,Blocks.iron_bars},
                {Items.redstone,null,Items.redstone},
                {Blocks.iron_bars,Items.redstone,Blocks.iron_bars}
        });


        //cleaning core
        RecipeHelper.addRecipe(ComponentTypes.CLEANING_CORE.getItemStack(), new Object[][] {
                {ComponentTypes.CLEANING_FAN.getItemStack(),Items.iron_ingot,ComponentTypes.CLEANING_FAN.getItemStack()},
                {ComponentTypes.CLEANING_TUBE.getItemStack(),ComponentTypes.CLEANING_TUBE.getItemStack(),ComponentTypes.CLEANING_TUBE.getItemStack()},
                {Items.iron_ingot,ComponentTypes.CLEANING_TUBE.getItemStack(),Items.iron_ingot}
        });

        //cleaning tube
        RecipeHelper.addRecipe(ComponentTypes.CLEANING_TUBE.getItemStack(2), new Object[][] {
                {orange,Items.iron_ingot,orange},
                {orange,Items.iron_ingot,orange},
                {orange,Items.iron_ingot,orange}
        });

        //solar panel

        RecipeHelper.addRecipe(ComponentTypes.SOLAR_PANEL.getItemStack(),new Object[][] {
                {Items.glowstone_dust,Items.redstone},
                {Items.iron_ingot,Items.glowstone_dust}
        });


        //magic thingy
        RecipeHelper.addRecipe(ComponentTypes.EYE_OF_GALGADOR.getItemStack(),new Object[][] {
                {Items.magma_cream,Items.fermented_spider_eye, Items.magma_cream},
                {Items.ghast_tear, Items.ender_eye,Items.ghast_tear},
                {Items.magma_cream,Items.fermented_spider_eye, Items.magma_cream}
        });




        //magic metal
        RecipeHelper.addRecipe(ComponentTypes.LUMP_OF_GALGADOR.getItemStack(2),new Object[][] {
                {Items.glowstone_dust,Blocks.diamond_block, Items.glowstone_dust},
                {ComponentTypes.EYE_OF_GALGADOR.getItemStack(), Items.glowstone_dust,ComponentTypes.EYE_OF_GALGADOR.getItemStack()},
                {ComponentTypes.STABILIZED_METAL.getItemStack(),ComponentTypes.EYE_OF_GALGADOR.getItemStack(),ComponentTypes.STABILIZED_METAL.getItemStack()}
        });

        FurnaceRecipes.smelting().func_151394_a(ComponentTypes.LUMP_OF_GALGADOR.getItemStack(), ComponentTypes.GALGADORIAN_METAL.getItemStack(), 0F);




        RecipeHelper.addRecipe(ComponentTypes.LARGE_LUMP_OF_GALGADOR.getItemStack(),new Object[][] {
                {ComponentTypes.LUMP_OF_GALGADOR.getItemStack(),ComponentTypes.LUMP_OF_GALGADOR.getItemStack(),ComponentTypes.LUMP_OF_GALGADOR.getItemStack()},
                {ComponentTypes.LUMP_OF_GALGADOR.getItemStack(),ComponentTypes.LUMP_OF_GALGADOR.getItemStack(),ComponentTypes.LUMP_OF_GALGADOR.getItemStack()},
                {ComponentTypes.LUMP_OF_GALGADOR.getItemStack(),ComponentTypes.LUMP_OF_GALGADOR.getItemStack(),ComponentTypes.LUMP_OF_GALGADOR.getItemStack()}
        });

        FurnaceRecipes.smelting().func_151394_a(ComponentTypes.LARGE_LUMP_OF_GALGADOR.getItemStack(), ComponentTypes.ENHANCED_GALGADORIAN_METAL.getItemStack(), 0F);




        RecipeHelper.addRecipe(ComponentTypes.RED_GIFT_RIBBON.getItemStack(),new Object[][] {
                {Items.string,Items.string,Items.string},
                {Items.string,red,Items.string},
                {Items.string,Items.string,Items.string}
        });

        RecipeHelper.addRecipe(ComponentTypes.YELLOW_GIFT_RIBBON.getItemStack(),new Object[][] {
                {Items.string,Items.string,Items.string},
                {Items.string,yellow,Items.string},
                {Items.string,Items.string,Items.string}
        });

        RecipeHelper.addRecipe(ComponentTypes.WARM_HAT.getItemStack(),new Object[][] {
                {null,new ItemStack(Blocks.wool,1,14),new ItemStack(Blocks.wool,1,0)},
                {new ItemStack(Blocks.wool,1,14),Items.diamond,new ItemStack(Blocks.wool,1,14)},
                {new ItemStack(Blocks.wool,1,14),new ItemStack(Blocks.wool,1,14),new ItemStack(Blocks.wool,1,14)}
        });

        RecipeHelper.addRecipe(ComponentTypes.SOCK.getItemStack(),new Object[][] {
                {new ItemStack(Blocks.wool,1,14),new ItemStack(Blocks.wool,1,14),Items.cookie},
                {new ItemStack(Blocks.wool,1,14),new ItemStack(Blocks.wool,1,14),Items.milk_bucket},
                {new ItemStack(Blocks.wool,1,14),new ItemStack(Blocks.wool,1,14),new ItemStack(Blocks.wool,1,14)}
        });

        //advanced panel
        RecipeHelper.addRecipe(ComponentTypes.ADVANCED_SOLAR_PANEL.getItemStack(),new Object[][] {
                {ComponentTypes.SOLAR_PANEL.getItemStack(),null,ComponentTypes.SOLAR_PANEL.getItemStack()},
                {Items.iron_ingot, ComponentTypes.SIMPLE_PCB.getItemStack(), Items.iron_ingot},
                {ComponentTypes.SOLAR_PANEL.getItemStack(),null,ComponentTypes.SOLAR_PANEL.getItemStack()}
        });
        RecipeHelper.addRecipe(ComponentTypes.ADVANCED_SOLAR_PANEL.getItemStack(),new Object[][] {
                {ComponentTypes.SOLAR_PANEL.getItemStack(),Items.iron_ingot,ComponentTypes.SOLAR_PANEL.getItemStack()},
                {null, ComponentTypes.SIMPLE_PCB.getItemStack(), null},
                {ComponentTypes.SOLAR_PANEL.getItemStack(),Items.iron_ingot,ComponentTypes.SOLAR_PANEL.getItemStack()}
        });


        //blank upgrade

        RecipeHelper.addRecipe(ComponentTypes.BLANK_UPGRADE.getItemStack(2),new Object[][] {
                {Items.iron_ingot,Items.iron_ingot,Items.iron_ingot},
                {ComponentTypes.REINFORCED_METAL.getItemStack(), Items.redstone, ComponentTypes.REINFORCED_METAL.getItemStack()},
                {Items.iron_ingot,ComponentTypes.ADVANCED_PCB.getItemStack(),Items.iron_ingot}
        });




        //valve
        RecipeHelper.addRecipe(ComponentTypes.TANK_VALVE.getItemStack(8), new Object[][] {
                {null, Items.iron_ingot, null},
                {Items.iron_ingot, Blocks.iron_bars, Items.iron_ingot},
                {null, Items.iron_ingot, null}
        });

        //tank pane
        RecipeHelper.addRecipe(ComponentTypes.TANK_PANE.getItemStack(32), new Object[][] {
                {Blocks.glass_pane,  Blocks.glass_pane, Blocks.glass_pane},
                {Blocks.glass, Blocks.glass_pane, Blocks.glass},
                {Blocks.glass_pane, Blocks.glass_pane, Blocks.glass_pane}
        });

        //large tank pane
        RecipeHelper.addRecipe(ComponentTypes.LARGE_TANK_PANE.getItemStack(), new Object[][] {
                {ComponentTypes.TANK_PANE.getItemStack(),  ComponentTypes.TANK_PANE.getItemStack()},
                {ComponentTypes.TANK_PANE.getItemStack(), ComponentTypes.TANK_PANE.getItemStack()}
        });

        //huge tank pane
        RecipeHelper.addRecipe(ComponentTypes.HUGE_TANK_PANE.getItemStack(), new Object[][] {
                {ComponentTypes.TANK_PANE.getItemStack(),  ComponentTypes.TANK_PANE.getItemStack(), ComponentTypes.TANK_PANE.getItemStack()},
                {ComponentTypes.TANK_PANE.getItemStack(), ComponentTypes.TANK_PANE.getItemStack(), ComponentTypes.TANK_PANE.getItemStack()},
                {ComponentTypes.TANK_PANE.getItemStack(), ComponentTypes.TANK_PANE.getItemStack(), ComponentTypes.TANK_PANE.getItemStack()}
        });



        //cleaning core
        RecipeHelper.addRecipe(ComponentTypes.LIQUID_CLEANING_CORE.getItemStack(), new Object[][] {
                {ComponentTypes.CLEANING_FAN.getItemStack(),Items.iron_ingot,ComponentTypes.CLEANING_FAN.getItemStack()},
                {ComponentTypes.LIQUID_CLEANING_TUBE.getItemStack(),ComponentTypes.LIQUID_CLEANING_TUBE.getItemStack(),ComponentTypes.LIQUID_CLEANING_TUBE.getItemStack()},
                {Items.iron_ingot,ComponentTypes.LIQUID_CLEANING_TUBE.getItemStack(),Items.iron_ingot}
        });

        //cleaning tube
        RecipeHelper.addRecipe(ComponentTypes.LIQUID_CLEANING_TUBE.getItemStack(2), new Object[][] {
                {green,Items.iron_ingot,green},
                {green,Items.iron_ingot,green},
                {green,Items.iron_ingot,green}
        });


        //Easter Eggs, really, Eggs you paint at Easter. Those kind of Easter Eggs.

        RecipeHelper.addRecipe(ComponentTypes.EXPLOSIVE_EASTER_EGG.getItemStack(), new Object[][] {
                {Items.gunpowder,Items.gunpowder,Items.gunpowder},
                {Items.gunpowder,Items.egg,Items.gunpowder},
                {Items.gunpowder,green,Items.gunpowder}
        });

        RecipeHelper.addRecipe(ComponentTypes.BURNING_EASTER_EGG.getItemStack(), new Object[][] {
                {Items.blaze_powder,Items.blaze_rod,Items.blaze_powder},
                {Items.blaze_powder,Items.egg,Items.blaze_powder},
                {red,Items.magma_cream,yellow}
        });

        RecipeHelper.addRecipe(ComponentTypes.GLISTERING_EASTER_EGG.getItemStack(), new Object[][] {
                {Items.gold_nugget,Items.gold_nugget,Items.gold_nugget},
                {Items.gold_nugget,Items.egg,Items.gold_nugget},
                {Items.gold_nugget,blue,Items.gold_nugget}
        });

        ItemStack chocolate = new ItemStack(Items.dye, 1, 3);

        RecipeHelper.addRecipe(ComponentTypes.CHOCOLATE_EASTER_EGG.getItemStack(), new Object[][] {
                {chocolate, Items.sugar, chocolate},
                {chocolate,Items.egg,chocolate},
                {chocolate, Items.sugar, chocolate}
        });

        RecipeHelper.addRecipe(ComponentTypes.BASKET.getItemStack(), new Object[][] {
                {Items.stick, Items.stick, Items.stick},
                {Items.stick,null,Items.stick},
                {planks, planks, planks}
        });


        //wood
        for (int i = 0; i < 4; i++) {
            RecipeHelper.addRecipe(new ItemStack(Blocks.planks, 2, i), new Object[][] {
                    {ItemCartComponent.getWood(i, true)}
            });

            RecipeHelper.addRecipe(new ItemStack(Items.stick, 2), new Object[][] {
                    {ItemCartComponent.getWood(i, false)}
            });
        }


        //higher tier saw heads
        RecipeHelper.addRecipe(ComponentTypes.HARDENED_SAW_BLADE.getItemStack(),new Object[][] {
                {Items.iron_ingot,Items.iron_ingot, ComponentTypes.REINFORCED_METAL.getItemStack()}
        });
        RecipeHelper.addRecipe(ComponentTypes.GALGADORIAN_SAW_BLADE.getItemStack(),new Object[][] {
                {Items.iron_ingot,Items.iron_ingot, ComponentTypes.GALGADORIAN_METAL.getItemStack()}
        });

        //galgadorian wheels
        RecipeHelper.addRecipe(ComponentTypes.GALGADORIAN_WHEELS.getItemStack(),new Object[][] {
                {null, ComponentTypes.REINFORCED_METAL.getItemStack(), null},
                {ComponentTypes.REINFORCED_METAL.getItemStack(), ComponentTypes.GALGADORIAN_METAL.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack()},
                {null, ComponentTypes.REINFORCED_METAL.getItemStack(), null}
        });




        //iron blade
        RecipeHelper.addRecipe(ComponentTypes.IRON_BLADE.getItemStack(4),new Object[][] {
                {null, Items.shears, null},
                {Items.iron_ingot, Items.iron_ingot, Items.iron_ingot},
                {null, Items.iron_ingot, null}
        });


        //blade arm
        RecipeHelper.addRecipe(ComponentTypes.BLADE_ARM.getItemStack(),new Object[][] {
                {ComponentTypes.IRON_BLADE.getItemStack(), null, ComponentTypes.IRON_BLADE.getItemStack()},
                {null, Items.iron_ingot, null},
                {ComponentTypes.IRON_BLADE.getItemStack(), null, ComponentTypes.IRON_BLADE.getItemStack()}
        });



        //storage blocks
        ItemBlockStorage.loadRecipes();
    }


    private ModItems() {}
}
