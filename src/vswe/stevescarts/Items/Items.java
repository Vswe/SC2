package vswe.stevescarts.Items;


import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.Configuration;
import vswe.stevescarts.Blocks.Blocks;
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

        ModuleData.init();

        for (ModuleData module : ModuleData.getList().values()) {
            if (!module.getIsLocked()) {
                validModules.put(module.getID(), config.get("EnabledModules", module.getName().replace(" ", "").replace(":","_"), module.getEnabledByDefault()).getBoolean(true));
            }
        }
    }

    public static void postBlockInit(Configuration config) {
        detectors = (ItemBlockDetector) Item.itemsList[Blocks.DETECTOR_UNIT.getId()];
        upgrades = (ItemUpgrade)Item.itemsList[Blocks.UPGRADE.getId()];
        storages = (ItemBlockStorage)Item.itemsList[Blocks.STORAGE.getId()];
    }

    public static void addNames() {
        LanguageRegistry.addName(carts, carts.getName());


        for (int i = 0; i < ItemCartComponent.size(); i++) {
            ItemStack subcomponent = new ItemStack(component,1,i);
            LanguageRegistry.addName(subcomponent, component.getName(subcomponent));
        }
        //invalid component
        LanguageRegistry.addName(new ItemStack(component,1,255), component.getName(null));

        for (ModuleData module : ModuleData.getList().values()) {
            ItemStack submodule = new ItemStack(modules,1,module.getID());
            LanguageRegistry.addName(submodule, modules.getName(submodule));
        }

        //invalid module
        LanguageRegistry.addName(new ItemStack(modules,1,255), modules.getName(null));


        for (int i = 0; i < ItemBlockStorage.blocks.length; i++) {
            ItemStack storage = new ItemStack(storages, 1, i);
            LanguageRegistry.addName(storage, storages.getName(storage));
        }

        for (AssemblerUpgrade upgrade : AssemblerUpgrade.getUpgradesList()) {
            ItemStack upgradeStack = new ItemStack(upgrades, 1, upgrade.getId());
            LanguageRegistry.addName(upgradeStack, upgrades.getName(upgradeStack));
        }

        for (DetectorType type : DetectorType.values()) {
            ItemStack stack = new ItemStack(detectors, 1, type.getMeta());
            LanguageRegistry.addName(stack, type.getName());
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
        ItemStack sapling = new ItemStack(Block.sapling, 1, -1);

        //wooden wheels
        RecipeHelper.addRecipe(new ItemStack(component, 1, 0), new Object[][]{
                {null, Item.stick, null},
                {Item.stick, planks, Item.stick},
                {null, Item.stick, null}
        });

        //iron wheels
        RecipeHelper.addRecipe(new ItemStack(component, 1 , 1), new Object[][] {
                {null, Item.stick, null},
                {Item.stick, Item.ingotIron, Item.stick},
                {null, Item.stick, null}
        });

        String red = "dyeRed";
        String green = "dyeGreen";
        String blue = "dyeBlue";
        String orange = "dyeOrange";
        String yellow = "dyeYellow";
        String black = "dyeBlack";
        String white = "dyeWhite";
        String brown = "dyeBrown";



        //color component (RED)
        RecipeHelper.addRecipe( new ItemStack(component, 1 , 2), new Object[][] {
                {null, Item.glowstone, null},
                {red, red, red},
                {null, Item.glowstone, null}
        });

        //color component (GREEN)
        RecipeHelper.addRecipe(new ItemStack(component, 1 , 3), new Object[][] {
                {null, Item.glowstone, null},
                {green, green, green},
                {null, Item.glowstone, null}
        });

        //color component (BLUE)
        RecipeHelper.addRecipe(new ItemStack(component, 1 , 4), new Object[][] {
                {null, Item.glowstone, null},
                {blue, blue, blue},
                {null, Item.glowstone, null}
        });


        //magic glass
        RecipeHelper.addRecipe(new ItemStack(component, 1 , 5), new Object[][] {
                {Block.thinGlass, Item.fermentedSpiderEye, Block.thinGlass},
                {Block.thinGlass, Item.redstone, Block.thinGlass},
                {Block.thinGlass, Block.thinGlass, Block.thinGlass}
        });

        ItemStack fuse = new ItemStack(component, 12, 43);

        //fuse
        RecipeHelper.addRecipe(fuse, new Object[][] {
                {Item.silk},
                {Item.silk},
                {Item.silk}
        });

        //dynamite
        RecipeHelper.addRecipe(new ItemStack(component, 1 , 6), new Object[][] {
                {fuse},
                {Item.gunpowder},
                {Item.gunpowder}
        });

        ItemStack smalltank = new ItemStack(component, 1 , 7);

        //small tank
        RecipeHelper.addRecipe(smalltank, new Object[][] {
                {Block.thinGlass,  Item.bucketEmpty, Block.thinGlass},
                {Block.glass, null, Block.glass},
                {Block.thinGlass, Item.bucketEmpty, Block.thinGlass}
        });

        //big tank
        RecipeHelper.addRecipe(new ItemStack(component, 1 , 8), new Object[][] {
                {Block.thinGlass, smalltank, Block.thinGlass},
                {Block.glass, null, Block.glass},
                {Block.thinGlass, smalltank, Block.thinGlass}
        });

        ItemStack pcb = new ItemStack(component, 1 , 9);

        //pcb
        RecipeHelper.addRecipe(pcb, new Object[][] {
                {Item.ingotIron,Item.redstone,Item.ingotIron},
                {Item.redstone,Item.ingotGold,Item.redstone},
                {Item.ingotIron,Item.redstone,Item.ingotIron}
        });
        RecipeHelper.addRecipe(pcb, new Object[][] {
                {Item.redstone,Item.ingotIron,Item.redstone},
                {Item.ingotIron,Item.ingotGold,Item.ingotIron},
                {Item.redstone,Item.ingotIron,Item.redstone}
        });

        //screen
        RecipeHelper.addRecipe(new ItemStack(component, 1 , 10), new Object[][] {
                {Item.ingotGold,Item.diamond,Item.ingotGold},
                {Block.thinGlass,pcb,Block.thinGlass},
                {Item.redstone,Block.thinGlass,Item.redstone}
        });

        ItemStack prehandle = new ItemStack(component, 1 , 11);
        ItemStack handle = new ItemStack(component, 1 , 12);

        //unrefined handle
        RecipeHelper.addRecipe(prehandle,new Object[][] {{null, null, Item.ingotIron},
                {null, Item.ingotIron, null},
                {Item.ingotIron, null, null}
        });

        FurnaceRecipes.smelting().addSmelting(component.itemID, prehandle.getItemDamage(), handle, 0F);

        //speed handle
        RecipeHelper.addRecipe(new ItemStack(component, 1 , 13),new Object[][] {{null, null, blue},
                {Item.ingotGold, handle, null},
                {Item.redstone, Item.ingotGold, null}
        });

        //wheel
        RecipeHelper.addRecipe(new ItemStack(component, 1 , 14),new Object[][] {
                {Item.ingotIron,Item.stick,Item.ingotIron},
                {Item.stick,Item.ingotIron,Item.stick},
                {null,Item.stick,null}
        });

        //saw head
        RecipeHelper.addRecipe(new ItemStack(component, 1 , 15),new Object[][] {
                {Item.ingotIron,Item.ingotIron,Item.diamond}
        });


        ItemStack advpcb = new ItemStack(component, 1 , 16);

        //advanced pcb
        RecipeHelper.addRecipe(advpcb,new Object[][] {
                {Item.redstone,Item.ingotIron,Item.redstone},
                {pcb,Item.ingotIron,pcb},
                {Item.redstone,Item.ingotIron,Item.redstone},
        });

        //wood cutter
        RecipeHelper.addRecipe(new ItemStack(component, 1 , 17),new Object[][] {
                {"treeSapling","treeSapling","treeSapling"},
                {"treeSapling",advpcb,"treeSapling"},
                {"treeSapling","treeSapling","treeSapling"}
        });


        ItemStack rawhardstuff = new ItemStack(component, 2 , 18);
        ItemStack hardstuff = new ItemStack(component, 1 , 19);

        RecipeHelper.addRecipe(rawhardstuff,new Object[][] {
                {Block.obsidian,null,Block.obsidian},
                {null,Item.diamond,null},
                {Block.obsidian,null,Block.obsidian}
        });


        FurnaceRecipes.smelting().addSmelting(component.itemID, rawhardstuff.getItemDamage(), hardstuff, 0F);

        ItemStack hardmesh = new ItemStack(component, 1 , 20);

        //hardened mesh
        RecipeHelper.addRecipe(hardmesh,new Object[][] {
                {Block.fenceIron,hardstuff,Block.fenceIron},
                {hardstuff,Block.fenceIron,hardstuff},
                {Block.fenceIron,hardstuff,Block.fenceIron}
        });

        ItemStack rawhardmetal = new ItemStack(component, 5 , 21);
        ItemStack hardmetal = new ItemStack(component, 1 , 22);

        //hardened metal
        RecipeHelper.addRecipe(rawhardmetal,new Object[][] {
                {Item.ingotIron,hardmesh,Item.ingotIron},
                {Item.ingotIron,Item.ingotIron,Item.ingotIron},
                {hardstuff,hardmesh,hardstuff}
        });

        FurnaceRecipes.smelting().addSmelting(component.itemID, rawhardmetal.getItemDamage(), hardmetal, 0F);




        //hardened wheels
        RecipeHelper.addRecipe(new ItemStack(component, 1 , 23),new Object[][] {
                {null,Item.ingotIron,null},
                {Item.ingotIron,hardmetal,Item.ingotIron},
                {null,Item.ingotIron,null}
        });




        //pipe
        RecipeHelper.addRecipe(new ItemStack(component, 1 , 24), new Object[][] {
                {Block.stone,Block.stone,Block.stone},
                {Item.ingotIron,null,null}
        });

        //shooting core
        RecipeHelper.addRecipe(new ItemStack(component, 1 , 25), new Object[][] {
                {Item.redstone,null,Item.redstone},
                {Item.redstone,Item.ingotGold,Item.redstone},
                {Block.dispenser,pcb,Block.dispenser}
        });

        //mob scanner
        RecipeHelper.addRecipe(new ItemStack(component, 1 , 26), new Object[][] {
                {Item.ingotGold,pcb,Item.ingotGold},
                {Item.redstone,advpcb,Item.redstone},
                {Item.redstone,null,Item.redstone}
        });


        //entity analyzer
        RecipeHelper.addRecipe(new ItemStack(component, 1 , 27), new Object[][] {
                {Item.ingotIron,Item.redstone,Item.ingotIron},
                {Item.ingotIron,pcb,Item.ingotIron},
                {Item.ingotIron,Item.ingotIron,Item.ingotIron}
        });

        //empty entity disk
        RecipeHelper.addRecipe(new ItemStack(component, 1 , 28), new Object[][] {
                {Item.redstone},
                {pcb}
        });



        //tri-torch
        RecipeHelper.addRecipe(new ItemStack(component, 1 , 29), new Object[][] {
                {Block.torchWood, Block.torchWood, Block.torchWood}
        });

        ItemStack chestThingy = new ItemStack(component, 32 , 30);
        ItemStack bigChestThingy = new ItemStack(component, 1 , 31);
        ItemStack hugeChestThingy = new ItemStack(component, 1 , 32);
        ItemStack chestLock = new ItemStack(component, 8 , 33);
        ItemStack ironChestThingy = new ItemStack(component, 8 , 34);
        ItemStack bigIronChestThingy = new ItemStack(component, 1 , 35);
        ItemStack hugeIronChestThingy = new ItemStack(component, 1 , 36);
        ItemStack dynamicChestThingy = new ItemStack(component, 1 , 37);
        ItemStack bigDynamicChestThingy = new ItemStack(component, 1 , 38);
        ItemStack hugeDynamicChestThingy = new ItemStack(component, 1 , 39);

        //chest thingy
        RecipeHelper.addRecipe(chestThingy, new Object[][] {
                {planks, planks, planks},
                {wood, planks, wood},
                {planks, planks, planks}
        });


        //big chest thingy
        RecipeHelper.addRecipe(bigChestThingy, new Object[][] {
                {chestThingy, chestThingy},
                {chestThingy, chestThingy}
        });

        //large chest thingy
        RecipeHelper.addRecipe(hugeChestThingy, new Object[][] {
                {chestThingy,chestThingy,chestThingy},
                {chestThingy,chestThingy,chestThingy},
                {chestThingy,chestThingy,chestThingy}
        });

        //large chest thingy
        RecipeHelper.addRecipe(chestLock, new Object[][] {
                {Item.ingotIron},
                {Block.stone}
        });
        RecipeHelper.addRecipe(chestLock, new Object[][] {
                {Block.stone},
                {Item.ingotIron}
        });

        //iron chest thingy
        RecipeHelper.addRecipe(ironChestThingy, new Object[][] {
                {chestThingy,chestThingy,chestThingy},
                {chestThingy,Item.ingotIron,chestThingy},
                {chestThingy,chestThingy,chestThingy}
        });

        //big iron chest thingy
        RecipeHelper.addRecipe(bigIronChestThingy, new Object[][] {
                {ironChestThingy, ironChestThingy},
                {ironChestThingy, ironChestThingy}
        });

        //large iron chest thingy
        RecipeHelper.addRecipe(hugeIronChestThingy, new Object[][] {
                {ironChestThingy,ironChestThingy,ironChestThingy},
                {ironChestThingy,ironChestThingy,ironChestThingy},
                {ironChestThingy,ironChestThingy,ironChestThingy}
        });

        //dynamic chest thingy
        RecipeHelper.addRecipe(dynamicChestThingy, new Object[][] {
                {ironChestThingy},
                {Item.redstone}
        });
        RecipeHelper.addRecipe(dynamicChestThingy, new Object[][] {
                {Item.redstone},
                {ironChestThingy}
        });

        //big dynamic chest thingy
        RecipeHelper.addRecipe(bigDynamicChestThingy, new Object[][] {
                {null,dynamicChestThingy, null},
                {dynamicChestThingy,Item.redstone, dynamicChestThingy},
                {null,dynamicChestThingy, null}
        });

        //large dynamic chest thingy
        RecipeHelper.addRecipe(hugeDynamicChestThingy, new Object[][] {
                {dynamicChestThingy,dynamicChestThingy,dynamicChestThingy},
                {dynamicChestThingy,pcb,dynamicChestThingy},
                {dynamicChestThingy,dynamicChestThingy,dynamicChestThingy}
        });


        ItemStack cleaningfan = new ItemStack(component,1,40);
        ItemStack cleaningtube = new ItemStack(component,2,42);

        //cleaning fan
        RecipeHelper.addRecipe(cleaningfan, new Object[][] {
                {Block.fenceIron,Item.redstone,Block.fenceIron},
                {Item.redstone,null,Item.redstone},
                {Block.fenceIron,Item.redstone,Block.fenceIron}
        });


        //cleaning core
        RecipeHelper.addRecipe(new ItemStack(component,1,41), new Object[][] {
                {cleaningfan,Item.ingotIron,cleaningfan},
                {cleaningtube,cleaningtube,cleaningtube},
                {Item.ingotIron,cleaningtube,Item.ingotIron}
        });

        //cleaning tube
        RecipeHelper.addRecipe(cleaningtube, new Object[][] {
                {orange,Item.ingotIron,orange},
                {orange,Item.ingotIron,orange},
                {orange,Item.ingotIron,orange}
        });

        //solar panel
        ItemStack solarpanel = new ItemStack(component,1,44);
        RecipeHelper.addRecipe(solarpanel,new Object[][] {
                {Item.glowstone,Item.redstone},
                {Item.ingotIron,Item.glowstone}
        });

        ItemStack magicTingy = new ItemStack(component,1,45);


        //magic thingy
        RecipeHelper.addRecipe(magicTingy,new Object[][] {
                {Item.magmaCream,Item.fermentedSpiderEye, Item.magmaCream},
                {Item.ghastTear, Item.eyeOfEnder,Item.ghastTear},
                {Item.magmaCream,Item.fermentedSpiderEye, Item.magmaCream}
        });

        ItemStack rawmagicmetal = new ItemStack(component,2,46);
        ItemStack magicmetal = new ItemStack(component,1,47);

        //magic metal
        RecipeHelper.addRecipe(rawmagicmetal,new Object[][] {
                {Item.glowstone,Block.blockDiamond, Item.glowstone},
                {magicTingy, Item.glowstone,magicTingy},
                {rawhardmetal,magicTingy,rawhardmetal}
        });

        FurnaceRecipes.smelting().addSmelting(component.itemID, rawmagicmetal.getItemDamage(), magicmetal, 0F);


        ItemStack rawmagicmetalthing = new ItemStack(component,1,48);
        ItemStack magicmetalthing = new ItemStack(component,1,49);

        RecipeHelper.addRecipe(rawmagicmetalthing,new Object[][] {
                {rawmagicmetal,rawmagicmetal,rawmagicmetal},
                {rawmagicmetal,rawmagicmetal,rawmagicmetal},
                {rawmagicmetal,rawmagicmetal,rawmagicmetal}
        });

        FurnaceRecipes.smelting().addSmelting(component.itemID, rawmagicmetalthing.getItemDamage(), magicmetalthing, 0F);


        ItemStack redribbon = new ItemStack(component,1,54);
        ItemStack yellowribbon = new ItemStack(component,1,55);

        RecipeHelper.addRecipe(redribbon,new Object[][] {
                {Item.silk,Item.silk,Item.silk},
                {Item.silk,red,Item.silk},
                {Item.silk,Item.silk,Item.silk}
        });

        RecipeHelper.addRecipe(yellowribbon,new Object[][] {
                {Item.silk,Item.silk,Item.silk},
                {Item.silk,yellow,Item.silk},
                {Item.silk,Item.silk,Item.silk}
        });

        RecipeHelper.addRecipe(new ItemStack(component,1,53),new Object[][] {
                {null,new ItemStack(Block.cloth,1,14),new ItemStack(Block.cloth,1,0)},
                {new ItemStack(Block.cloth,1,14),Item.diamond,new ItemStack(Block.cloth,1,14)},
                {new ItemStack(Block.cloth,1,14),new ItemStack(Block.cloth,1,14),new ItemStack(Block.cloth,1,14)}
        });

        RecipeHelper.addRecipe(new ItemStack(component,1,56),new Object[][] {
                {new ItemStack(Block.cloth,1,14),new ItemStack(Block.cloth,1,14),Item.cookie},
                {new ItemStack(Block.cloth,1,14),new ItemStack(Block.cloth,1,14),Item.bucketMilk},
                {new ItemStack(Block.cloth,1,14),new ItemStack(Block.cloth,1,14),new ItemStack(Block.cloth,1,14)}
        });

        //advanced panel
        ItemStack advsolarpanel = new ItemStack(component,1,58);
        RecipeHelper.addRecipe(advsolarpanel,new Object[][] {
                {solarpanel,null,solarpanel},
                {Item.ingotIron, pcb, Item.ingotIron},
                {solarpanel,null,solarpanel}
        });
        RecipeHelper.addRecipe(advsolarpanel,new Object[][] {
                {solarpanel,Item.ingotIron,solarpanel},
                {null, pcb, null},
                {solarpanel,Item.ingotIron,solarpanel}
        });


        //blank upgrade
        ItemStack blankupgrade = new ItemStack(component,1,59);
        RecipeHelper.addRecipe(blankupgrade,new Object[][] {
                {Item.ingotIron,Item.ingotIron,Item.ingotIron},
                {hardmetal, Item.redstone, hardmetal},
                {Item.ingotIron,advpcb,Item.ingotIron}
        });


        ItemStack valve = new ItemStack(component, 8 , 60);
        ItemStack tankpane = new ItemStack(component, 32 , 61);
        ItemStack largetankpane = new ItemStack(component, 1 , 62);
        ItemStack hugetankpane = new ItemStack(component, 1 , 63);
        ItemStack advtank = new ItemStack(modules, 1 , 66);


        //valve
        RecipeHelper.addRecipe(valve, new Object[][] {
                {null, Item.ingotIron, null},
                {Item.ingotIron, Block.fenceIron, Item.ingotIron},
                {null, Item.ingotIron, null}
        });

        //tank pane
        RecipeHelper.addRecipe(tankpane, new Object[][] {
                {Block.thinGlass,  Block.thinGlass, Block.thinGlass},
                {Block.glass, Block.thinGlass, Block.glass},
                {Block.thinGlass, Block.thinGlass, Block.thinGlass}
        });

        //large tank pane
        RecipeHelper.addRecipe(largetankpane, new Object[][] {
                {tankpane,  tankpane},
                {tankpane, tankpane}
        });

        //huge tank pane
        RecipeHelper.addRecipe(hugetankpane, new Object[][] {
                {tankpane,  tankpane, tankpane},
                {tankpane, tankpane, tankpane},
                {tankpane, tankpane, tankpane}
        });

        ItemStack cleaningtubeliquid = new ItemStack(component,2,65);

        //cleaning core
        RecipeHelper.addRecipe(new ItemStack(component,1,64), new Object[][] {
                {cleaningfan,Item.ingotIron,cleaningfan},
                {cleaningtubeliquid,cleaningtubeliquid,cleaningtubeliquid},
                {Item.ingotIron,cleaningtubeliquid,Item.ingotIron}
        });

        //cleaning tube
        RecipeHelper.addRecipe(cleaningtubeliquid, new Object[][] {
                {green,Item.ingotIron,green},
                {green,Item.ingotIron,green},
                {green,Item.ingotIron,green}
        });


        //Easter Eggs, really, Eggs you paint at Easter. Those kind of Easter Eggs.
        ItemStack easterEggExplosive = new ItemStack(component,1,66);
        ItemStack easterEggBurning = new ItemStack(component,1,67);
        ItemStack easterEggGlistering = new ItemStack(component,1,68);
        ItemStack easterEggChocolate = new ItemStack(component,1,69);


        RecipeHelper.addRecipe(easterEggExplosive, new Object[][] {
                {Item.gunpowder,Item.gunpowder,Item.gunpowder},
                {Item.gunpowder,Item.egg,Item.gunpowder},
                {Item.gunpowder,green,Item.gunpowder}
        });

        RecipeHelper.addRecipe(easterEggBurning, new Object[][] {
                {Item.blazePowder,Item.blazeRod,Item.blazePowder},
                {Item.blazePowder,Item.egg,Item.blazePowder},
                {red,Item.magmaCream,yellow}
        });

        RecipeHelper.addRecipe(easterEggGlistering, new Object[][] {
                {Item.goldNugget,Item.goldNugget,Item.goldNugget},
                {Item.goldNugget,Item.egg,Item.goldNugget},
                {Item.goldNugget,blue,Item.goldNugget}
        });

        ItemStack chocolate = new ItemStack(Item.dyePowder, 1, 3);

        RecipeHelper.addRecipe(easterEggChocolate, new Object[][] {
                {chocolate, Item.sugar, chocolate},
                {chocolate,Item.egg,chocolate},
                {chocolate, Item.sugar, chocolate}
        });

        ItemStack basket = new ItemStack(component,1,71);

        RecipeHelper.addRecipe(basket, new Object[][] {
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
        RecipeHelper.addRecipe(new ItemStack(component, 1 , 80),new Object[][] {
                {Item.ingotIron,Item.ingotIron, hardmetal}
        });
        RecipeHelper.addRecipe(new ItemStack(component, 1 , 81),new Object[][] {
                {Item.ingotIron,Item.ingotIron, magicmetal}
        });

        //galgadorian wheels
        RecipeHelper.addRecipe(new ItemStack(component, 1 , 82),new Object[][] {
                {null, hardmetal, null},
                {hardmetal, magicmetal, hardmetal},
                {null, hardmetal, null}
        });


        ItemStack ironblade = new ItemStack(component, 8 , 83);

        //iron blade
        RecipeHelper.addRecipe(ironblade,new Object[][] {
                {null, Item.shears, null},
                {Item.ingotIron, Item.ingotIron, Item.ingotIron},
                {null, Item.ingotIron, null}
        });


        //blade arm
        RecipeHelper.addRecipe(new ItemStack(component, 1 , 84),new Object[][] {
                {ironblade, null, ironblade},
                {null, hardmetal, null},
                {ironblade, null, ironblade}
        });



        //storage blocks
        ItemBlockStorage.loadRecipes();
    }


    private Items() {}
}
