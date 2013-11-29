package vswe.stevescarts;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;
import vswe.stevescarts.Blocks.BlockActivator;
import vswe.stevescarts.Blocks.BlockCargoManager;
import vswe.stevescarts.Blocks.BlockCartAssembler;
import vswe.stevescarts.Blocks.BlockDetector;
import vswe.stevescarts.Blocks.BlockDistributor;
import vswe.stevescarts.Blocks.BlockLiquidManager;
import vswe.stevescarts.Blocks.BlockMetalStorage;
import vswe.stevescarts.Blocks.BlockRailAdvDetector;
import vswe.stevescarts.Blocks.BlockRailJunction;
import vswe.stevescarts.Blocks.BlockUpgrade;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.CraftingHandler;
import vswe.stevescarts.Helpers.CreativeTabSC2;
import vswe.stevescarts.Helpers.DetectorType;
import vswe.stevescarts.Helpers.EntityCake;
import vswe.stevescarts.Helpers.EntityEasterEgg;
import vswe.stevescarts.Helpers.GeneratedInfo;
import vswe.stevescarts.Helpers.GiftItem;
import vswe.stevescarts.Helpers.ShapedRecipes2;
import vswe.stevescarts.Helpers.TradeHandler;
import vswe.stevescarts.Helpers.WoodFuelHandler;
import vswe.stevescarts.Items.ItemBlockDetector;
import vswe.stevescarts.Items.ItemBlockStorage;
import vswe.stevescarts.Items.ItemCartComponent;
import vswe.stevescarts.Items.ItemCartModule;
import vswe.stevescarts.Items.ItemCarts;
import vswe.stevescarts.Items.ItemUpgrade;
import vswe.stevescarts.Listeners.ChunkListener;
import vswe.stevescarts.Listeners.MobDeathListener;
import vswe.stevescarts.Listeners.MobInteractListener;
import vswe.stevescarts.Listeners.PlayerSleepListener;
import vswe.stevescarts.Listeners.TickListener;
import vswe.stevescarts.Listeners.TicketListener;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.TileEntities.TileEntityActivator;
import vswe.stevescarts.TileEntities.TileEntityCargo;
import vswe.stevescarts.TileEntities.TileEntityCartAssembler;
import vswe.stevescarts.TileEntities.TileEntityDetector;
import vswe.stevescarts.TileEntities.TileEntityDistributor;
import vswe.stevescarts.TileEntities.TileEntityLiquid;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;
import vswe.stevescarts.Upgrades.AssemblerUpgrade;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "StevesCarts", name = "Steve's Carts 2", version = GeneratedInfo.version)
@NetworkMod(channels = { "SC2" }, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class StevesCarts {

	public static boolean hasGreenScreen = false;
	public static boolean isChristmas = false;
	public static boolean isHalloween = false;
	public static boolean isEaster = false;	
	public static boolean freezeCartSimulation = false;
	public static boolean renderSteve = false;
	public static boolean arcadeDevOperator = false;
	
	public final String texturePath = "/assets/stevescarts/textures";
	//public final String soundPath = "/assets/stevescarts/sounds";
	public final String textureHeader = "stevescarts";
	public final String localStart = "SC2";
	
	public static ItemCarts carts;
	public static ItemCartComponent component;
	public static ItemCartModule modules;
	public static ItemUpgrade upgrades;
	public static ItemBlockStorage storages;
	public static ItemBlockDetector detectors;
	@SidedProxy(clientSide = "vswe.stevescarts.ClientProxy", serverSide = "vswe.stevescarts.CommonProxy")
	public static CommonProxy proxy;
	@Instance("StevesCarts")
	public static StevesCarts instance;

	public static CreativeTabSC2 tabsSC2 = new CreativeTabSC2("SC2Modules");
	public static CreativeTabSC2 tabsSC2Components = new CreativeTabSC2("SC2Items");
	public static CreativeTabSC2 tabsSC2Blocks = new CreativeTabSC2("SC2Blocks");
	
	public Block blockCargoManager;
	public Block blockJunction;
	public Block blockAdvDetector;
	public BlockCartAssembler blockAssembler;
	public Block blockActivator;
	public Block blockDistributor;
	public Block blockDetector;
	public Block blockUpgrade;
	public Block blockLiquidManager;
	public Block blockStorage;
	
	public ISimpleBlockRenderingHandler blockRenderer;
	
	
	private HashMap<String,Integer> IDs = new HashMap<String,Integer>();
	private HashMap<Byte,Boolean> validModules = new HashMap<Byte,Boolean>();
	public int maxDynamites = 50;
	public boolean useArcadeSounds;
	public boolean useArcadeMobSounds;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		addItemID(config,"ModularCart", 29743);
		addItemID(config,"ModuleComponents", 29742);
		addItemID(config,"CartModule", 29741);
		
		addBlockID(config,"CargoManager",901);
		addBlockID(config,"CartAssembler",902);
		addBlockID(config,"Junctionrail",903);
		addBlockID(config,"AdvancedDetector",904);
		addBlockID(config,"Toggler",905);
		addBlockID(config,"Distributor",906);
		addBlockID(config,"Detector",907);
		addBlockID(config,"Upgrade",908);
		addBlockID(config,"LiquidManager",909);	
		
		addBlockID(config,"StorageBlocks",910);
		

		(carts = new ItemCarts(getID("ModularCart"))).setUnlocalizedName(localStart + "ModularCart");
		component = new ItemCartComponent(getID("ModuleComponents"));
		modules = new ItemCartModule(getID("CartModule"));
		//modules.setUnlocalizedName(modules.getUnlocalizedName());
		
		ModuleData.init();
		
		
		for (ModuleData module : ModuleData.getList().values()) {
			if (!module.getIsLocked()) {
				validModules.put(module.getID(), config.get("EnabledModules", module.getName().replace(" ", "").replace(":","_"), module.getEnabledByDefault()).getBoolean(true));
			}
		}

		
		maxDynamites = Math.min(maxDynamites, config.get("Settings", "MaximumNumberOfDynamites", maxDynamites).getInt(maxDynamites));	
		useArcadeSounds = config.get("Settings", "useArcadeSounds", true).getBoolean(true);	
		useArcadeMobSounds = config.get("Settings", "useTetrisMobSounds", true).getBoolean(true);	
		
		config.save();
		
		blockCargoManager = new BlockCargoManager(getID("CargoManager")).setHardness(2F).setStepSound(Block.soundMetalFootstep);
		GameRegistry.registerBlock(blockCargoManager, "BlockCargoManager");
		blockCargoManager.setUnlocalizedName(localStart + "BlockCargoManager");
		GameRegistry.registerTileEntity(TileEntityCargo.class, "cargo");	

		blockAssembler = (BlockCartAssembler)(new BlockCartAssembler(getID("CartAssembler")).setHardness(2F).setStepSound(Block.soundMetalFootstep));
		GameRegistry.registerBlock(blockAssembler, "BlockCartAssembler");
		blockAssembler.setUnlocalizedName(localStart + "BlockCartAssembler");
		GameRegistry.registerTileEntity(TileEntityCartAssembler.class, "assembler");	

		blockJunction = new BlockRailJunction(getID("Junctionrail")).setHardness(3F).setStepSound(Block.soundMetalFootstep);
		GameRegistry.registerBlock(blockJunction, "BlockJunction");	
		blockJunction.setUnlocalizedName(localStart + "BlockJunction");
		
		blockAdvDetector = new BlockRailAdvDetector(getID("AdvancedDetector")).setHardness(3F).setStepSound(Block.soundMetalFootstep);
		GameRegistry.registerBlock(blockAdvDetector, "BlockAdvDetector");	
		blockAdvDetector.setUnlocalizedName(localStart + "BlockAdvDetector");

		blockActivator = new BlockActivator(getID("Toggler")).setHardness(2F).setStepSound(Block.soundMetalFootstep);
		GameRegistry.registerBlock(blockActivator, "BlockActivator");
		blockActivator.setUnlocalizedName(localStart + "BlockActivator");
		GameRegistry.registerTileEntity(TileEntityActivator.class, "activator");	

		blockDistributor = new BlockDistributor(getID("Distributor")).setHardness(2F).setStepSound(Block.soundMetalFootstep);
		GameRegistry.registerBlock(blockDistributor, "BlockDistributor");
		blockDistributor.setUnlocalizedName(localStart + "BlockDistributor");
		GameRegistry.registerTileEntity(TileEntityDistributor.class, "distributor");

		blockDetector = new BlockDetector(getID("Detector")).setHardness(2F).setStepSound(Block.soundMetalFootstep);
		GameRegistry.registerBlock(blockDetector, ItemBlockDetector.class, "BlockDetector");
		blockDetector.setUnlocalizedName(localStart + "BlockDetector");
		detectors = (ItemBlockDetector)Item.itemsList[getID("Detector")]; 	
		GameRegistry.registerTileEntity(TileEntityDetector.class, "detector");		
		

		
		blockUpgrade = new BlockUpgrade(getID("Upgrade")).setHardness(2F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("BlockUpgrade");
		GameRegistry.registerBlock(blockUpgrade, ItemUpgrade.class, "upgrade");
		GameRegistry.registerTileEntity(TileEntityUpgrade.class, "upgrade");
		upgrades = (ItemUpgrade)Item.itemsList[getID("Upgrade")]; 		
		AssemblerUpgrade.init();		

		blockLiquidManager = new BlockLiquidManager(getID("LiquidManager")).setHardness(2F).setStepSound(Block.soundMetalFootstep);
		GameRegistry.registerBlock(blockLiquidManager, "BlockLiquidManager");
		blockLiquidManager.setUnlocalizedName(localStart + "BlockLiquidManager");
		GameRegistry.registerTileEntity(TileEntityLiquid.class, "liquid");			
	
		
		ItemBlockStorage.init();		
		blockStorage = new BlockMetalStorage(getID("StorageBlocks")).setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundMetalFootstep);
		GameRegistry.registerBlock(blockStorage, ItemBlockStorage.class, "BlockMetalStorage");
		storages = (ItemBlockStorage)Item.itemsList[getID("StorageBlocks")]; 		
		
		

		initCart(0,MinecartModular.class);


		EntityRegistry.registerModEntity(EntityEasterEgg.class, "Egg.Vswe", 2, instance, 80, 3, true);
		EntityRegistry.registerModEntity(EntityCake.class, "Cake.Vswe", 3, instance, 80, 3, true);
		proxy.soundInit();
		
	}



	 @EventHandler
	public void load(FMLInitializationEvent evt) {

		LanguageRegistry.instance().addStringLocalization("itemGroup.SC2Modules", "en_US", "Steve's Carts 2 Modules");
		LanguageRegistry.instance().addStringLocalization("itemGroup.SC2Items", "en_US", "Steve's Carts 2 Components");
		LanguageRegistry.instance().addStringLocalization("itemGroup.SC2Blocks", "en_US", "Steve's Carts 2 Blocks");
	
		new TickListener();
		new TicketListener();
		new ChunkListener();
		new CraftingHandler();
		new WoodFuelHandler();
		//new TestListener();
		if (isChristmas) {
			new TradeHandler();
			new MobDeathListener();
			new MobInteractListener();
			new PlayerSleepListener();
		}

		
		LanguageRegistry.addName(carts, carts.getName());			
			
		
		for (int i = 0; i < ItemCartComponent.size(); i++) {
			ItemStack subcomponent = new ItemStack(component,1,i);
			LanguageRegistry.addName(subcomponent, component.getName(subcomponent));
		}
		LanguageRegistry.addName(new ItemStack(component,1,255), component.getName(null));
		
		for (ModuleData module : ModuleData.getList().values()) {
			ItemStack submodule = new ItemStack(modules,1,module.getID());
			LanguageRegistry.addName(submodule, modules.getName(submodule));
		
		
			if (!module.getIsLocked() && validModules.get(module.getID())) {
				module.loadRecipe();
			}
		}	
		GiftItem.init();
		
		LanguageRegistry.addName(new ItemStack(modules,1,255), modules.getName(null));

		
		
		NetworkRegistry.instance().registerGuiHandler(instance, proxy);
		proxy.renderInit();
		

		tabsSC2Blocks.setIcon(new ItemStack(blockAssembler, 1));
		
		
		LanguageRegistry.addName(blockCargoManager, "Cargo Manager");
		LanguageRegistry.addName(blockAssembler, "Cart Assembler");			
		LanguageRegistry.addName(blockJunction, "Junction Rail");
		LanguageRegistry.addName(blockAdvDetector, "Advanced Detector Rail");
		LanguageRegistry.addName(blockActivator, "Module Toggler");
		LanguageRegistry.addName(blockDistributor, "External Distributor");	
		LanguageRegistry.addName(blockDetector, "Detector Manager");
		LanguageRegistry.addName(blockLiquidManager, "Liquid Manager");
	
		
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
		
		TileEntityCargo.loadSelectionSettings();
		
	
		addRecipes();
		//MinecraftForge.EVENT_BUS.register(this);
	}
	
	private int getID(String name) {
		return IDs.get(name);
	}
	
	private void addBlockID(Configuration config, String name, int defaultID) {
		IDs.put(name,config.getBlock(name,defaultID).getInt(defaultID));
	}
	
	private void addItemID(Configuration config, String name, int defaultID) {
		IDs.put(name,config.getItem(name,defaultID).getInt(defaultID));
	}	
	
	private void initCart(int ID, Class<? extends MinecartModular> cart) {	
		EntityRegistry.registerModEntity(cart, "Minecart.Vswe." + ID, ID, instance, 80, 3, true);
		//MinecartRegistry.registerMinecart(cart, new ItemStack(carts, 1, ID));
	}
	


	@EventHandler
	public void modsLoaded(FMLPostInitializationEvent event) {

	}
	

	private void addRecipes() {	
		String planks = "plankWood"; //ore dict
		String wood = "logWood"; //ore dict
		ItemStack sapling = new ItemStack(Block.sapling, 1, -1);
	
		//wooden wheels
		addRecipe(new ItemStack(component, 1 , 0), new Object[][] {
												{null, Item.stick, null},
												{Item.stick, planks, Item.stick},
												{null, Item.stick, null}
		});

		//iron wheels
		addRecipe(new ItemStack(component, 1 , 1), new Object[][] {
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
		addRecipe( new ItemStack(component, 1 , 2), new Object[][] {
												{null, Item.glowstone, null},
												{red, red, red},
												{null, Item.glowstone, null}
		});	
		
		//color component (GREEN)
		addRecipe(new ItemStack(component, 1 , 3), new Object[][] {
												{null, Item.glowstone, null},
												{green, green, green},
												{null, Item.glowstone, null}
		});	

		//color component (BLUE)
		addRecipe(new ItemStack(component, 1 , 4), new Object[][] {
												{null, Item.glowstone, null},
												{blue, blue, blue},
												{null, Item.glowstone, null}
		});	

		
		//magic glass
		addRecipe(new ItemStack(component, 1 , 5), new Object[][] {
												{Block.thinGlass, Item.fermentedSpiderEye, Block.thinGlass},
												{Block.thinGlass, Item.redstone, Block.thinGlass},
												{Block.thinGlass, Block.thinGlass, Block.thinGlass}
		});		
		
		ItemStack fuse = new ItemStack(component, 12, 43);
		
		//fuse
		addRecipe(fuse, new Object[][] {
							{Item.silk},
							{Item.silk},
							{Item.silk}
		});			
		
		//dynamite
		addRecipe(new ItemStack(component, 1 , 6), new Object[][] {
												{fuse},
												{Item.gunpowder},
												{Item.gunpowder}
		});	

		ItemStack smalltank = new ItemStack(component, 1 , 7);
		
		//small tank
		addRecipe(smalltank, new Object[][] {
												{Block.thinGlass,  Item.bucketEmpty, Block.thinGlass},
												{Block.glass, null, Block.glass},
												{Block.thinGlass, Item.bucketEmpty, Block.thinGlass}
		});		

		//big tank
		addRecipe(new ItemStack(component, 1 , 8), new Object[][] {
												{Block.thinGlass, smalltank, Block.thinGlass},
												{Block.glass, null, Block.glass},
												{Block.thinGlass, smalltank, Block.thinGlass}
		});	

		ItemStack pcb = new ItemStack(component, 1 , 9);
		
		//pcb
		addRecipe(pcb, new Object[][] {
					{Item.ingotIron,Item.redstone,Item.ingotIron},
					{Item.redstone,Item.ingotGold,Item.redstone},
					{Item.ingotIron,Item.redstone,Item.ingotIron}			
		});	
		addRecipe(pcb, new Object[][] {
					{Item.redstone,Item.ingotIron,Item.redstone},
					{Item.ingotIron,Item.ingotGold,Item.ingotIron},
					{Item.redstone,Item.ingotIron,Item.redstone}			
		});			
		
		//screen
		addRecipe(new ItemStack(component, 1 , 10), new Object[][] {
												{Item.ingotGold,Item.diamond,Item.ingotGold},
												{Block.thinGlass,pcb,Block.thinGlass},
												{Item.redstone,Block.thinGlass,Item.redstone}	
		});	
		
		ItemStack prehandle = new ItemStack(component, 1 , 11);
		ItemStack handle = new ItemStack(component, 1 , 12);
		
		//unrefined handle
		addRecipe(prehandle,new Object[][] {{null, null, Item.ingotIron},
											{null, Item.ingotIron, null},
											{Item.ingotIron, null, null}
										});		
		
		FurnaceRecipes.smelting().addSmelting(component.itemID, prehandle.getItemDamage(), handle, 0F);
		
		//speed handle
		addRecipe(new ItemStack(component, 1 , 13),new Object[][] {{null, null, blue},
								   {Item.ingotGold, handle, null},
								   {Item.redstone, Item.ingotGold, null}
								  });

		//wheel
		addRecipe(new ItemStack(component, 1 , 14),new Object[][] {
								{Item.ingotIron,Item.stick,Item.ingotIron},
								{Item.stick,Item.ingotIron,Item.stick},
								{null,Item.stick,null}
							});								  
								  
		//saw head
		addRecipe(new ItemStack(component, 1 , 15),new Object[][] {
								{Item.ingotIron,Item.ingotIron,Item.diamond}
							});								 
							 

		ItemStack advpcb = new ItemStack(component, 1 , 16);

		//advanced pcb
		addRecipe(advpcb,new Object[][] {
				{Item.redstone,Item.ingotIron,Item.redstone},
				{pcb,Item.ingotIron,pcb},
				{Item.redstone,Item.ingotIron,Item.redstone},
			});	

		//wood cutter
		addRecipe(new ItemStack(component, 1 , 17),new Object[][] {
				{"treeSapling","treeSapling","treeSapling"},
				{"treeSapling",advpcb,"treeSapling"},
				{"treeSapling","treeSapling","treeSapling"}
			});
			
			
		ItemStack rawhardstuff = new ItemStack(component, 2 , 18);		
		ItemStack hardstuff = new ItemStack(component, 1 , 19);
		
		addRecipe(rawhardstuff,new Object[][] {
				{Block.obsidian,null,Block.obsidian},
				{null,Item.diamond,null},
				{Block.obsidian,null,Block.obsidian}
			});		
			
			
		FurnaceRecipes.smelting().addSmelting(component.itemID, rawhardstuff.getItemDamage(), hardstuff, 0F);

		ItemStack hardmesh = new ItemStack(component, 1 , 20);
		
		//hardened mesh
		addRecipe(hardmesh,new Object[][] {
				{Block.fenceIron,hardstuff,Block.fenceIron},
				{hardstuff,Block.fenceIron,hardstuff},
				{Block.fenceIron,hardstuff,Block.fenceIron}
			});		

		ItemStack rawhardmetal = new ItemStack(component, 5 , 21);
		ItemStack hardmetal = new ItemStack(component, 1 , 22);
		
		//hardened metal
		addRecipe(rawhardmetal,new Object[][] {
				{Item.ingotIron,hardmesh,Item.ingotIron},
				{Item.ingotIron,Item.ingotIron,Item.ingotIron},
				{hardstuff,hardmesh,hardstuff}
			});	

		FurnaceRecipes.smelting().addSmelting(component.itemID, rawhardmetal.getItemDamage(), hardmetal, 0F);	
		
		

		
		//hardened wheels
		addRecipe(new ItemStack(component, 1 , 23),new Object[][] {
				{null,Item.ingotIron,null},
				{Item.ingotIron,hardmetal,Item.ingotIron},
				{null,Item.ingotIron,null}
			});	



		
		//pipe
		addRecipe(new ItemStack(component, 1 , 24), new Object[][] {
				{Block.stone,Block.stone,Block.stone},
				{Item.ingotIron,null,null}
			});	

		//shooting core
		addRecipe(new ItemStack(component, 1 , 25), new Object[][] {
				{Item.redstone,null,Item.redstone},
				{Item.redstone,Item.ingotGold,Item.redstone},
				{Block.dispenser,pcb,Block.dispenser}
			});

		//mob scanner
		addRecipe(new ItemStack(component, 1 , 26), new Object[][] {
				{Item.ingotGold,pcb,Item.ingotGold},
				{Item.redstone,advpcb,Item.redstone},
				{Item.redstone,null,Item.redstone}
			});				
			
		
		//entity analyzer
		addRecipe(new ItemStack(component, 1 , 27), new Object[][] {
				{Item.ingotIron,Item.redstone,Item.ingotIron},
				{Item.ingotIron,pcb,Item.ingotIron},
				{Item.ingotIron,Item.ingotIron,Item.ingotIron}
			});

		//empty entity disk
		addRecipe(new ItemStack(component, 1 , 28), new Object[][] {
				{Item.redstone},
				{pcb}
			});	
			
			
			
		//tri-torch
		addRecipe(new ItemStack(component, 1 , 29), new Object[][] {
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
		addRecipe(chestThingy, new Object[][] {
				{planks, planks, planks},
				{wood, planks, wood},
				{planks, planks, planks}
			});	
			
		
		//big chest thingy
		addRecipe(bigChestThingy, new Object[][] {
				{chestThingy, chestThingy},
				{chestThingy, chestThingy}
			});				
			
		//large chest thingy
		addRecipe(hugeChestThingy, new Object[][] {
				{chestThingy,chestThingy,chestThingy},
				{chestThingy,chestThingy,chestThingy},
				{chestThingy,chestThingy,chestThingy}
			});

		//large chest thingy
		addRecipe(chestLock, new Object[][] {
				{Item.ingotIron},
				{Block.stone}
			});			
		addRecipe(chestLock, new Object[][] {
				{Block.stone},
				{Item.ingotIron}
			});	

		//iron chest thingy
		addRecipe(ironChestThingy, new Object[][] {
				{chestThingy,chestThingy,chestThingy},
				{chestThingy,Item.ingotIron,chestThingy},
				{chestThingy,chestThingy,chestThingy}
			});

		//big iron chest thingy
		addRecipe(bigIronChestThingy, new Object[][] {
				{ironChestThingy, ironChestThingy},
				{ironChestThingy, ironChestThingy}
			});				
			
		//large iron chest thingy
		addRecipe(hugeIronChestThingy, new Object[][] {
				{ironChestThingy,ironChestThingy,ironChestThingy},
				{ironChestThingy,ironChestThingy,ironChestThingy},
				{ironChestThingy,ironChestThingy,ironChestThingy}
			});
			
		//dynamic chest thingy
		addRecipe(dynamicChestThingy, new Object[][] {
				{ironChestThingy},
				{Item.redstone}
			});			
		addRecipe(dynamicChestThingy, new Object[][] {
				{Item.redstone},
				{ironChestThingy}
			});	
			
		//big dynamic chest thingy
		addRecipe(bigDynamicChestThingy, new Object[][] {
				{null,dynamicChestThingy, null},
				{dynamicChestThingy,Item.redstone, dynamicChestThingy},
				{null,dynamicChestThingy, null}
			});				
			
		//large dynamic chest thingy
		addRecipe(hugeDynamicChestThingy, new Object[][] {
				{dynamicChestThingy,dynamicChestThingy,dynamicChestThingy},
				{dynamicChestThingy,pcb,dynamicChestThingy},
				{dynamicChestThingy,dynamicChestThingy,dynamicChestThingy}
			});			

		
		ItemStack cleaningfan = new ItemStack(component,1,40);
		ItemStack cleaningtube = new ItemStack(component,2,42);
		
		//cleaning fan		
		addRecipe(cleaningfan, new Object[][] {
				{Block.fenceIron,Item.redstone,Block.fenceIron},
				{Item.redstone,null,Item.redstone},
				{Block.fenceIron,Item.redstone,Block.fenceIron}
			});
			
			
		//cleaning core		
		addRecipe(new ItemStack(component,1,41), new Object[][] {
				{cleaningfan,Item.ingotIron,cleaningfan},
				{cleaningtube,cleaningtube,cleaningtube},
				{Item.ingotIron,cleaningtube,Item.ingotIron}
			});
			
		//cleaning tube
		addRecipe(cleaningtube, new Object[][] {
				{orange,Item.ingotIron,orange},
				{orange,Item.ingotIron,orange},
				{orange,Item.ingotIron,orange}
			});			
		
		//solar panel
		ItemStack solarpanel = new ItemStack(component,1,44);
		addRecipe(solarpanel,new Object[][] {
				{Item.glowstone,Item.redstone},
				{Item.ingotIron,Item.glowstone}
			});
			
		ItemStack magicTingy = new ItemStack(component,1,45);
		
		
		//magic thingy
		addRecipe(magicTingy,new Object[][] {
				{Item.magmaCream,Item.fermentedSpiderEye, Item.magmaCream},
				{Item.ghastTear, Item.eyeOfEnder,Item.ghastTear},
				{Item.magmaCream,Item.fermentedSpiderEye, Item.magmaCream}
			});	

		ItemStack rawmagicmetal = new ItemStack(component,2,46);
		ItemStack magicmetal = new ItemStack(component,1,47);
		
		//magic metal
		addRecipe(rawmagicmetal,new Object[][] {
				{Item.glowstone,Block.blockDiamond, Item.glowstone},
				{magicTingy, Item.glowstone,magicTingy},
				{rawhardmetal,magicTingy,rawhardmetal}
			});		
			
		FurnaceRecipes.smelting().addSmelting(component.itemID, rawmagicmetal.getItemDamage(), magicmetal, 0F);	
		
		
		ItemStack rawmagicmetalthing = new ItemStack(component,1,48);
		ItemStack magicmetalthing = new ItemStack(component,1,49);
		
		addRecipe(rawmagicmetalthing,new Object[][] {
				{rawmagicmetal,rawmagicmetal,rawmagicmetal},
				{rawmagicmetal,rawmagicmetal,rawmagicmetal},
				{rawmagicmetal,rawmagicmetal,rawmagicmetal}				
			});	

		FurnaceRecipes.smelting().addSmelting(component.itemID, rawmagicmetalthing.getItemDamage(), magicmetalthing, 0F);	
		
		
		ItemStack redribbon = new ItemStack(component,1,54);
		ItemStack yellowribbon = new ItemStack(component,1,55);
		
		addRecipe(redribbon,new Object[][] {
				{Item.silk,Item.silk,Item.silk},
				{Item.silk,red,Item.silk},
				{Item.silk,Item.silk,Item.silk}				
			});		

		addRecipe(yellowribbon,new Object[][] {
				{Item.silk,Item.silk,Item.silk},
				{Item.silk,yellow,Item.silk},
				{Item.silk,Item.silk,Item.silk}				
			});	

		addRecipe(new ItemStack(component,1,53),new Object[][] {
				{null,new ItemStack(Block.cloth,1,14),new ItemStack(Block.cloth,1,0)},
				{new ItemStack(Block.cloth,1,14),Item.diamond,new ItemStack(Block.cloth,1,14)},
				{new ItemStack(Block.cloth,1,14),new ItemStack(Block.cloth,1,14),new ItemStack(Block.cloth,1,14)}				
			});		

		addRecipe(new ItemStack(component,1,56),new Object[][] {
				{new ItemStack(Block.cloth,1,14),new ItemStack(Block.cloth,1,14),Item.cookie},
				{new ItemStack(Block.cloth,1,14),new ItemStack(Block.cloth,1,14),Item.bucketMilk},
				{new ItemStack(Block.cloth,1,14),new ItemStack(Block.cloth,1,14),new ItemStack(Block.cloth,1,14)}				
			});

		//advanced panel
		ItemStack advsolarpanel = new ItemStack(component,1,58);
		addRecipe(advsolarpanel,new Object[][] {
				{solarpanel,null,solarpanel},
				{Item.ingotIron, pcb, Item.ingotIron},
				{solarpanel,null,solarpanel}				
			});
		addRecipe(advsolarpanel,new Object[][] {
				{solarpanel,Item.ingotIron,solarpanel},
				{null, pcb, null},
				{solarpanel,Item.ingotIron,solarpanel}				
			});	
			
			
		//blank upgrade
		ItemStack blankupgrade = new ItemStack(component,1,59);
		addRecipe(blankupgrade,new Object[][] {
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
		addRecipe(valve, new Object[][] {
												{null, Item.ingotIron, null},
												{Item.ingotIron, Block.fenceIron, Item.ingotIron},
												{null, Item.ingotIron, null}
		});	
		
		//tank pane
		addRecipe(tankpane, new Object[][] {
												{Block.thinGlass,  Block.thinGlass, Block.thinGlass},
												{Block.glass, Block.thinGlass, Block.glass},
												{Block.thinGlass, Block.thinGlass, Block.thinGlass}
		});	
		
		//large tank pane
		addRecipe(largetankpane, new Object[][] {
												{tankpane,  tankpane},
												{tankpane, tankpane}
		});
		
		//huge tank pane
		addRecipe(hugetankpane, new Object[][] {
												{tankpane,  tankpane, tankpane},
												{tankpane, tankpane, tankpane},
												{tankpane, tankpane, tankpane}
		});
		
		ItemStack cleaningtubeliquid = new ItemStack(component,2,65);
		
		//cleaning core		
		addRecipe(new ItemStack(component,1,64), new Object[][] {
				{cleaningfan,Item.ingotIron,cleaningfan},
				{cleaningtubeliquid,cleaningtubeliquid,cleaningtubeliquid},
				{Item.ingotIron,cleaningtubeliquid,Item.ingotIron}
			});
			
		//cleaning tube
		addRecipe(cleaningtubeliquid, new Object[][] {
				{green,Item.ingotIron,green},
				{green,Item.ingotIron,green},
				{green,Item.ingotIron,green}
			});	


		//Easter Eggs, really, Eggs you paint at Easter. Those kind of Easter Eggs.
		ItemStack easterEggExplosive = new ItemStack(component,1,66);
		ItemStack easterEggBurning = new ItemStack(component,1,67);
		ItemStack easterEggGlistering = new ItemStack(component,1,68);
		ItemStack easterEggChocolate = new ItemStack(component,1,69);


		addRecipe(easterEggExplosive, new Object[][] {
				{Item.gunpowder,Item.gunpowder,Item.gunpowder},
				{Item.gunpowder,Item.egg,Item.gunpowder},
				{Item.gunpowder,green,Item.gunpowder}
			});	

		addRecipe(easterEggBurning, new Object[][] {
				{Item.blazePowder,Item.blazeRod,Item.blazePowder},
				{Item.blazePowder,Item.egg,Item.blazePowder},
				{red,Item.magmaCream,yellow}
			});		

		addRecipe(easterEggGlistering, new Object[][] {
				{Item.goldNugget,Item.goldNugget,Item.goldNugget},
				{Item.goldNugget,Item.egg,Item.goldNugget},
				{Item.goldNugget,blue,Item.goldNugget}
			});	

		ItemStack chocolate = new ItemStack(Item.dyePowder, 1, 3);
			
		addRecipe(easterEggChocolate, new Object[][] {
				{chocolate, Item.sugar, chocolate},
				{chocolate,Item.egg,chocolate},
				{chocolate, Item.sugar, chocolate}
			});	

		ItemStack basket = new ItemStack(component,1,71);
			
		addRecipe(basket, new Object[][] {
				{Item.stick, Item.stick, Item.stick},
				{Item.stick,null,Item.stick},
				{planks, planks, planks}
			});
		
		
		//wood
		for (int i = 0; i < 4; i++) {
			addRecipe(new ItemStack(Block.planks, 2, i), new Object[][] {
					{ItemCartComponent.getWood(i, true)}
				});	
			
			addRecipe(new ItemStack(Item.stick, 2), new Object[][] {
					{ItemCartComponent.getWood(i, false)}
				});					
		}
		
		
		//higher tier saw heads
		addRecipe(new ItemStack(component, 1 , 80),new Object[][] {
								{Item.ingotIron,Item.ingotIron, hardmetal}
							});				
		addRecipe(new ItemStack(component, 1 , 81),new Object[][] {
								{Item.ingotIron,Item.ingotIron, magicmetal}
							});				
		
		//galgadorian wheels
		addRecipe(new ItemStack(component, 1 , 82),new Object[][] {
			{null, hardmetal, null},
			{hardmetal, magicmetal, hardmetal},
			{null, hardmetal, null}
		});			
		
		
		ItemStack ironblade = new ItemStack(component, 8 , 83);
		
		//iron blade
		addRecipe(ironblade,new Object[][] {
			{null, Item.shears, null},
			{Item.ingotIron, Item.ingotIron, Item.ingotIron},
			{null, Item.ingotIron, null}
		});		
		
		
		//blade arm
		addRecipe(new ItemStack(component, 1 , 84),new Object[][] {
			{ironblade, null, ironblade},
			{null, hardmetal, null},
			{ironblade, null, ironblade}
		});			
		
		
		//cargo manager
		addRecipe(new ItemStack(blockCargoManager, 1 ), new Object[][] {
				{bigIronChestThingy, hugeIronChestThingy, bigIronChestThingy},
				{hugeIronChestThingy, bigDynamicChestThingy, hugeIronChestThingy},
				{bigIronChestThingy, hugeIronChestThingy, bigIronChestThingy}
		});		
		
		
		//activator
		addRecipe(new ItemStack(blockActivator, 1 ), new Object[][] {
				{orange, Item.ingotGold, blue},
				{Block.stone, Item.ingotIron, Block.stone},
				{Item.redstone, advpcb, Item.redstone}
		});	
		
		//distributor
		addRecipe(new ItemStack(blockDistributor, 1 ), new Object[][] {
				{Block.stone, pcb, Block.stone},
				{pcb, Item.redstone, pcb},
				{Block.stone, pcb, Block.stone}
		});			
		
		//cart assembler
		addRecipe(new ItemStack(blockAssembler, 1 ), new Object[][] {
				{Item.ingotIron, Block.stone, Item.ingotIron},
				{Block.stone, Item.ingotIron, Block.stone},
				{pcb, Block.stone, pcb}
		});	

		//junction rail
		addRecipe(new ItemStack(blockJunction, 1 ), new Object[][] {
				{null,Item.redstone,null},
				{Item.redstone,Block.rail,Item.redstone},
				{null,Item.redstone,null}
		});			
		

		//adv detector rail
		addRecipe(new ItemStack(blockAdvDetector, 2), new Object[][] {
				{Item.ingotIron,Block.pressurePlateStone,Item.ingotIron},
				{Item.ingotIron, Item.redstone,Item.ingotIron},
				{Item.ingotIron,Block.pressurePlateStone,Item.ingotIron}
		});	
		
		/** === detector managers === **/		
		//detector unit
		ItemStack unit = new ItemStack(blockDetector, 1 , 1);
		addRecipe(unit, new Object[][] {
				{Block.cobblestone, Block.pressurePlateStone, Block.cobblestone},
				{Item.ingotIron, pcb , Item.ingotIron},
				{Block.cobblestone, Item.redstone, Block.cobblestone}
		});				
		//detector manager
		addRecipe(new ItemStack(blockDetector, 1 , 0), new Object[][] {
			{pcb},
			{unit}
		});		
		//detector station
		addRecipe(new ItemStack(blockDetector, 1 , 2), new Object[][] {
			{Item.ingotIron, Item.ingotIron, Item.ingotIron},
			{null, unit, null},
			{null, pcb, null}
		});	
		//detector junction
		addRecipe(new ItemStack(blockDetector, 1 , 3), new Object[][] {
			{Block.torchRedstoneActive, null, Block.torchRedstoneActive},
			{Item.redstone, unit, Item.redstone},
			{null, pcb, null}
		});	
		//detector redstone
		addRecipe(new ItemStack(blockDetector, 1 , 4), new Object[][] {
			{Item.redstone, Item.redstone,Item.redstone},
			{Item.redstone, unit, Item.redstone},
			{Item.redstone, Item.redstone, Item.redstone}
		});			
		/** **/
		
		
		//liquid manager
		addRecipe(new ItemStack(blockLiquidManager, 1 ), new Object[][] {
				{advtank, Item.ingotIron, advtank},
				{Item.ingotIron, valve, Item.ingotIron},
				{advtank, Item.ingotIron, advtank}
		});		
		
		//storage blocks
		ItemBlockStorage.loadRecipes();
	}

	public static void addRecipe(ItemStack item, Object[][] recipe) {
		if (recipe != null && item != null) {
			if (item.getItem() == component && !component.isValid(item)) {
				return;
			}
		
			java.util.ArrayList usedItems = new java.util.ArrayList();
			String chars = "ABCDEFGHI";
			String[] parts = new String[recipe.length];
			boolean isOreDict = false;
			boolean isSpecial = false;
			ItemStack[] items = new ItemStack[recipe.length * recipe[0].length];
			for (int i = 0;i < recipe.length; i++) { 
				parts[i] = "";
				for (int j = 0;j < recipe[i].length; j++) { 
					Object obj = recipe[i][j];
					
					boolean valid = true;
					if (obj instanceof Item) {
						obj = new ItemStack((Item)obj,1);
					}else if (obj instanceof Block) {
						obj = new ItemStack((Block)obj,1);
					}else if (obj instanceof ItemStack) {
						obj = ((ItemStack)obj).copy();
						((ItemStack)obj).stackSize = 1;
						if (obj != null && ((ItemStack)obj).getItem() instanceof ItemEnchantedBook) {
							isSpecial = true;
						}
					}else if(obj instanceof String) {
						isOreDict = true;
					}else {
						valid = false;
					}
					
					if (obj instanceof ItemStack) {
						items[j + i*recipe[i].length] = (ItemStack)obj;
					}
					char myChar;
					if (valid) {
						int ind = -1;
						for (int k = 0; k < usedItems.size();k++) {
							if ((usedItems.get(k) instanceof ItemStack && 
							obj instanceof ItemStack && 
							((ItemStack)usedItems.get(k)).isItemEqual((ItemStack)obj)) 
							||
							(usedItems.get(k) instanceof String && 
							obj instanceof String && 
							((String)usedItems.get(k)).equals((String)obj)) 							
							)
							{
								ind = k;
								break;
							}
						}
						
						if (ind == -1) {
							usedItems.add(obj);
							ind = usedItems.size()-1;
						}
						
						myChar = chars.charAt(ind);
																
					}else {
						myChar = ' ';
					}
					
					parts[i] += myChar;
				}
			}
			
			Object[] finalRecipe = new Object[parts.length + usedItems.size()*2];
			for(int i = 0; i < parts.length; i++) {
				finalRecipe[i] = parts[i];
			}			
			for (int i = 0; i < usedItems.size();i++) {
				finalRecipe[parts.length+i*2] = chars.charAt(i);
				finalRecipe[parts.length+i*2+1] = usedItems.get(i);
			}
			if (isSpecial) {
				GameRegistry.addRecipe(new ShapedRecipes2(recipe[0].length, recipe.length, items, item));
			}else if (isOreDict) {
				GameRegistry.addRecipe(new ShapedOreRecipe(item, finalRecipe));
			}else{
				GameRegistry.addRecipe(item,finalRecipe);
			}
			
			
		}		

	}	




}
