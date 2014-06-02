package vswe.stevescarts.Items;


import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.config.Configuration;
import vswe.stevescarts.Blocks.Blocks;
import vswe.stevescarts.Helpers.ComponentTypes;
import vswe.stevescarts.Helpers.DetectorType;
import vswe.stevescarts.Helpers.GiftItem;
import vswe.stevescarts.Helpers.RecipeHelper;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Upgrades.AssemblerUpgrade;

import java.util.HashMap;

public final class Items {

    public static ItemCarts carts;
    public static ItemCartComponent component;
    public static ItemCartModule modules;
    public static ItemUpgrade upgrades;
    public static ItemBlockStorage storages;
    public static ItemBlockDetector detectors;


    private static final String CART_NAME = "ModularCart";
    private static final String COMPONENTS_NAME = "ModuleComponents";
    private static final String MODULES_NAME = "CartModule";

    private static final int CART_DEFAULT_ID = 29743;
    private static final int COMPONENTS_DEFAULT_ID = 29742;
    private static final int MODULES_DEFAULT_ID = 29741;

    private static HashMap<Byte,Boolean> validModules = new HashMap<Byte,Boolean>();

    public static void preBlockInit(Configuration config) {
        (carts = new ItemCarts(config.getItem(CART_NAME, CART_DEFAULT_ID).getInt(CART_DEFAULT_ID))).setUnlocalizedName(StevesCarts.localStart + CART_NAME);
        component = new ItemCartComponent(config.getItem(COMPONENTS_NAME, COMPONENTS_DEFAULT_ID).getInt(COMPONENTS_DEFAULT_ID));
        modules = new ItemCartModule(config.getItem(MODULES_NAME, MODULES_DEFAULT_ID).getInt(MODULES_DEFAULT_ID));

        GameRegistry.registerItem(carts, CART_NAME);
        GameRegistry.registerItem(component, COMPONENTS_NAME);
        GameRegistry.registerItem(modules, MODULES_NAME);

        ModuleData.init();

        for (ModuleData module : ModuleData.getList().values()) {
            if (!module.getIsLocked()) {
                validModules.put(module.getID(), config.get("EnabledModules", module.getName().replace(" ", "").replace(":","_"), module.getEnabledByDefault()).getBoolean(true));
            }
        }

        for (int i = 0; i < ItemCartComponent.size(); i++) {
            ItemStack subcomponent = new ItemStack(component,1,i);
            GameRegistry.registerCustomItemStack(subcomponent.getUnlocalizedName(), subcomponent);
        }

        for (ModuleData module : ModuleData.getList().values()) {
            ItemStack submodule = new ItemStack(modules,1,module.getID());
            GameRegistry.registerCustomItemStack(submodule.getUnlocalizedName(), submodule);
        }
    }

    public static void postBlockInit(Configuration config) {
        detectors = (ItemBlockDetector) Item.itemsList[Blocks.DETECTOR_UNIT.getId()];
        upgrades = (ItemUpgrade)Item.itemsList[Blocks.UPGRADE.getId()];
        storages = (ItemBlockStorage)Item.itemsList[Blocks.STORAGE.getId()];



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
        for (ModuleData module : ModuleData.getList().values()) {
            ItemStack submodule = new ItemStack(modules,1,module.getID());

            if (!module.getIsLocked() && validModules.get(module.getID())) {
                module.loadRecipe();
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
                {null, Item.stick, null},
                {Item.stick, planks, Item.stick},
                {null, Item.stick, null}
        });

        //iron wheels
        RecipeHelper.addRecipe(ComponentTypes.IRON_WHEELS.getItemStack(), new Object[][] {
                {null, Item.stick, null},
                {Item.stick, Item.ingotIron, Item.stick},
                {null, Item.stick, null}
        });




        //color component (RED)
        RecipeHelper.addRecipe(ComponentTypes.RED_PIGMENT.getItemStack(), new Object[][] {
                {null, Item.glowstone, null},
                {red, red, red},
                {null, Item.glowstone, null}
        });

        //color component (GREEN)
        RecipeHelper.addRecipe(ComponentTypes.GREEN_PIGMENT.getItemStack(), new Object[][] {
                {null, Item.glowstone, null},
                {green, green, green},
                {null, Item.glowstone, null}
        });

        //color component (BLUE)
        RecipeHelper.addRecipe(ComponentTypes.BLUE_PIGMENT.getItemStack(), new Object[][] {
                {null, Item.glowstone, null},
                {blue, blue, blue},
                {null, Item.glowstone, null}
        });


        //magic glass
        RecipeHelper.addRecipe(ComponentTypes.GLASS_O_MAGIC.getItemStack(), new Object[][] {
                {Block.thinGlass, Item.fermentedSpiderEye, Block.thinGlass},
                {Block.thinGlass, Item.redstone, Block.thinGlass},
                {Block.thinGlass, Block.thinGlass, Block.thinGlass}
        });


        //fuse
        RecipeHelper.addRecipe(ComponentTypes.FUSE.getItemStack(12), new Object[][] {
                {Item.silk},
                {Item.silk},
                {Item.silk}
        });

        //dynamite
        RecipeHelper.addRecipe(ComponentTypes.DYNAMITE.getItemStack(), new Object[][] {
                {ComponentTypes.FUSE.getItemStack()},
                {Item.gunpowder},
                {Item.gunpowder}
        });





        //pcb
        RecipeHelper.addRecipe(ComponentTypes.SIMPLE_PCB.getItemStack(), new Object[][] {
                {Item.ingotIron,Item.redstone,Item.ingotIron},
                {Item.redstone,Item.ingotGold,Item.redstone},
                {Item.ingotIron,Item.redstone,Item.ingotIron}
        });
        RecipeHelper.addRecipe(ComponentTypes.SIMPLE_PCB.getItemStack(), new Object[][] {
                {Item.redstone,Item.ingotIron,Item.redstone},
                {Item.ingotIron,Item.ingotGold,Item.ingotIron},
                {Item.redstone,Item.ingotIron,Item.redstone}
        });

        //screen
        RecipeHelper.addRecipe(ComponentTypes.GRAPHICAL_INTERFACE.getItemStack(), new Object[][] {
                {Item.ingotGold,Item.diamond,Item.ingotGold},
                {Block.thinGlass,ComponentTypes.SIMPLE_PCB.getItemStack(),Block.thinGlass},
                {Item.redstone,Block.thinGlass,Item.redstone}
        });

        //unrefined handle
        RecipeHelper.addRecipe(ComponentTypes.RAW_HANDLE.getItemStack(),new Object[][] {{null, null, Item.ingotIron},
                {null, Item.ingotIron, null},
                {Item.ingotIron, null, null}
        });

        FurnaceRecipes.smelting().addSmelting(component.itemID, ComponentTypes.RAW_HANDLE.getId(), ComponentTypes.REFINED_HANDLE.getItemStack(), 0F);

        //speed handle
        RecipeHelper.addRecipe(ComponentTypes.SPEED_HANDLE.getItemStack(),new Object[][] {{null, null, blue},
                {Item.ingotGold, ComponentTypes.REFINED_HANDLE.getItemStack(), null},
                {Item.redstone, Item.ingotGold, null}
        });

        //wheel
        RecipeHelper.addRecipe(ComponentTypes.WHEEL.getItemStack(),new Object[][] {
                {Item.ingotIron,Item.stick,Item.ingotIron},
                {Item.stick,Item.ingotIron,Item.stick},
                {null,Item.stick,null}
        });

        //saw head
        RecipeHelper.addRecipe(ComponentTypes.SAW_BLADE.getItemStack(),new Object[][] {
                {Item.ingotIron,Item.ingotIron,Item.diamond}
        });



        //advanced pcb
        RecipeHelper.addRecipe(ComponentTypes.ADVANCED_PCB.getItemStack(),new Object[][] {
                {Item.redstone,Item.ingotIron,Item.redstone},
                {ComponentTypes.SIMPLE_PCB.getItemStack(),Item.ingotIron,ComponentTypes.SIMPLE_PCB.getItemStack()},
                {Item.redstone,Item.ingotIron,Item.redstone},
        });

        //wood cutter
        RecipeHelper.addRecipe(ComponentTypes.WOOD_CUTTING_CORE.getItemStack(),new Object[][] {
                {"treeSapling","treeSapling","treeSapling"},
                {"treeSapling",ComponentTypes.ADVANCED_PCB.getItemStack(),"treeSapling"},
                {"treeSapling","treeSapling","treeSapling"}
        });




        RecipeHelper.addRecipe(ComponentTypes.RAW_HARDENER.getItemStack(2),new Object[][] {
                {Block.obsidian,null,Block.obsidian},
                {null,Item.diamond,null},
                {Block.obsidian,null,Block.obsidian}
        });


        FurnaceRecipes.smelting().addSmelting(component.itemID, ComponentTypes.RAW_HARDENER.getId(), ComponentTypes.REFINED_HARDENER.getItemStack(), 0F);



        //hardened mesh
        RecipeHelper.addRecipe(ComponentTypes.HARDENED_MESH.getItemStack(),new Object[][] {
                {Block.fenceIron,ComponentTypes.REFINED_HARDENER.getItemStack(),Block.fenceIron},
                {ComponentTypes.REFINED_HARDENER.getItemStack(),Block.fenceIron,ComponentTypes.REFINED_HARDENER.getItemStack()},
                {Block.fenceIron,ComponentTypes.REFINED_HARDENER.getItemStack(),Block.fenceIron}
        });



        //hardened metal
        RecipeHelper.addRecipe(ComponentTypes.STABILIZED_METAL.getItemStack(5),new Object[][] {
                {Item.ingotIron,ComponentTypes.HARDENED_MESH.getItemStack(),Item.ingotIron},
                {Item.ingotIron,Item.ingotIron,Item.ingotIron},
                {ComponentTypes.REFINED_HARDENER.getItemStack(),ComponentTypes.REFINED_HARDENER.getItemStack(),ComponentTypes.REFINED_HARDENER.getItemStack()}
        });

        FurnaceRecipes.smelting().addSmelting(component.itemID, ComponentTypes.STABILIZED_METAL.getId(), ComponentTypes.REINFORCED_METAL.getItemStack(), 0F);




        //hardened wheels
        RecipeHelper.addRecipe(ComponentTypes.REINFORCED_WHEELS.getItemStack(),new Object[][] {
                {null,Item.ingotIron,null},
                {Item.ingotIron,ComponentTypes.REINFORCED_METAL.getItemStack(),Item.ingotIron},
                {null,Item.ingotIron,null}
        });




        //pipe
        RecipeHelper.addRecipe(ComponentTypes.PIPE.getItemStack(), new Object[][] {
                {Block.stone,Block.stone,Block.stone},
                {Item.ingotIron,null,null}
        });

        //shooting core
        RecipeHelper.addRecipe(ComponentTypes.SHOOTING_STATION.getItemStack(), new Object[][] {
                {Item.redstone,null,Item.redstone},
                {Item.redstone,Item.ingotGold,Item.redstone},
                {Block.dispenser,ComponentTypes.SIMPLE_PCB.getItemStack(),Block.dispenser}
        });

        //mob scanner
        RecipeHelper.addRecipe(ComponentTypes.ENTITY_SCANNER.getItemStack(), new Object[][] {
                {Item.ingotGold,ComponentTypes.SIMPLE_PCB.getItemStack(),Item.ingotGold},
                {Item.redstone,ComponentTypes.ADVANCED_PCB.getItemStack(),Item.redstone},
                {Item.redstone,null,Item.redstone}
        });


        //entity analyzer
        RecipeHelper.addRecipe(ComponentTypes.ENTITY_ANALYZER.getItemStack(), new Object[][] {
                {Item.ingotIron,Item.redstone,Item.ingotIron},
                {Item.ingotIron,ComponentTypes.SIMPLE_PCB.getItemStack(),Item.ingotIron},
                {Item.ingotIron,Item.ingotIron,Item.ingotIron}
        });

        //empty entity disk
        RecipeHelper.addRecipe(ComponentTypes.EMPTY_DISK.getItemStack(), new Object[][] {
                {Item.redstone},
                {ComponentTypes.SIMPLE_PCB.getItemStack()}
        });



        //tri-torch
        RecipeHelper.addRecipe(ComponentTypes.TRI_TORCH.getItemStack(), new Object[][] {
                {Block.torchWood, Block.torchWood, Block.torchWood}
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
                {Item.ingotIron},
                {Block.stone}
        });
        RecipeHelper.addRecipe(ComponentTypes.CHEST_LOCK.getItemStack(8), new Object[][] {
                {Block.stone},
                {Item.ingotIron}
        });

        //iron chest thingy
        RecipeHelper.addRecipe(ComponentTypes.IRON_PANE.getItemStack(8), new Object[][] {
                {ComponentTypes.CHEST_PANE.getItemStack(),ComponentTypes.CHEST_PANE.getItemStack(),ComponentTypes.CHEST_PANE.getItemStack()},
                {ComponentTypes.CHEST_PANE.getItemStack(),Item.ingotIron,ComponentTypes.CHEST_PANE.getItemStack()},
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
                {Item.redstone}
        });
        RecipeHelper.addRecipe(ComponentTypes.DYNAMIC_PANE.getItemStack(), new Object[][] {
                {Item.redstone},
                {ComponentTypes.IRON_PANE.getItemStack()}
        });

        //big dynamic chest thingy
        RecipeHelper.addRecipe(ComponentTypes.LARGE_DYNAMIC_PANE.getItemStack(), new Object[][] {
                {null,ComponentTypes.DYNAMIC_PANE.getItemStack(), null},
                {ComponentTypes.DYNAMIC_PANE.getItemStack(),Item.redstone, ComponentTypes.DYNAMIC_PANE.getItemStack()},
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
                {Block.fenceIron,Item.redstone,Block.fenceIron},
                {Item.redstone,null,Item.redstone},
                {Block.fenceIron,Item.redstone,Block.fenceIron}
        });


        //cleaning core
        RecipeHelper.addRecipe(ComponentTypes.CLEANING_CORE.getItemStack(), new Object[][] {
                {ComponentTypes.CLEANING_FAN.getItemStack(),Item.ingotIron,ComponentTypes.CLEANING_FAN.getItemStack()},
                {ComponentTypes.CLEANING_TUBE.getItemStack(),ComponentTypes.CLEANING_TUBE.getItemStack(),ComponentTypes.CLEANING_TUBE.getItemStack()},
                {Item.ingotIron,ComponentTypes.CLEANING_TUBE.getItemStack(),Item.ingotIron}
        });

        //cleaning tube
        RecipeHelper.addRecipe(ComponentTypes.CLEANING_TUBE.getItemStack(2), new Object[][] {
                {orange,Item.ingotIron,orange},
                {orange,Item.ingotIron,orange},
                {orange,Item.ingotIron,orange}
        });

        //solar panel

        RecipeHelper.addRecipe(ComponentTypes.SOLAR_PANEL.getItemStack(),new Object[][] {
                {Item.glowstone,Item.redstone},
                {Item.ingotIron,Item.glowstone}
        });


        //magic thingy
        RecipeHelper.addRecipe(ComponentTypes.EYE_OF_GALGADOR.getItemStack(),new Object[][] {
                {Item.magmaCream,Item.fermentedSpiderEye, Item.magmaCream},
                {Item.ghastTear, Item.eyeOfEnder,Item.ghastTear},
                {Item.magmaCream,Item.fermentedSpiderEye, Item.magmaCream}
        });




        //magic metal
        RecipeHelper.addRecipe(ComponentTypes.LUMP_OF_GALGADOR.getItemStack(2),new Object[][] {
                {Item.glowstone,Block.blockDiamond, Item.glowstone},
                {ComponentTypes.EYE_OF_GALGADOR.getItemStack(), Item.glowstone,ComponentTypes.EYE_OF_GALGADOR.getItemStack()},
                {ComponentTypes.STABILIZED_METAL.getItemStack(),ComponentTypes.EYE_OF_GALGADOR.getItemStack(),ComponentTypes.STABILIZED_METAL.getItemStack()}
        });

        FurnaceRecipes.smelting().addSmelting(component.itemID, ComponentTypes.LUMP_OF_GALGADOR.getId(), ComponentTypes.GALGADORIAN_METAL.getItemStack(), 0F);




        RecipeHelper.addRecipe(ComponentTypes.LARGE_LUMP_OF_GALGADOR.getItemStack(),new Object[][] {
                {ComponentTypes.LUMP_OF_GALGADOR.getItemStack(),ComponentTypes.LUMP_OF_GALGADOR.getItemStack(),ComponentTypes.LUMP_OF_GALGADOR.getItemStack()},
                {ComponentTypes.LUMP_OF_GALGADOR.getItemStack(),ComponentTypes.LUMP_OF_GALGADOR.getItemStack(),ComponentTypes.LUMP_OF_GALGADOR.getItemStack()},
                {ComponentTypes.LUMP_OF_GALGADOR.getItemStack(),ComponentTypes.LUMP_OF_GALGADOR.getItemStack(),ComponentTypes.LUMP_OF_GALGADOR.getItemStack()}
        });

        FurnaceRecipes.smelting().addSmelting(component.itemID, ComponentTypes.LARGE_LUMP_OF_GALGADOR.getId(), ComponentTypes.ENHANCED_GALGADORIAN_METAL.getItemStack(), 0F);




        RecipeHelper.addRecipe(ComponentTypes.RED_GIFT_RIBBON.getItemStack(),new Object[][] {
                {Item.silk,Item.silk,Item.silk},
                {Item.silk,red,Item.silk},
                {Item.silk,Item.silk,Item.silk}
        });

        RecipeHelper.addRecipe(ComponentTypes.YELLOW_GIFT_RIBBON.getItemStack(),new Object[][] {
                {Item.silk,Item.silk,Item.silk},
                {Item.silk,yellow,Item.silk},
                {Item.silk,Item.silk,Item.silk}
        });

        RecipeHelper.addRecipe(ComponentTypes.WARM_HAT.getItemStack(),new Object[][] {
                {null,new ItemStack(Block.cloth,1,14),new ItemStack(Block.cloth,1,0)},
                {new ItemStack(Block.cloth,1,14),Item.diamond,new ItemStack(Block.cloth,1,14)},
                {new ItemStack(Block.cloth,1,14),new ItemStack(Block.cloth,1,14),new ItemStack(Block.cloth,1,14)}
        });

        RecipeHelper.addRecipe(ComponentTypes.SOCK.getItemStack(),new Object[][] {
                {new ItemStack(Block.cloth,1,14),new ItemStack(Block.cloth,1,14),Item.cookie},
                {new ItemStack(Block.cloth,1,14),new ItemStack(Block.cloth,1,14),Item.bucketMilk},
                {new ItemStack(Block.cloth,1,14),new ItemStack(Block.cloth,1,14),new ItemStack(Block.cloth,1,14)}
        });

        //advanced panel
        RecipeHelper.addRecipe(ComponentTypes.ADVANCED_SOLAR_PANEL.getItemStack(),new Object[][] {
                {ComponentTypes.SOLAR_PANEL.getItemStack(),null,ComponentTypes.SOLAR_PANEL.getItemStack()},
                {Item.ingotIron, ComponentTypes.SIMPLE_PCB.getItemStack(), Item.ingotIron},
                {ComponentTypes.SOLAR_PANEL.getItemStack(),null,ComponentTypes.SOLAR_PANEL.getItemStack()}
        });
        RecipeHelper.addRecipe(ComponentTypes.ADVANCED_SOLAR_PANEL.getItemStack(),new Object[][] {
                {ComponentTypes.SOLAR_PANEL.getItemStack(),Item.ingotIron,ComponentTypes.SOLAR_PANEL.getItemStack()},
                {null, ComponentTypes.SIMPLE_PCB.getItemStack(), null},
                {ComponentTypes.SOLAR_PANEL.getItemStack(),Item.ingotIron,ComponentTypes.SOLAR_PANEL.getItemStack()}
        });


        //blank upgrade

        RecipeHelper.addRecipe(ComponentTypes.BLANK_UPGRADE.getItemStack(2),new Object[][] {
                {Item.ingotIron,Item.ingotIron,Item.ingotIron},
                {ComponentTypes.REINFORCED_METAL.getItemStack(), Item.redstone, ComponentTypes.REINFORCED_METAL.getItemStack()},
                {Item.ingotIron,ComponentTypes.ADVANCED_PCB.getItemStack(),Item.ingotIron}
        });




        //valve
        RecipeHelper.addRecipe(ComponentTypes.TANK_VALVE.getItemStack(8), new Object[][] {
                {null, Item.ingotIron, null},
                {Item.ingotIron, Block.fenceIron, Item.ingotIron},
                {null, Item.ingotIron, null}
        });

        //tank pane
        RecipeHelper.addRecipe(ComponentTypes.TANK_PANE.getItemStack(32), new Object[][] {
                {Block.thinGlass,  Block.thinGlass, Block.thinGlass},
                {Block.glass, Block.thinGlass, Block.glass},
                {Block.thinGlass, Block.thinGlass, Block.thinGlass}
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
                {ComponentTypes.CLEANING_FAN.getItemStack(),Item.ingotIron,ComponentTypes.CLEANING_FAN.getItemStack()},
                {ComponentTypes.LIQUID_CLEANING_TUBE.getItemStack(),ComponentTypes.LIQUID_CLEANING_TUBE.getItemStack(),ComponentTypes.LIQUID_CLEANING_TUBE.getItemStack()},
                {Item.ingotIron,ComponentTypes.LIQUID_CLEANING_TUBE.getItemStack(),Item.ingotIron}
        });

        //cleaning tube
        RecipeHelper.addRecipe(ComponentTypes.LIQUID_CLEANING_TUBE.getItemStack(2), new Object[][] {
                {green,Item.ingotIron,green},
                {green,Item.ingotIron,green},
                {green,Item.ingotIron,green}
        });


        //Easter Eggs, really, Eggs you paint at Easter. Those kind of Easter Eggs.

        RecipeHelper.addRecipe(ComponentTypes.EXPLOSIVE_EASTER_EGG.getItemStack(), new Object[][] {
                {Item.gunpowder,Item.gunpowder,Item.gunpowder},
                {Item.gunpowder,Item.egg,Item.gunpowder},
                {Item.gunpowder,green,Item.gunpowder}
        });

        RecipeHelper.addRecipe(ComponentTypes.BURNING_EASTER_EGG.getItemStack(), new Object[][] {
                {Item.blazePowder,Item.blazeRod,Item.blazePowder},
                {Item.blazePowder,Item.egg,Item.blazePowder},
                {red,Item.magmaCream,yellow}
        });

        RecipeHelper.addRecipe(ComponentTypes.GLISTERING_EASTER_EGG.getItemStack(), new Object[][] {
                {Item.goldNugget,Item.goldNugget,Item.goldNugget},
                {Item.goldNugget,Item.egg,Item.goldNugget},
                {Item.goldNugget,blue,Item.goldNugget}
        });

        ItemStack chocolate = new ItemStack(Item.dyePowder, 1, 3);

        RecipeHelper.addRecipe(ComponentTypes.CHOCOLATE_EASTER_EGG.getItemStack(), new Object[][] {
                {chocolate, Item.sugar, chocolate},
                {chocolate,Item.egg,chocolate},
                {chocolate, Item.sugar, chocolate}
        });

        RecipeHelper.addRecipe(ComponentTypes.BASKET.getItemStack(), new Object[][] {
                {Item.stick, Item.stick, Item.stick},
                {Item.stick,null,Item.stick},
                {planks, planks, planks}
        });


        //wood
        for (int i = 0; i < 4; i++) {
            RecipeHelper.addRecipe(new ItemStack(Block.planks, 2, i), new Object[][] {
                    {ItemCartComponent.getWood(i, true)}
            });

            RecipeHelper.addRecipe(new ItemStack(Item.stick, 2), new Object[][] {
                    {ItemCartComponent.getWood(i, false)}
            });
        }


        //higher tier saw heads
        RecipeHelper.addRecipe(ComponentTypes.HARDENED_SAW_BLADE.getItemStack(),new Object[][] {
                {Item.ingotIron,Item.ingotIron, ComponentTypes.REINFORCED_METAL.getItemStack()}
        });
        RecipeHelper.addRecipe(ComponentTypes.GALGADORIAN_SAW_BLADE.getItemStack(),new Object[][] {
                {Item.ingotIron,Item.ingotIron, ComponentTypes.GALGADORIAN_METAL.getItemStack()}
        });

        //galgadorian wheels
        RecipeHelper.addRecipe(ComponentTypes.GALGADORIAN_WHEELS.getItemStack(),new Object[][] {
                {null, ComponentTypes.REINFORCED_METAL.getItemStack(), null},
                {ComponentTypes.REINFORCED_METAL.getItemStack(), ComponentTypes.GALGADORIAN_METAL.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack()},
                {null, ComponentTypes.REINFORCED_METAL.getItemStack(), null}
        });




        //iron blade
        RecipeHelper.addRecipe(ComponentTypes.IRON_BLADE.getItemStack(4),new Object[][] {
                {null, Item.shears, null},
                {Item.ingotIron, Item.ingotIron, Item.ingotIron},
                {null, Item.ingotIron, null}
        });


        //blade arm
        RecipeHelper.addRecipe(ComponentTypes.BLADE_ARM.getItemStack(),new Object[][] {
                {ComponentTypes.IRON_BLADE.getItemStack(), null, ComponentTypes.IRON_BLADE.getItemStack()},
                {null, Item.ingotIron, null},
                {ComponentTypes.IRON_BLADE.getItemStack(), null, ComponentTypes.IRON_BLADE.getItemStack()}
        });



        //storage blocks
        ItemBlockStorage.loadRecipes();
    }


    private Items() {}
}
