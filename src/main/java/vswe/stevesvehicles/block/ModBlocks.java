package vswe.stevesvehicles.block;


import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import vswe.stevesvehicles.detector.DetectorType;
import vswe.stevesvehicles.old.Items.ItemBlockDetector;
import vswe.stevesvehicles.old.Items.ItemBlockStorage;
import vswe.stevesvehicles.old.Items.ItemUpgrade;
import vswe.stevesvehicles.recipe.IRecipeOutput;
import vswe.stevesvehicles.recipe.ModuleRecipeShaped;
import vswe.stevesvehicles.tileentity.TileEntityActivator;
import vswe.stevesvehicles.tileentity.TileEntityCargo;
import vswe.stevesvehicles.tileentity.TileEntityCartAssembler;
import vswe.stevesvehicles.tileentity.TileEntityDetector;
import vswe.stevesvehicles.tileentity.TileEntityDistributor;
import vswe.stevesvehicles.tileentity.TileEntityLiquid;
import vswe.stevesvehicles.tileentity.TileEntityUpgrade;

import static vswe.stevesvehicles.old.Helpers.ComponentTypes.*;

import java.lang.reflect.Constructor;

public enum ModBlocks implements IRecipeOutput {
    CARGO_MANAGER("cargo_manager", BlockCargoManager.class, TileEntityCargo.class, "cargo"),
    JUNCTION("junction_rail", BlockRailJunction.class),
    ADVANCED_DETECTOR("advanced_detector_rail", BlockRailAdvancedDetector.class),
    CART_ASSEMBLER("vehicle_assembler", BlockCartAssembler.class, TileEntityCartAssembler.class, "assembler"),
    MODULE_TOGGLER("module_toggler", BlockActivator.class, TileEntityActivator.class, "toggler"),
    EXTERNAL_DISTRIBUTOR("external_distributor", BlockDistributor.class, TileEntityDistributor.class, "distributor"),
    DETECTOR_UNIT("detector_unit", BlockDetector.class, TileEntityDetector.class, "detector", ItemBlockDetector.class),
    UPGRADE("upgrade", BlockUpgrade.class, TileEntityUpgrade.class, "upgrade", ItemUpgrade.class),
    LIQUID_MANAGER("liquid_manager", BlockLiquidManager.class, TileEntityLiquid.class, "liquid"),
    STORAGE("metal_storage", BlockMetalStorage.class, ItemBlockStorage.class);



    private final String unlocalizedName;
    private final Class<? extends IBlockBase> clazz;
    private final Class<? extends TileEntity> tileEntityClazz;
    private final String tileEntityName;
    private final Class<? extends ItemBlock> itemClazz;

    private Block block;

    ModBlocks(String unlocalizedName, Class<? extends IBlockBase> clazz) {
        this(unlocalizedName, clazz, null, null);
    }

    ModBlocks(String unlocalizedName, Class<? extends IBlockBase> clazz, Class<? extends TileEntity> tileEntityClazz, String tileEntityName) {
        this(unlocalizedName, clazz, tileEntityClazz, tileEntityName, ItemBlock.class);
    }

    ModBlocks(String unlocalizedName, Class<? extends IBlockBase> clazz, Class<? extends ItemBlock> itemClazz) {
        this(unlocalizedName, clazz, null, null, itemClazz);
    }

    ModBlocks(String unlocalizedName, Class<? extends IBlockBase> clazz, Class<? extends TileEntity> tileEntityClazz, String tileEntityName, Class<? extends ItemBlock> itemClazz) {
        this.unlocalizedName = unlocalizedName;
        this.clazz = clazz;
        this.tileEntityClazz = tileEntityClazz;
        this.tileEntityName = tileEntityName;
        this.itemClazz = itemClazz;
    }


    public static void init() {
        for (ModBlocks blockInfo : values()) {
            try {
                if (Block.class.isAssignableFrom(blockInfo.clazz)) {
                    Constructor<? extends IBlockBase> blockConstructor = blockInfo.clazz.getConstructor();
                    Object blockInstance = blockConstructor.newInstance();

                    IBlockBase blockBase = (IBlockBase)blockInstance;
                    Block block = (Block)blockInstance;
                    block.setHardness(2F).setStepSound(Block.soundTypeMetal);
                    GameRegistry.registerBlock(block, blockInfo.itemClazz, blockInfo.unlocalizedName);
                    blockBase.setUnlocalizedName("steves_vehicles:tile.common:" + blockInfo.unlocalizedName) ;

                    blockInfo.block = block;

                    if (blockInfo.tileEntityClazz != null) {
                        GameRegistry.registerTileEntity(blockInfo.tileEntityClazz, blockInfo.tileEntityName);
                    }
                }else{
                    System.out.println("This is not a block (" + blockInfo.unlocalizedName + ")");
                }
            }catch(Exception e) {
                System.out.println("Failed to create block (" + blockInfo.unlocalizedName + ")");

                e.printStackTrace();
            }
        }

    }


    private void addRecipeWithCount(int count, Object ... recipe) {
        GameRegistry.addRecipe(new ModuleRecipeShaped(this, count, 3, 3, recipe));
    }

    private void addRecipe(Object ... recipe) {
        GameRegistry.addRecipe(new ModuleRecipeShaped(this, 3, 3, recipe));
    }

    private static final String PLANKS = "plankWood";
    private static final String GLASS = "blockGlass";

    private static final String BLUE = "dyeBlue";
    private static final String ORANGE = "dyeOrange";

    public static void addRecipes() {



        CARGO_MANAGER.addRecipe(    PLANKS,     PLANKS,         PLANKS,
                                    PLANKS,     SIMPLE_PCB,     PLANKS,
                                    PLANKS,     PLANKS,         PLANKS);

        LIQUID_MANAGER.addRecipe(   GLASS,     GLASS,           GLASS,
                                    GLASS,     SIMPLE_PCB,      GLASS,
                                    GLASS,     GLASS,           GLASS);



        MODULE_TOGGLER.addRecipe(   ORANGE,             Items.gold_ingot,       BLUE,
                                    Blocks.stone,       Items.iron_ingot,       Blocks.stone,
                                    Items.redstone,     SIMPLE_PCB,             Items.redstone);


        EXTERNAL_DISTRIBUTOR.addRecipe( Blocks.stone,   SIMPLE_PCB,         Blocks.stone,
                                        SIMPLE_PCB,     Items.redstone,     SIMPLE_PCB,
                                        Blocks.stone,   SIMPLE_PCB,         Blocks.stone);


        CART_ASSEMBLER.addRecipe(   Items.iron_ingot,   Blocks.stone,       Items.iron_ingot,
                                    Blocks.stone,       Items.iron_ingot,   Blocks.stone,
                                    SIMPLE_PCB,         Blocks.stone,       SIMPLE_PCB);


        JUNCTION.addRecipe(null, Items.redstone, null,
                Items.redstone, Blocks.rail, Items.redstone,
                null, Items.redstone, null);



        ADVANCED_DETECTOR.addRecipeWithCount(2,
                Items.iron_ingot,   Blocks.stone_pressure_plate,    Items.iron_ingot,
                Items.iron_ingot,   Items.redstone,                 Items.iron_ingot,
                Items.iron_ingot,   Blocks.stone_pressure_plate,    Items.iron_ingot);



        DetectorType.UNIT.addShapedRecipe(  Blocks.cobblestone,     Blocks.stone_pressure_plate,    Blocks.cobblestone,
                                            Items.iron_ingot,       SIMPLE_PCB,                     Items.iron_ingot,
                                            Blocks.cobblestone,     Blocks.stone_pressure_plate,    Blocks.cobblestone);

        DetectorType.NORMAL.addShapelessRecipe(DetectorType.UNIT, SIMPLE_PCB, Items.redstone);
        DetectorType.STOP.addShapelessRecipe(DetectorType.UNIT, SIMPLE_PCB, Items.iron_ingot);
        DetectorType.JUNCTION.addShapelessRecipe(DetectorType.UNIT, SIMPLE_PCB, Blocks.redstone_torch);
        DetectorType.REDSTONE.addShapelessRecipe(DetectorType.UNIT, Items.redstone, Items.redstone, Items.redstone);

    }

    public Block getBlock() {
        return block;
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(getBlock());
    }
}
