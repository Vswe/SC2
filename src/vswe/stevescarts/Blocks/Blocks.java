package vswe.stevescarts.Blocks;


import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.Configuration;
import vswe.stevescarts.Helpers.ComponentTypes;
import vswe.stevescarts.Helpers.RecipeHelper;
import vswe.stevescarts.Items.ItemBlockDetector;
import vswe.stevescarts.Items.ItemBlockStorage;
import vswe.stevescarts.Items.ItemUpgrade;
import vswe.stevescarts.Items.Items;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.TileEntities.*;

import java.lang.reflect.Constructor;

public enum Blocks {
    CARGO_MANAGER("BlockCargoManager", "Cargo Manager", BlockCargoManager.class, "CargoManager", 901, TileEntityCargo.class, "cargo"),
    JUNCTION("BlockJunction", "Junction Rail", BlockRailJunction.class, "Junctionrail", 903),
    ADVANCED_DETECTOR("BlockAdvDetector", "Advanced Detector Rail", BlockRailAdvDetector.class, "AdvancedDetector", 904),
    CART_ASSEMBLER("BlockCartAssembler", "Cart Assembler", BlockCartAssembler.class, "CartAssembler", 902, TileEntityCartAssembler.class, "assembler"),
    MODULE_TOGGLER("BlockActivator", "Module Toggler", BlockActivator.class, "Toggler", 905, TileEntityActivator.class, "activator"),
    EXTERNAL_DISTRIBUTOR("BlockDistributor", "External Distributor", BlockDistributor.class, "Distributor", 906, TileEntityDistributor.class, "distributor"),
    DETECTOR_UNIT("BlockDetector", null, BlockDetector.class, "Detector", 907, TileEntityDetector.class, "detector", ItemBlockDetector.class),
    UPGRADE("upgrade", null, BlockUpgrade.class, "Upgrade", 908, TileEntityUpgrade.class, "upgrade", ItemUpgrade.class),
    LIQUID_MANAGER("BlockLiquidManager", "Liquid Manager", BlockLiquidManager.class, "LiquidManager", 909, TileEntityLiquid.class, "liquid"),
    STORAGE("BlockMetalStorage", null, BlockMetalStorage.class, "StorageBlocks", 910, ItemBlockStorage.class);


    private final String name;
    private final String displayName;
    private final Class<? extends Block> clazz;
    private final String configEntry;
    private final int defaultId;
    private final Class<? extends TileEntity> tileEntityClazz;
    private final String tileEntityName;
    private final Class<? extends ItemBlock> itemClazz;

    private Block block;

    Blocks(String name, String displayName, Class<? extends Block> clazz, String configEntry, int defaultId) {
        this(name, displayName, clazz, configEntry, defaultId, null, null);
    }

    Blocks(String name, String displayName, Class<? extends Block> clazz, String configEntry, int defaultId, Class<? extends TileEntity> tileEntityClazz, String tileEntityName) {
        this(name, displayName, clazz, configEntry, defaultId, tileEntityClazz, tileEntityName, ItemBlock.class);
    }

    Blocks(String name, String displayName, Class<? extends Block> clazz, String configEntry, int defaultId, Class<? extends ItemBlock> itemClazz) {
        this(name, displayName, clazz, configEntry, defaultId, null, null, itemClazz);
    }

    Blocks(String name, String displayName, Class<? extends Block> clazz, String configEntry, int defaultId, Class<? extends TileEntity> tileEntityClazz, String tileEntityName, Class<? extends ItemBlock> itemClazz) {
        this.name = name;
        this.displayName = displayName;
        this.clazz = clazz;
        this.configEntry = configEntry;
        this.defaultId = defaultId;
        this.tileEntityClazz = tileEntityClazz;
        this.tileEntityName = tileEntityName;
        this.itemClazz = itemClazz;
    }


    public static void init(Configuration config) {
        for (Blocks blockInfo : values()) {
            try {
                Constructor<? extends Block> blockConstructor = blockInfo.clazz.getConstructor(new Class[] {int.class});
                Object blockInstance = blockConstructor.newInstance(new Object[] {config.getBlock(blockInfo.configEntry, blockInfo.defaultId).getInt(blockInfo.defaultId)});

                Block block = (Block)blockInstance;
                block.setHardness(2F).setStepSound(Block.soundMetalFootstep);
                GameRegistry.registerBlock(block, blockInfo.itemClazz, blockInfo.name);
                block.setUnlocalizedName(StevesCarts.localStart + blockInfo.name);

                blockInfo.block = block;

                if (blockInfo.tileEntityClazz != null) {
                    GameRegistry.registerTileEntity(blockInfo.tileEntityClazz, blockInfo.tileEntityName);
                }
            }catch(Exception e) {
                System.out.println("Failed to create block (" + blockInfo.name + ")");

                e.printStackTrace();
            }
        }


        STORAGE.block.setHardness(5.0F).setResistance(10.0F);
    }

    public static void addNames() {
        for (Blocks blockInfo : values()) {
            if (blockInfo.displayName != null) {
                LanguageRegistry.addName(blockInfo.block, blockInfo.displayName);
            }
        }
    }

    public static void addRecipes() {
        String blue = "dyeBlue";
        String orange = "dyeOrange";



        //cargo manager
        RecipeHelper.addRecipe(new ItemStack(CARGO_MANAGER.block, 1), new Object[][]{
                {ComponentTypes.LARGE_IRON_PANE.getItemStack(), ComponentTypes.HUGE_IRON_PANE.getItemStack(), ComponentTypes.LARGE_IRON_PANE.getItemStack()},
                {ComponentTypes.HUGE_IRON_PANE.getItemStack(), ComponentTypes.LARGE_DYNAMIC_PANE.getItemStack(), ComponentTypes.HUGE_IRON_PANE.getItemStack()},
                {ComponentTypes.LARGE_IRON_PANE.getItemStack(), ComponentTypes.HUGE_IRON_PANE.getItemStack(), ComponentTypes.LARGE_IRON_PANE.getItemStack()}
        });


        //activator
        RecipeHelper.addRecipe(new ItemStack(MODULE_TOGGLER.block, 1), new Object[][]{
                {orange, Item.ingotGold, blue},
                {Block.stone, Item.ingotIron, Block.stone},
                {Item.redstone, ComponentTypes.ADVANCED_PCB.getItemStack(), Item.redstone}
        });

        //distributor
        RecipeHelper.addRecipe(new ItemStack(EXTERNAL_DISTRIBUTOR.block, 1), new Object[][]{
                {Block.stone, ComponentTypes.SIMPLE_PCB.getItemStack(), Block.stone},
                {ComponentTypes.SIMPLE_PCB.getItemStack(), Item.redstone, ComponentTypes.SIMPLE_PCB.getItemStack()},
                {Block.stone, ComponentTypes.SIMPLE_PCB.getItemStack(), Block.stone}
        });

        //cart assembler
        RecipeHelper.addRecipe(new ItemStack(CART_ASSEMBLER.block, 1), new Object[][]{
                {Item.ingotIron, Block.stone, Item.ingotIron},
                {Block.stone, Item.ingotIron, Block.stone},
                {ComponentTypes.SIMPLE_PCB.getItemStack(), Block.stone, ComponentTypes.SIMPLE_PCB.getItemStack()}
        });

        //junction rail
        RecipeHelper.addRecipe(new ItemStack(JUNCTION.block, 1), new Object[][]{
                {null, Item.redstone, null},
                {Item.redstone, Block.rail, Item.redstone},
                {null, Item.redstone, null}
        });


        //adv detector rail
        RecipeHelper.addRecipe(new ItemStack(ADVANCED_DETECTOR.block, 2), new Object[][]{
                {Item.ingotIron, Block.pressurePlateStone, Item.ingotIron},
                {Item.ingotIron, Item.redstone, Item.ingotIron},
                {Item.ingotIron, Block.pressurePlateStone, Item.ingotIron}
        });

        /** === detector units === **/
        //detector unit
        ItemStack unit = new ItemStack(DETECTOR_UNIT.block, 1 , 1);
        RecipeHelper.addRecipe(unit, new Object[][]{
                {Block.cobblestone, Block.pressurePlateStone, Block.cobblestone},
                {Item.ingotIron, ComponentTypes.SIMPLE_PCB.getItemStack(), Item.ingotIron},
                {Block.cobblestone, Item.redstone, Block.cobblestone}
        });
        //detector manager
        RecipeHelper.addRecipe(new ItemStack(DETECTOR_UNIT.block, 1, 0), new Object[][]{
                {ComponentTypes.SIMPLE_PCB.getItemStack()},
                {unit}
        });
        //detector station
        RecipeHelper.addRecipe(new ItemStack(DETECTOR_UNIT.block, 1, 2), new Object[][]{
                {Item.ingotIron, Item.ingotIron, Item.ingotIron},
                {null, unit, null},
                {null, ComponentTypes.SIMPLE_PCB.getItemStack(), null}
        });
        //detector junction
        RecipeHelper.addRecipe(new ItemStack(DETECTOR_UNIT.block, 1, 3), new Object[][]{
                {Block.torchRedstoneActive, null, Block.torchRedstoneActive},
                {Item.redstone, unit, Item.redstone},
                {null, ComponentTypes.SIMPLE_PCB.getItemStack(), null}
        });
        //detector redstone
        RecipeHelper.addRecipe(new ItemStack(DETECTOR_UNIT.block, 1, 4), new Object[][]{
                {Item.redstone, Item.redstone, Item.redstone},
                {Item.redstone, unit, Item.redstone},
                {Item.redstone, Item.redstone, Item.redstone}
        });
        /** **/

        ItemStack advtank = new ItemStack(Items.modules, 1, 66);

        //liquid manager
        RecipeHelper.addRecipe(new ItemStack(LIQUID_MANAGER.block, 1), new Object[][]{
                {advtank, Item.ingotIron, advtank},
                {Item.ingotIron, ComponentTypes.TANK_VALVE, Item.ingotIron},
                {advtank, Item.ingotIron, advtank}
        });
    }

    public Block getBlock() {
        return block;
    }

    public int getId() {
        return block.blockID;
    }

}
