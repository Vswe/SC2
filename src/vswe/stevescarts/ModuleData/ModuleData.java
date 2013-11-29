package vswe.stevescarts.ModuleData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import vswe.stevescarts.Modules.Workers.*;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.CartVersion;
import vswe.stevescarts.Helpers.ColorHelper;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Models.Cart.ModelAdvancedTank;
import vswe.stevescarts.Models.Cart.ModelBridge;
import vswe.stevescarts.Models.Cart.ModelCage;
import vswe.stevescarts.Models.Cart.ModelCake;
import vswe.stevescarts.Models.Cart.ModelCartbase;
import vswe.stevescarts.Models.Cart.ModelCleaner;
import vswe.stevescarts.Models.Cart.ModelCompactSolarPanel;
import vswe.stevescarts.Models.Cart.ModelDrill;
import vswe.stevescarts.Models.Cart.ModelDynamite;
import vswe.stevescarts.Models.Cart.ModelEggBasket;
import vswe.stevescarts.Models.Cart.ModelEngineFrame;
import vswe.stevescarts.Models.Cart.ModelEngineInside;
import vswe.stevescarts.Models.Cart.ModelExtractingChests;
import vswe.stevescarts.Models.Cart.ModelFarmer;
import vswe.stevescarts.Models.Cart.ModelFrontChest;
import vswe.stevescarts.Models.Cart.ModelFrontTank;
import vswe.stevescarts.Models.Cart.ModelGiftStorage;
import vswe.stevescarts.Models.Cart.ModelGun;
import vswe.stevescarts.Models.Cart.ModelHull;
import vswe.stevescarts.Models.Cart.ModelHullTop;
import vswe.stevescarts.Models.Cart.ModelLawnMower;
import vswe.stevescarts.Models.Cart.ModelLever;
import vswe.stevescarts.Models.Cart.ModelLiquidDrainer;
import vswe.stevescarts.Models.Cart.ModelLiquidSensors;
import vswe.stevescarts.Models.Cart.ModelMobDetector;
import vswe.stevescarts.Models.Cart.ModelNote;
import vswe.stevescarts.Models.Cart.ModelPigHead;
import vswe.stevescarts.Models.Cart.ModelPigHelmet;
import vswe.stevescarts.Models.Cart.ModelPigTail;
import vswe.stevescarts.Models.Cart.ModelPumpkinHull;
import vswe.stevescarts.Models.Cart.ModelPumpkinHullTop;
import vswe.stevescarts.Models.Cart.ModelRailer;
import vswe.stevescarts.Models.Cart.ModelSeat;
import vswe.stevescarts.Models.Cart.ModelShield;
import vswe.stevescarts.Models.Cart.ModelShootingRig;
import vswe.stevescarts.Models.Cart.ModelSideChests;
import vswe.stevescarts.Models.Cart.ModelSideTanks;
import vswe.stevescarts.Models.Cart.ModelSniperRifle;
import vswe.stevescarts.Models.Cart.ModelSolarPanelBase;
import vswe.stevescarts.Models.Cart.ModelSolarPanelHeads;
import vswe.stevescarts.Models.Cart.ModelToolPlate;
import vswe.stevescarts.Models.Cart.ModelTopChest;
import vswe.stevescarts.Models.Cart.ModelTopTank;
import vswe.stevescarts.Models.Cart.ModelTorchplacer;
import vswe.stevescarts.Models.Cart.ModelTrackRemover;
import vswe.stevescarts.Models.Cart.ModelWheel;
import vswe.stevescarts.Models.Cart.ModelWoodCutter;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.ModuleSmelterAdv;
import vswe.stevescarts.Modules.Addons.ModuleAddon;
import vswe.stevescarts.Modules.Addons.ModuleBrake;
import vswe.stevescarts.Modules.Addons.ModuleChunkLoader;
import vswe.stevescarts.Modules.Addons.ModuleColorizer;
import vswe.stevescarts.Modules.Addons.ModuleCrafter;
import vswe.stevescarts.Modules.Addons.ModuleCrafterAdv;
import vswe.stevescarts.Modules.Addons.ModuleCreativeIncinerator;
import vswe.stevescarts.Modules.Addons.ModuleCreativeSupplies;
import vswe.stevescarts.Modules.Addons.ModuleDrillIntelligence;
import vswe.stevescarts.Modules.Addons.ModuleEnchants;
import vswe.stevescarts.Modules.Addons.ModuleHeightControl;
import vswe.stevescarts.Modules.Addons.ModuleIncinerator;
import vswe.stevescarts.Modules.Addons.ModuleInvisible;
import vswe.stevescarts.Modules.Addons.ModuleLabel;
import vswe.stevescarts.Modules.Addons.ModuleLiquidSensors;
import vswe.stevescarts.Modules.Addons.ModuleMelter;
import vswe.stevescarts.Modules.Addons.ModuleMelterExtreme;
import vswe.stevescarts.Modules.Addons.ModuleOreTracker;
import vswe.stevescarts.Modules.Addons.ModulePowerObserver;
import vswe.stevescarts.Modules.Addons.ModuleShield;
import vswe.stevescarts.Modules.Addons.ModuleSmelter;
import vswe.stevescarts.Modules.Addons.ModuleSnowCannon;
import vswe.stevescarts.Modules.Addons.Mobdetectors.ModuleAnimal;
import vswe.stevescarts.Modules.Addons.Mobdetectors.ModuleBat;
import vswe.stevescarts.Modules.Addons.Mobdetectors.ModuleMonster;
import vswe.stevescarts.Modules.Addons.Mobdetectors.ModulePlayer;
import vswe.stevescarts.Modules.Addons.Mobdetectors.ModuleVillager;
import vswe.stevescarts.Modules.Addons.Plants.ModuleModTrees;
import vswe.stevescarts.Modules.Addons.Plants.ModuleNetherwart;
import vswe.stevescarts.Modules.Addons.Plants.ModulePlantSize;
import vswe.stevescarts.Modules.Addons.Projectiles.ModuleCake;
import vswe.stevescarts.Modules.Addons.Projectiles.ModuleEgg;
import vswe.stevescarts.Modules.Addons.Projectiles.ModuleFireball;
import vswe.stevescarts.Modules.Addons.Projectiles.ModulePotion;
import vswe.stevescarts.Modules.Addons.Projectiles.ModuleSnowball;
import vswe.stevescarts.Modules.Engines.ModuleCheatEngine;
import vswe.stevescarts.Modules.Engines.ModuleCoalStandard;
import vswe.stevescarts.Modules.Engines.ModuleCoalTiny;
import vswe.stevescarts.Modules.Engines.ModuleEngine;
import vswe.stevescarts.Modules.Engines.ModuleSolarBasic;
import vswe.stevescarts.Modules.Engines.ModuleSolarCompact;
import vswe.stevescarts.Modules.Engines.ModuleSolarStandard;
import vswe.stevescarts.Modules.Engines.ModuleThermalAdvanced;
import vswe.stevescarts.Modules.Engines.ModuleThermalStandard;
import vswe.stevescarts.Modules.Hull.ModuleCheatHull;
import vswe.stevescarts.Modules.Hull.ModuleGalgadorian;
import vswe.stevescarts.Modules.Hull.ModuleHull;
import vswe.stevescarts.Modules.Hull.ModulePig;
import vswe.stevescarts.Modules.Hull.ModulePumpkin;
import vswe.stevescarts.Modules.Hull.ModuleReinforced;
import vswe.stevescarts.Modules.Hull.ModuleStandard;
import vswe.stevescarts.Modules.Hull.ModuleWood;
import vswe.stevescarts.Modules.Realtimers.ModuleAdvControl;
import vswe.stevescarts.Modules.Realtimers.ModuleArcade;
import vswe.stevescarts.Modules.Realtimers.ModuleCage;
import vswe.stevescarts.Modules.Realtimers.ModuleCakeServer;
import vswe.stevescarts.Modules.Realtimers.ModuleCakeServerDynamite;
import vswe.stevescarts.Modules.Realtimers.ModuleCleaner;
import vswe.stevescarts.Modules.Realtimers.ModuleDynamite;
import vswe.stevescarts.Modules.Realtimers.ModuleExperience;
import vswe.stevescarts.Modules.Realtimers.ModuleFirework;
import vswe.stevescarts.Modules.Realtimers.ModuleFlowerRemover;
import vswe.stevescarts.Modules.Realtimers.ModuleMilker;
import vswe.stevescarts.Modules.Realtimers.ModuleNote;
import vswe.stevescarts.Modules.Realtimers.ModuleSeat;
import vswe.stevescarts.Modules.Realtimers.ModuleShooter;
import vswe.stevescarts.Modules.Realtimers.ModuleShooterAdv;
import vswe.stevescarts.Modules.Storages.ModuleStorage;
import vswe.stevescarts.Modules.Storages.Chests.ModuleEggBasket;
import vswe.stevescarts.Modules.Storages.Chests.ModuleExtractingChests;
import vswe.stevescarts.Modules.Storages.Chests.ModuleFrontChest;
import vswe.stevescarts.Modules.Storages.Chests.ModuleGiftStorage;
import vswe.stevescarts.Modules.Storages.Chests.ModuleInternalStorage;
import vswe.stevescarts.Modules.Storages.Chests.ModuleSideChests;
import vswe.stevescarts.Modules.Storages.Chests.ModuleTopChest;
import vswe.stevescarts.Modules.Storages.Tanks.ModuleAdvancedTank;
import vswe.stevescarts.Modules.Storages.Tanks.ModuleCheatTank;
import vswe.stevescarts.Modules.Storages.Tanks.ModuleFrontTank;
import vswe.stevescarts.Modules.Storages.Tanks.ModuleInternalTank;
import vswe.stevescarts.Modules.Storages.Tanks.ModuleOpenTank;
import vswe.stevescarts.Modules.Storages.Tanks.ModuleSideTanks;
import vswe.stevescarts.Modules.Storages.Tanks.ModuleTopTank;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrillDiamond;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrillGalgadorian;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrillHardened;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrillIron;
import vswe.stevescarts.Modules.Workers.Tools.ModuleFarmerDiamond;
import vswe.stevescarts.Modules.Workers.Tools.ModuleFarmerGalgadorian;
import vswe.stevescarts.Modules.Workers.Tools.ModuleTool;
import vswe.stevescarts.Modules.Workers.Tools.ModuleWoodcutterDiamond;
import vswe.stevescarts.Modules.Workers.Tools.ModuleWoodcutterGalgadorian;
import vswe.stevescarts.Modules.Workers.Tools.ModuleWoodcutterHardened;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
public class ModuleData {


	private static HashMap<Byte,ModuleData> moduleList;		
	private static Class[] moduleGroups;
	private static String[] moduleGroupNames;
	
	public static HashMap<Byte,ModuleData> getList() {
		return moduleList;
	}
	
	public static Collection<ModuleData> getModules() {
		return getList().values();
	}
	
	public static void init() {
		String planks = "plankWood"; //ore dict
		String wood = "logWood"; //ore dict
		
		ItemStack woodSingleSlab = new ItemStack(Block.woodSingleSlab, 1, -1);
	
		ItemStack woodenWheels = new ItemStack(StevesCarts.instance.component, 1 , 0);
		ItemStack ironWheels = new ItemStack(StevesCarts.instance.component, 1 , 1);
	
		ItemStack redComponent = new ItemStack(StevesCarts.instance.component, 1 , 2);
		ItemStack greenComponent = new ItemStack(StevesCarts.instance.component, 1 , 3);
		ItemStack blueComponent = new ItemStack(StevesCarts.instance.component, 1 , 4);	
	
		ItemStack magicGlass = new ItemStack(StevesCarts.instance.component, 1 , 5);	
		ItemStack dynamite = new ItemStack(StevesCarts.instance.component, 1 , 6);	
		
		ItemStack smalltank = new ItemStack(StevesCarts.instance.component, 1 , 7);	
		ItemStack bigtank = new ItemStack(StevesCarts.instance.component, 1 , 8);	
		
		ItemStack pcb = new ItemStack(StevesCarts.instance.component, 1 , 9);
		
		ItemStack screen = new ItemStack(StevesCarts.instance.component, 1 , 10);
		ItemStack handle = new ItemStack(StevesCarts.instance.component, 1 , 12);
		ItemStack speedhandle = new ItemStack(StevesCarts.instance.component, 1 , 13);
		ItemStack wheel = new ItemStack(StevesCarts.instance.component, 1 , 14);
		
		ItemStack saw = new ItemStack(StevesCarts.instance.component, 1 , 15);
		ItemStack advpcb = new ItemStack(StevesCarts.instance.component, 1 , 16);
		ItemStack woodcore = new ItemStack(StevesCarts.instance.component, 1 , 17);
		
		ItemStack hardstuff = new ItemStack(StevesCarts.instance.component, 1 , 19);
		ItemStack hardmesh = new ItemStack(StevesCarts.instance.component, 1 , 20);
		
		ItemStack refinedMetal = new ItemStack(StevesCarts.instance.component, 1 , 22);
		ItemStack refinedWheels = new ItemStack(StevesCarts.instance.component, 1 , 23);
		
		ItemStack pipe = new ItemStack(StevesCarts.instance.component, 1 , 24);
		ItemStack shootingcore = new ItemStack(StevesCarts.instance.component, 1 , 25);

		ItemStack mobdetector = new ItemStack(StevesCarts.instance.component, 1 , 26);
		ItemStack analyzer = new ItemStack(StevesCarts.instance.component, 1 , 27);
		ItemStack disk = new ItemStack(StevesCarts.instance.component, 1 , 28);
		
		ItemStack tritorch = new ItemStack(StevesCarts.instance.component, 1, 29);
		
		ItemStack chestThingy = new ItemStack(StevesCarts.instance.component, 1 , 30);	
		ItemStack bigChestThingy = new ItemStack(StevesCarts.instance.component, 1 , 31);	
		ItemStack hugeChestThingy = new ItemStack(StevesCarts.instance.component, 1 , 32);	
		ItemStack chestLock = new ItemStack(StevesCarts.instance.component, 1 , 33);
		ItemStack ironChestThingy = new ItemStack(StevesCarts.instance.component, 1 , 34);
		ItemStack bigIronChestThingy = new ItemStack(StevesCarts.instance.component, 1 , 35);
		ItemStack hugeIronChestThingy = new ItemStack(StevesCarts.instance.component, 1 , 36);	
		ItemStack dynamicChestThingy = new ItemStack(StevesCarts.instance.component, 1 , 37);	
		ItemStack bigDynamicChestThingy = new ItemStack(StevesCarts.instance.component, 1 , 38);
		ItemStack hugeDynamicChestThingy = new ItemStack(StevesCarts.instance.component, 1 , 39);		
		
		ItemStack cleaningcore = new ItemStack(StevesCarts.instance.component, 1 , 41);
		ItemStack cleaningtube = new ItemStack(StevesCarts.instance.component, 1 , 42);
		
		ItemStack fuse = new ItemStack(StevesCarts.instance.component, 12, 43);	
		
		ItemStack solarpanel = new ItemStack(StevesCarts.instance.component, 1 , 44);
		
		ItemStack magicTingy = new ItemStack(StevesCarts.instance.component,1,45);
		ItemStack magicmetal = new ItemStack(StevesCarts.instance.component, 1 , 47);		
		ItemStack magicmetalthingy = new ItemStack(StevesCarts.instance.component, 1 , 49);
		
		ItemStack bonemeal = new ItemStack(Item.dyePowder, 1, 15);
		
		ItemStack greenwrapping = new ItemStack(StevesCarts.instance.component, 1, 51);
		ItemStack redwrapping = new ItemStack(StevesCarts.instance.component, 1, 52);
		ItemStack redribbon = new ItemStack(StevesCarts.instance.component, 1, 54);
		ItemStack yellowribbon = new ItemStack(StevesCarts.instance.component, 1, 55);
		ItemStack filledsock = new ItemStack(StevesCarts.instance.component, 1, 57);
		
		ItemStack advsolarpanel = new ItemStack(StevesCarts.instance.component, 1, 58);
		
		ItemStack valve = new ItemStack(StevesCarts.instance.component, 1, 60);
		ItemStack tankpane = new ItemStack(StevesCarts.instance.component, 1, 61);
		ItemStack largetankpane = new ItemStack(StevesCarts.instance.component, 1, 62);
		ItemStack hugetankpane = new ItemStack(StevesCarts.instance.component, 1, 63);
		
		ItemStack cleaningcoreliquid = new ItemStack(StevesCarts.instance.component, 1 , 64);
		ItemStack cleaningtubeliquid = new ItemStack(StevesCarts.instance.component, 1 , 65);		
		
		ItemStack [] easterEggs = new ItemStack [] { 
			new ItemStack(StevesCarts.instance.component, 1 , 66),
			new ItemStack(StevesCarts.instance.component, 1 , 67),
			new ItemStack(StevesCarts.instance.component, 1 , 68),
			new ItemStack(StevesCarts.instance.component, 1 , 69)
			};
		ItemStack basket = new ItemStack(StevesCarts.instance.component, 1 , 71);	

		ItemStack hardenedsaw = new ItemStack(StevesCarts.instance.component, 1 , 80);	
		ItemStack galgsaw = new ItemStack(StevesCarts.instance.component, 1 , 81);	
		ItemStack galgWheels = new ItemStack(StevesCarts.instance.component, 1 , 82);	
		
		ItemStack bladearm = new ItemStack(StevesCarts.instance.component, 1 , 84);	
		
		moduleGroups = new Class[] {ModuleHull.class, ModuleEngine.class, ModuleTool.class,ModuleStorage.class, ModuleAddon.class};
		moduleGroupNames = new String[] {"Hull", "Engine", "Tool", "Storage", "Addon", "Attachment"};
	
		moduleList = new HashMap<Byte,ModuleData> ();
		
		ModuleDataGroup engineGroup = new ModuleDataGroup("Engine");
		
		ModuleData coalStandard = new ModuleData(0, "Coal Engine", ModuleCoalStandard.class,15)/*.addSide(SIDE.BACK)*/			
			.addRecipe(new Object[][] {
				{Item.ingotIron,Item.ingotIron,Item.ingotIron},
				{Item.ingotIron,Block.furnaceIdle,Item.ingotIron},
				{Block.pistonBase,null,Block.pistonBase}
			});		
			
		ModuleData coalTiny = new ModuleData(44, "Tiny Coal Engine", ModuleCoalTiny.class,2)/*.addSide(SIDE.BACK)*/
			.addRecipe(new Object[][] {
				{Item.ingotIron,Block.furnaceIdle,Item.ingotIron},
				{null,Block.pistonBase,null}
			});	
			
		addNemesis(coalTiny, coalStandard);

		
		ModuleData solar1 = new ModuleData(1, "Solar Engine", ModuleSolarStandard.class, 20).addSides(new SIDE[] {SIDE.CENTER, SIDE.TOP})	
			.removeModel("Top")		
			.addRecipe(new Object[][] {
				{Item.ingotIron,solarpanel,Item.ingotIron},
				{solarpanel,advpcb,solarpanel},
				{Block.pistonBase,solarpanel,Block.pistonBase}
			});			
		
		ModuleData solar0 = new ModuleData(45, "Basic Solar Engine", ModuleSolarBasic.class, 12).addSides(new SIDE[] {SIDE.CENTER, SIDE.TOP})
			.removeModel("Top")			
			.addRecipe(new Object[][] {
				{solarpanel,Item.ingotIron,solarpanel},
				{Item.ingotIron,pcb,Item.ingotIron},
				{null,Block.pistonBase,null}
			});

		
		ModuleData compactsolar = new ModuleData(56, "Compact Solar Engine", ModuleSolarCompact.class, 32).addSides(new SIDE[] {SIDE.RIGHT, SIDE.LEFT})
			.addRecipe(new Object[][] {
				{advsolarpanel,Item.ingotIron,advsolarpanel},
				{advpcb,Item.redstone, advpcb},
				{Block.pistonBase,Item.ingotIron,Block.pistonBase}
			});		

		
		
		new ModuleData(2, "Side Chests", ModuleSideChests.class,3).addSides(new SIDE[] {SIDE.RIGHT, SIDE.LEFT})
			.addRecipe(new Object[][] {
				{hugeChestThingy,chestThingy, hugeChestThingy},
				{bigChestThingy,chestLock,bigChestThingy},
				{hugeChestThingy,chestThingy,hugeChestThingy}
			});			
		new ModuleData(3, "Top Chest", ModuleTopChest.class,6).addSide(SIDE.TOP)
			.addRecipe(new Object[][] {
				{hugeChestThingy,hugeChestThingy, hugeChestThingy},
				{chestThingy,chestLock,chestThingy},
				{hugeChestThingy,hugeChestThingy,hugeChestThingy}
			});			
		ModuleData frontChest = new ModuleData(4, "Front Chest", ModuleFrontChest.class,5).addSide(SIDE.FRONT)
			.addRecipe(new Object[][] {
				{chestThingy,bigChestThingy, chestThingy},
				{chestThingy,chestLock,chestThingy},
				{bigChestThingy,bigChestThingy,bigChestThingy}
			});	
		
			
		new ModuleData(5, "Internal Storage", ModuleInternalStorage.class,25).setAllowDuplicate()			
			.addRecipe(new Object[][] {
				{chestThingy,chestThingy, chestThingy},
				{chestThingy,chestLock,chestThingy},
				{chestThingy,chestThingy,chestThingy}
			});		
		new ModuleData(6, "Extracting Chests", ModuleExtractingChests.class, 75).addSides(new SIDE[] {SIDE.CENTER, SIDE.RIGHT, SIDE.LEFT})
			.addRecipe(new Object[][] {
				{hugeIronChestThingy, hugeIronChestThingy, hugeIronChestThingy},
				{bigIronChestThingy,chestLock,bigIronChestThingy},
				{hugeDynamicChestThingy,bigDynamicChestThingy,hugeDynamicChestThingy}
			});		
			
		new ModuleData(7, "Torch Placer", ModuleTorch.class,14).addSides(new SIDE[] {SIDE.RIGHT, SIDE.LEFT})
			.addRecipe(new Object[][] {
				{tritorch,null, tritorch},
				{Item.ingotIron,null,Item.ingotIron},
				{Item.ingotIron,Item.ingotIron,Item.ingotIron}
			});				
		
		
		
		ModuleData drill = new ModuleDataTool(8, "Basic Drill", ModuleDrillDiamond.class,10, false).addSide(SIDE.FRONT)	
			.addRecipe(new Object[][] {{Item.ingotIron, Item.diamond,  null},
							   {null, Item.ingotIron,  Item.diamond},
							   {Item.ingotIron,  Item.diamond, null}
							  });

		ModuleData ironDrill = new ModuleDataTool(42, "Iron Drill", ModuleDrillIron.class,3, false).addSide(SIDE.FRONT)			
			.addRecipe(new Object[][] {{Item.ingotIron, Item.ingotIron,  null},
									   {null, Item.ingotIron,  Item.ingotIron},
									   {Item.ingotIron,  Item.ingotIron, null}
									  });
						  
		ModuleData hardeneddrill = new ModuleDataTool(43, "Hardened Drill", ModuleDrillHardened.class,45, false).addSide(SIDE.FRONT)
			.addRecipe(new Object[][] {{hardmesh, refinedMetal,  null},
							   {Block.blockDiamond, drill.getItemStack(), refinedMetal},
							   {hardmesh,  refinedMetal, null}
							  });	
							  
		ModuleData galgdrill = new ModuleDataTool(9, "Galgadorian Drill", ModuleDrillGalgadorian.class,150, true).addSide(SIDE.FRONT)		
			.addRecipe(new Object[][] {{magicmetal, magicmetalthingy,  null},
							   {Block.blockDiamond, hardeneddrill.getItemStack(), magicmetalthingy},
							   {magicmetal,  magicmetalthingy, null}
							  });	

		ModuleDataGroup drillGroup = new ModuleDataGroup("Drill");
		drillGroup.add(drill);
		drillGroup.add(ironDrill);
		drillGroup.add(hardeneddrill);
		drillGroup.add(galgdrill);
		
		ModuleData railer = new ModuleData(10, "Railer", ModuleRailer.class,3)/*.addSide(SIDE.TOP)*/	
			.addRecipe(new Object[][] {{Block.stone,Block.stone,Block.stone},
										{Item.ingotIron,Block.rail,Item.ingotIron},
										{Block.stone,Block.stone,Block.stone}
									  });											  	
		ModuleData largerailer = new ModuleData(11, "Large Railer", ModuleRailerLarge.class,17)/*.addSide(SIDE.TOP)*/			
			.addRecipe(new Object[][] {{Item.ingotIron,Item.ingotIron,Item.ingotIron},
										{railer.getItemStack(),Block.rail,railer.getItemStack()},
										{Item.ingotIron,Item.ingotIron,Item.ingotIron}
									  });				
		addNemesis(railer, largerailer);

		
		
		new ModuleData(12, "Bridge Builder", ModuleBridge.class,14)	
			.addRecipe(new Object[][] {
				{null,Item.redstone,null},
				{Block.brick,pcb,Block.brick},
				{null,Block.pistonBase,null}
			});			
		new ModuleData(13, "Track Remover", ModuleRemover.class,8).addSides(new SIDE[] {SIDE.TOP, SIDE.BACK})	
			.addRecipe(new Object[][] {	{Item.ingotIron,Item.ingotIron,Item.ingotIron},
										{Item.ingotIron,null,Item.ingotIron},
										{Item.ingotIron,null,null}	
									});
			
		ModuleDataGroup farmerGroup = new ModuleDataGroup("Farmer");
		
		ModuleData farmerbasic = new ModuleDataTool(14, "Basic Farmer", ModuleFarmerDiamond.class,36, false).addSide(SIDE.FRONT)
			.addRecipe(new Object[][] {	{Item.diamond,Item.diamond,Item.diamond},
										{null,Item.ingotIron,null},
										{pcb,Item.ingotGold,pcb}	
									});			
		
		ModuleData farmergalg = new ModuleDataTool(84, "Galgadorian Farmer", ModuleFarmerGalgadorian.class,55, true).addSide(SIDE.FRONT)
				.addRecipe(new Object[][] {	{magicmetal,magicmetal,magicmetal},
											{null,refinedMetal,null},
											{advpcb,Item.ingotGold,advpcb}	
										})
				.addRecipe(new Object[][] {	{magicmetal,magicmetal,magicmetal},
						{null,farmerbasic.getItemStack(),null},
						{null,pcb,null}	
					});			
		
		farmerGroup.add(farmerbasic);
		farmerGroup.add(farmergalg);
	
		ModuleDataGroup woodcutterGroup = new ModuleDataGroup("Wood Cutter");
		
		ModuleData woodcutter = new ModuleDataTool(15, "Basic Wood Cutter", ModuleWoodcutterDiamond.class,34, false).addSide(SIDE.FRONT)		
			.addRecipe(new Object[][] {	{saw,saw,saw},
										{saw,Item.ingotIron,saw},
										{null,woodcore,null}
									});	
		
		ModuleData woodcutterHardened = new ModuleDataTool(79, "Hardened Wood Cutter", ModuleWoodcutterHardened.class, 65, false).addSide(SIDE.FRONT)	
		.addRecipe(new Object[][] {	{hardenedsaw,hardenedsaw,hardenedsaw},
									{hardenedsaw,Item.diamond,hardenedsaw},
									{null,woodcore,null}
								})
		.addRecipe(new Object[][] {	{refinedMetal,refinedMetal,refinedMetal},
									{refinedMetal,Item.ingotIron,refinedMetal},
									{null,woodcutter.getItemStack(),null}
								});									
		
		ModuleData woodcutterGalgadorian  = new ModuleDataTool(80, "Galgadorian Wood Cutter", ModuleWoodcutterGalgadorian.class,120, true).addSide(SIDE.FRONT)	
		.addRecipe(new Object[][] {	{galgsaw,galgsaw,galgsaw},
									{galgsaw,refinedMetal,galgsaw},
									{null,woodcore,null}
								})
		.addRecipe(new Object[][] {	{magicmetal,magicmetal,magicmetal},
									{magicmetal,Item.ingotIron,magicmetal},
									{null,woodcutterHardened.getItemStack(),null}
								});	
		
		
		woodcutterGroup.add(woodcutter);
		woodcutterGroup.add(woodcutterHardened);
		woodcutterGroup.add(woodcutterGalgadorian);
		
		ModuleDataGroup tankGroup = new ModuleDataGroup("Tank");
		new ModuleData(16, "Hydrator", ModuleHydrater.class,6).addRequirement(tankGroup)		
			.addRecipe(new Object[][] {	{Item.ingotIron,Item.glassBottle,Item.ingotIron},
										{null,Block.fenceIron,null},
									});				

		
		new ModuleData(18, "Fertilizer", ModuleFertilizer.class,10)	
			.addRecipe(new Object[][] {	{bonemeal,null,bonemeal},
										{Item.glassBottle,Item.leather,Item.glassBottle},
										{Item.leather,pcb,Item.leather}
									});		


		new ModuleData(19, "Height Controller", ModuleHeightControl.class, 20)
			.addRecipe(new Object[][] {
				{null,Item.compass,null},
				{Item.paper,pcb,Item.paper},
				{Item.paper,Item.paper,Item.paper}
			});		
		ModuleData liquidsensors = new ModuleData(20, "Liquid Sensors", ModuleLiquidSensors.class,27).addRequirement(drillGroup)
			.addRecipe(new Object[][] {
				{Item.redstone,null,Item.redstone},
				{Item.bucketLava,Item.diamond,Item.bucketWater},
				{Item.ingotIron,advpcb,Item.ingotIron}
			});			
		
									
		ModuleData seat = new ModuleData(25, "Seat", ModuleSeat.class,3).addSides(new SIDE[] {SIDE.CENTER, SIDE.TOP})		
			.addRecipe(new Object[][] {{null, planks},
									   {null, planks},
									   {woodSingleSlab, planks}
									  });
									  
		new ModuleData(26, "Brake Handle", ModuleBrake.class,12).addSide(SIDE.RIGHT).addParent(seat)
			.addRecipe(new Object[][] {{null, null, new ItemStack(Item.dyePowder, 1, 1)},
									   {Item.ingotIron, handle, null},
									   {Item.redstone, Item.ingotIron, null}
									  });
									  
		new ModuleData(27, "Advanced Control System", ModuleAdvControl.class,38).addSide(SIDE.RIGHT).addParent(seat)		
			.addRecipe(new Object[][] {{null, screen, null},
									   {Item.redstone, wheel, Item.redstone},
									   {Item.ingotIron, Item.ingotIron, speedhandle}
									  });			
		
		
		ModuleDataGroup detectorGroup = new ModuleDataGroup("Entity Detector");
		

		
		ModuleData shooter = new ModuleData(28, "Shooter", ModuleShooter.class,15).addSide(SIDE.TOP)	
			.addRecipe(new Object[][] {{pipe, pipe, pipe},
									   {pipe, shootingcore, pipe},
									   {pipe, pipe,pipe}
									  });			
		
		ModuleData advshooter = new ModuleData(29, "Advanced Shooter", ModuleShooterAdv.class,50).addSide(SIDE.TOP).addRequirement(detectorGroup)		
			.addRecipe(new Object[][] {{null, mobdetector, null},
									   {null, shootingcore, pipe},
									   {Item.ingotIron, analyzer ,Item.ingotIron}
									  });		

		ModuleDataGroup shooterGroup = new ModuleDataGroup("Shooter");
		shooterGroup.add(shooter);
		shooterGroup.add(advshooter);
		
		ModuleData animal = new ModuleData(21, "Entity Detector: Animal", ModuleAnimal.class,1).addParent(advshooter)
			.addRecipe(new Object[][] {{Item.porkRaw},
										{disk}
									});		
		ModuleData player = new ModuleData(22, "Entity Detector: Player", ModulePlayer.class,7).addParent(advshooter)
			.addRecipe(new Object[][] {	{Item.diamond},
										{disk}
									});			
		ModuleData villager = new ModuleData(23, "Entity Detector: Villager", ModuleVillager.class,1).addParent(advshooter)
			.addRecipe(new Object[][] {	{Item.emerald},
										{disk}
									});			
		ModuleData monster = new ModuleData(24, "Entity Detector: Monster", ModuleMonster.class,1).addParent(advshooter)
			.addRecipe(new Object[][] {	{Item.slimeBall},
										{disk}
									});			
		ModuleData bats = new ModuleData(48, "Entity Detector: Bat", ModuleBat.class,1).addParent(advshooter)
			.addRecipe(new Object[][] {	{Block.pumpkin},
										{disk}
									});	
		if (!StevesCarts.isHalloween) {
			bats.lock();
		}	

		detectorGroup.add(animal);
		detectorGroup.add(player);
		detectorGroup.add(villager);
		detectorGroup.add(monster);
		detectorGroup.add(bats);			
		
		ModuleData cleaner = new ModuleData(30, "Cleaning Machine", ModuleCleaner.class,23).addSide(SIDE.CENTER)		
			.addRecipe(new Object[][] {{cleaningtube, cleaningcore, cleaningtube},
									   {cleaningtube, null, cleaningtube},
									   {cleaningtube, null , cleaningtube}
									  });	
							  
		addNemesis(frontChest, cleaner);

		
		new ModuleData(31, "Dynamite Carrier", ModuleDynamite.class,3).addSide(SIDE.TOP)					
			.addRecipe(new Object[][] {{null,  dynamite,  null},
									   {dynamite, Item.flintAndSteel,  dynamite},
									   {null,  dynamite, null}
									  });
		
		new ModuleData(32, "Divine Shield", ModuleShield.class, 60)			
			.addRecipe(new Object[][] {{Block.obsidian,  hardstuff,  Block.obsidian},
									   {hardstuff, Block.blockDiamond, hardstuff},
									   {Block.obsidian,  hardstuff, Block.obsidian}
									  });

		
		ModuleData melter = new ModuleData(33, "Melter", ModuleMelter.class,10)
			.addRecipe(new Object[][] {{Block.netherBrick,  Block.glowStone,  Block.netherBrick},
									   {Item.glowstone, Block.furnaceIdle, Item.glowstone},
									   {Block.netherBrick,  Block.glowStone, Block.netherBrick}
									  });
		ModuleData extrememelter = new ModuleData(34, "Extreme Melter", ModuleMelterExtreme.class,19)
			.addRecipe(new Object[][] {{Block.netherBrick,  Block.obsidian,  Block.netherBrick},
									   {melter.getItemStack(), Item.bucketLava, melter.getItemStack()},
									   {Block.netherBrick,  Block.obsidian, Block.netherBrick}
									  });	
		addNemesis(melter, extrememelter);
								  
		
		new ModuleData(36, "Invisibility Core", ModuleInvisible.class, 21)
			.addRecipe(new Object[][] {{null, magicGlass, null},
									   {magicGlass, Item.eyeOfEnder, magicGlass},
									   {null, Item.goldenCarrot, null}
									  });
				
		new ModuleDataHull(37, "Wooden Hull", ModuleWood.class).setCapacity(50).setEngineMax(1).setAddonMax(0).setComplexityMax(15)		
			.addRecipe(new Object[][] {{planks, null, planks},
									   {planks, planks, planks},
									   {woodenWheels, null, woodenWheels}
									  });
		
		new ModuleDataHull(38, "Standard Hull", ModuleStandard.class).setCapacity(200).setEngineMax(3).setAddonMax(6).setComplexityMax(50)
			.addRecipe(new Object[][] {{Item.ingotIron, null, Item.ingotIron},
									   {Item.ingotIron, Item.ingotIron, Item.ingotIron},
									   {ironWheels, null, ironWheels}
									  });		
		
		ModuleData reinfhull = new ModuleDataHull(39, "Reinforced Hull", ModuleReinforced.class).setCapacity(500).setEngineMax(5).setAddonMax(12).setComplexityMax(150)		
			.addRecipe(new Object[][] {{refinedMetal, null, refinedMetal},
									   {refinedMetal, refinedMetal, refinedMetal},
									   {refinedWheels, null, refinedWheels}
									  });

		ModuleData pumpkinhull = new ModuleDataHull(47, "Pumpkin chariot", ModulePumpkin.class).setCapacity(40).setEngineMax(1).setAddonMax(0).setComplexityMax(15)	
			.addRecipe(new Object[][] {{planks, null, planks},
									   {planks, Block.pumpkin, planks},
									   {woodenWheels, null, woodenWheels}
									  });
									  
		if (!StevesCarts.isHalloween) {
			pumpkinhull.lock();
		}
		
		new ModuleDataHull(62, "Mechanical Pig", ModulePig.class).setCapacity(150).setEngineMax(2).setAddonMax(4).setComplexityMax(50).addSide(SIDE.FRONT) 			
			.addRecipe(new Object[][] {{Item.porkRaw, null, Item.porkRaw},
									   {Item.porkRaw, Item.porkRaw, Item.porkRaw},
									   {ironWheels, null, ironWheels}
									  })
		.addMessage("In memory of Vswe's Thunderpig")
		.addMessage("arena victory. Thanks everyone")
		.addMessage("who donated during the 2013")
		.addMessage("Minecraft Marathon");
		
		new ModuleDataHull(76, "Creative Hull", ModuleCheatHull.class).setCapacity(10000).setEngineMax(5).setAddonMax(12).setComplexityMax(150);

		
		
		new ModuleDataHull(81, "Galgadorian Hull", ModuleGalgadorian.class).setCapacity(1000).setEngineMax(5).setAddonMax(12).setComplexityMax(150)		
				.addRecipe(new Object[][] {{magicmetal, null, magicmetal},
										   {magicmetal, magicmetal, magicmetal},
										   {galgWheels, null, galgWheels}
										  });		
		
		StevesCarts.tabsSC2.setIcon(reinfhull.getItemStack());
		StevesCarts.tabsSC2Components.setIcon(refinedWheels);

		
		
		new ModuleData(40, "Note Sequencer", ModuleNote.class, 30).addSides(new SIDE[] {SIDE.RIGHT, SIDE.LEFT})
			.addRecipe(new Object[][] {
									   {Block.music, null, Block.music},
									   {Block.music, Block.jukebox, Block.music},
									   {planks, Item.redstone, planks}
									  });
									  

		
		new ModuleData(41, "Colorizer", ModuleColorizer.class, 15)
			.addRecipe(new Object[][] {
									   {redComponent, greenComponent, blueComponent},
									   {Item.ingotIron, Item.redstone, Item.ingotIron},
									   {null, Item.ingotIron, null}
									  });			
		
		//new ModuleData(46, "Commander", realtimeModuleCommand.class, 15);
		
		new ModuleData(49, "Chunk Loader", ModuleChunkLoader.class, 84)
			.addRecipe(new Object[][] {
									   {null, Item.enderPearl, null},
									   {pcb, Item.ingotIron, pcb},
									   {refinedMetal, advpcb, refinedMetal}
									  });	
		
		
		ModuleData gift = new ModuleData(50, "Gift Storage", ModuleGiftStorage.class, 12) {
			@Override
			public String getModuleInfoText(byte b) {
				if (b == 0) {
					return "Empty";
				}else{
					return "Might contain a surprise";
				}				
			}
			
			@Override
			public String getCartInfoText(String name, byte b) {
				if (b == 0) {
					return "Empty " + name;
				}else{
					return "Full " + name;
				}
			}		
		}.addSides(new SIDE[] {SIDE.RIGHT, SIDE.LEFT}).useExtraData((byte)1)			
			.addRecipe(new Object[][] {
				{yellowribbon,null, redribbon},
				{redwrapping,chestLock,greenwrapping},
				{redwrapping,filledsock,greenwrapping}
			});			
		if (!StevesCarts.isChristmas) {
			gift.lock();
		}	
		
		
		new ModuleData(51, "Projectile: Potion", ModulePotion.class, 10).addRequirement(shooterGroup)
			.addRecipe(new Object[][] {	{Item.glassBottle},
										{disk}
									});		
		new ModuleData(52, "Projectile: Fire Charge", ModuleFireball.class, 10).lockByDefault().addRequirement(shooterGroup)
			.addRecipe(new Object[][] {	{Item.fireballCharge},
										{disk}
									});			
		new ModuleData(53, "Projectile: Egg", ModuleEgg.class, 10).addRequirement(shooterGroup)
			.addRecipe(new Object[][] {	{Item.egg},
										{disk}
			
										});				
		ModuleData snowballshooter = new ModuleData(54, "Projectile: Snowball", ModuleSnowball.class, 10).addRequirement(shooterGroup)
			.addRecipe(new Object[][] {	{Item.snowball},
										{disk}
									});	
			
		if (!StevesCarts.isChristmas) {
			snowballshooter.lock();
		}	
		
			
	ModuleData cake = new ModuleData(90, "Projectile: Cake", ModuleCake.class, 10).addRequirement(shooterGroup).lock()
	.addRecipe(new Object[][] {	{Item.cake},
			{disk}	
	});
		

	
		ModuleData snowgenerator = new ModuleData(55, "Freezer", ModuleSnowCannon.class, 24)
			.addRecipe(new Object[][] {	{Block.blockSnow, Item.bucketWater, Block.blockSnow},
										{Item.bucketWater, pcb, Item.bucketWater},
										{Block.blockSnow, Item.bucketWater, Block.blockSnow}
									});	
		if (!StevesCarts.isChristmas) {
			snowgenerator.lock();
		}	
		addNemesis(snowgenerator, melter);
		addNemesis(snowgenerator, extrememelter);
				

		ModuleData cage = new ModuleData(57, "Cage", ModuleCage.class, 7).addSides(new SIDE[] {SIDE.TOP, SIDE.CENTER})						
			.addRecipe(new Object[][] {	{Block.fenceIron, Block.fenceIron, Block.fenceIron},
										{Block.fenceIron, pcb, Block.fenceIron},
										{Block.fenceIron, Block.fenceIron, Block.fenceIron}
									  });				
			

		new ModuleData(58, "Crop: Nether Wart", ModuleNetherwart.class, 20).addRequirement(farmerGroup)
			.addRecipe(new Object[][] {{Item.netherStalkSeeds},
										{disk}
									  });	
									  
		new ModuleData(59, "Firework display", ModuleFirework.class, 45)
			.addRecipe(new Object[][] {	{Block.fenceIron, Block.dispenser, Block.fenceIron},
										{Block.workbench, fuse, Block.workbench},
										{pcb, Item.flintAndSteel, pcb}
									  });			
		
		
		ModuleData cheatengine = new ModuleData(61, "Creative Engine", ModuleCheatEngine.class, 1);
		
		ModuleData internalTank = new ModuleData(63, "Internal Tank", ModuleInternalTank.class, 37).setAllowDuplicate()
			.addRecipe(new Object[][] {{tankpane, tankpane, tankpane},
										{tankpane, valve, tankpane},
										{tankpane, tankpane, tankpane}
									  });		
		
		ModuleData sideTank = new ModuleData(64, "Side Tanks", ModuleSideTanks.class, 10).addSides(new SIDE[] {SIDE.RIGHT, SIDE.LEFT})			
			.addRecipe(new Object[][] {{hugetankpane, tankpane, hugetankpane},
										{largetankpane, valve, largetankpane},
										{hugetankpane, tankpane, hugetankpane}
									  });			
		
		ModuleData topTank = new ModuleData(65, "Top Tank", ModuleTopTank.class, 22).addSide(SIDE.TOP)
			.addRecipe(new Object[][] {{hugetankpane, hugetankpane, hugetankpane},
										{tankpane, valve, tankpane},
										{hugetankpane, hugetankpane, hugetankpane}
									  });	
									  
		ModuleData advancedTank = new ModuleData(66, "Advanced Tank", ModuleAdvancedTank.class, 54).addSides(new SIDE[] {SIDE.TOP, SIDE.CENTER})
			.addRecipe(new Object[][] {{hugetankpane, hugetankpane, hugetankpane},
										{hugetankpane, valve, hugetankpane},
										{hugetankpane, hugetankpane, hugetankpane}
									  });
									  
		ModuleData frontTank = new ModuleData(67, "Front Tank", ModuleFrontTank.class, 15).addSide(SIDE.FRONT)
		.addRecipe(new Object[][] {{tankpane, largetankpane, tankpane},
										{tankpane, valve, tankpane},
										{largetankpane, largetankpane, largetankpane}
									  });	
		
		ModuleData creativeTank = new ModuleData(72, "Creative Tank", ModuleCheatTank.class, 1).setAllowDuplicate()
			.addMessage("Room for an average sized ocean");		
		
		ModuleData topTankOpen = new ModuleData(73, "Open Tank", ModuleOpenTank.class, 31).addSide(SIDE.TOP)		
			.addRecipe(new Object[][] {{tankpane, null, tankpane},
										{tankpane, valve, tankpane},
										{hugetankpane, hugetankpane, hugetankpane}
									  });			
		
		addNemesis(frontTank, cleaner);

		

		tankGroup.add(internalTank).add(sideTank).add(topTank).add(advancedTank).add(frontTank).add(creativeTank).add(topTankOpen);
		
		new ModuleData(68, "Incinerator", ModuleIncinerator.class, 23).addRequirement(tankGroup).addRequirement(drillGroup)
			.addRecipe(new Object[][] {{Block.netherBrick,  Block.netherBrick,  Block.netherBrick},
									   {Block.obsidian, Block.furnaceIdle, Block.obsidian},
									   {Block.netherBrick,  Block.netherBrick, Block.netherBrick}
									  });	
				
		ModuleData thermal0 = new ModuleData(69, "Thermal Engine", ModuleThermalStandard.class, 28).addRequirement(tankGroup)
			.addRecipe(new Object[][] {{Block.netherBrick,  Block.netherBrick,  Block.netherBrick},
									   {Block.obsidian, Block.furnaceIdle, Block.obsidian},
									   {Block.pistonBase, null, Block.pistonBase}
									  });	
									  
		ModuleData thermal1 = new ModuleData(70, "Advanced Thermal Engine", ModuleThermalAdvanced.class, 58).addRequirement(tankGroup.copy(2))
			.addRecipe(new Object[][] {{Block.netherBrick,  Block.netherBrick,  Block.netherBrick},
									   {refinedMetal, thermal0.getItemStack(), refinedMetal},
									   {Block.pistonBase, null, Block.pistonBase}
									  });		
		addNemesis(thermal0, thermal1);

		ModuleData cleanerliquid = new ModuleData(71, "Liquid Cleaner", ModuleLiquidDrainer.class, 30).addSide(SIDE.CENTER).addParent(liquidsensors).addRequirement(tankGroup)
			.addRecipe(new Object[][] {{cleaningtubeliquid, cleaningcoreliquid, cleaningtubeliquid},
									   {cleaningtubeliquid, null, cleaningtubeliquid},
									   {cleaningtubeliquid, null , cleaningtubeliquid}
									  });
									  
		addNemesis(frontTank, cleanerliquid);
		addNemesis(frontChest, cleanerliquid);		
		

		ItemStack yellowWool = new ItemStack(Block.cloth, 1, 4);
		ModuleData eggBasket = new ModuleData(74, "Egg Basket", ModuleEggBasket.class, 14) {
			@Override
			public String getModuleInfoText(byte b) {
				if (b == 0) {
					return "Empty";
				}else{
					return "Full of Eggs";
				}				
			}
			
			@Override
			public String getCartInfoText(String name, byte b) {
				if (b == 0) {
					return "Empty " + name;
				}else{
					return "Full " + name;
				}
			}
		}.addSide(SIDE.TOP).useExtraData((byte)1)	
			.addRecipe(new Object[][] {{yellowWool, yellowWool, yellowWool},
									   {easterEggs[0], chestLock, easterEggs[1]},
									   {easterEggs[2], basket , easterEggs[3]}
									  });
		
		if (!StevesCarts.isEaster) {
			eggBasket.lock();
		}			
		
		ModuleData intelligence = new ModuleData(75, "Drill Intelligence", ModuleDrillIntelligence.class, 21).addRequirement(drillGroup)
			.addRecipe(new Object[][] {{Item.ingotGold, Item.ingotGold, Item.ingotGold},
									   {Item.ingotIron, advpcb, Item.ingotIron},
									   {advpcb, Item.redstone , advpcb}
									  });	

		new ModuleData(77, "Power Observer", ModulePowerObserver.class, 12).addRequirement(engineGroup)
			.addRecipe(new Object[][] {{null, Block.pistonBase, null},
									   {Item.ingotIron, Item.redstone, Item.ingotIron},
									   {Item.redstone, advpcb , Item.redstone}
									  });			
				

		engineGroup.add(coalTiny);
		engineGroup.add(coalStandard);
		engineGroup.add(solar0);
		engineGroup.add(solar1);		
		engineGroup.add(thermal0);
		engineGroup.add(thermal1);		
		engineGroup.add(compactsolar);
		engineGroup.add(cheatengine);	
		

		
		ModuleDataGroup toolGroup = ModuleDataGroup.getCombinedGroup("Tool", drillGroup, woodcutterGroup);
		toolGroup.add(farmerGroup);

		
		ModuleDataGroup enchantableGroup = ModuleDataGroup.getCombinedGroup("Tool or Shooter", toolGroup, shooterGroup);
		
		new ModuleData(82, "Enchanter", ModuleEnchants.class, 72).addRequirement(enchantableGroup)
			.addRecipe(new Object[][] {{null, magicmetal, null},
					   {Item.book, Block.enchantmentTable, Item.book},
					   {Item.redstone,  advpcb, Item.redstone}
					  });	
		
		new ModuleData(83, "Ore Extractor", ModuleOreTracker.class, 80).addRequirement(drillGroup)
		.addRecipe(new Object[][] {{Block.torchRedstoneActive, null, Block.torchRedstoneActive},
				   {magicTingy, magicmetal, magicTingy},
				   {Item.netherQuartz,  advpcb, Item.netherQuartz}
				  });			
		
		
		ModuleData flowerremover = new ModuleData(85, "Lawn Mower", ModuleFlowerRemover.class, 38).addSides(new SIDE[] {SIDE.RIGHT, SIDE.LEFT})	
				.addRecipe(new Object[][] {{bladearm, null,bladearm},
						   {null, pcb, null},
						   {bladearm,  null, bladearm}
						  });	
		
		new ModuleData(86, "Milker", ModuleMilker.class, 26).addParent(cage)
			.addRecipe(new Object[][] {{Item.wheat, Item.wheat, Item.wheat},
					{pcb, Item.bucketEmpty, pcb},
					{null, pcb, null},
					  });		
		
		ModuleData crafter = new ModuleData(87, "Crafter", ModuleCrafter.class, 22).setAllowDuplicate()
			.addRecipe(new Object[][] {{pcb},
					   {Block.workbench}
					  });	
		
		new ModuleData(88, "Tree: Exotic", ModuleModTrees.class, 30).addRequirement(woodcutterGroup)
			.addRecipe(new Object[][] {{Item.glowstone, null, Item.glowstone},
					{Item.redstone, Block.sapling, Item.redstone},
					{pcb, disk, pcb}
				});					
			
		
		new ModuleData(89, "Planter Range Extender", ModulePlantSize.class, 20).addRequirement(woodcutterGroup)
			.addRecipe(new Object[][] {{Item.redstone, advpcb, Item.redstone},
					{null, Block.sapling, null},
					{pcb, Block.sapling, pcb}
				});		
		
		
		new ModuleData(78, "Steve's Arcade", ModuleArcade.class, 10).addParent(seat)
			.addRecipe(new Object[][] {{null, Block.thinGlass, null},
					{planks, pcb, planks},
					{Item.redstone, planks, Item.redstone}
				});	
		
		ModuleData smelter = new ModuleData(91, "Smelter", ModuleSmelter.class, 22).setAllowDuplicate()
		.addRecipe(new Object[][] {{pcb},
				   {Block.furnaceIdle}
				  });	
		
		new ModuleData(92, "Advanced Crafter", ModuleCrafterAdv.class, 42).setAllowDuplicate()
		.addRecipe(new Object[][] {{null, Item.diamond, null},
					{null, advpcb, null},
				   {pcb, crafter.getItemStack(), pcb}
				  });			

		new ModuleData(93, "Advanced Smelter", ModuleSmelterAdv.class, 42).setAllowDuplicate()
		.addRecipe(new Object[][] {{null, Item.diamond, null},
					{null, advpcb, null},
				   {pcb, smelter.getItemStack(), pcb}
				  });		
		
		new ModuleData(94, "Information Provider", ModuleLabel.class, 12)
		.addRecipe(new Object[][] {{ Block.thinGlass, Block.thinGlass,  Block.thinGlass},
				{Item.ingotIron, Item.glowstone, Item.ingotIron},
			   {pcb, Item.sign, pcb}
			  });	
			
		
		new ModuleData(95, "Experience Bank", ModuleExperience.class, 36)
		.addRecipe(new Object[][] {{null, Item.redstone,  null},
				{Item.glowstone, Item.emerald, Item.glowstone},
			   {pcb, Block.cauldron, pcb}
			  });		
		
		
		new ModuleData(96, "Creative Incinerator", ModuleCreativeIncinerator.class, 1).addRequirement(drillGroup);
	
		new ModuleData(97, "Creative Supplies", ModuleCreativeSupplies.class, 1);
		
		new ModuleData(99, "Cake Server", ModuleCakeServer.class, 10).addSide(SIDE.TOP)
		.addMessage("1 year in alpha")
		.addRecipe(new Object[][] {{null, Item.cake,  null},
				{"slabWood", "slabWood", "slabWood"},
			   {null, pcb, null}
			  });	
		
		ModuleData trickOrTreat = new ModuleData(100, "Trick-or-Treat Cake Server", ModuleCakeServerDynamite.class, 15).addSide(SIDE.TOP)
			.addRecipe(new Object[][] {{null, Item.cake,  null},
					{"slabWood", "slabWood", "slabWood"},
				   {dynamite, pcb, dynamite}
				  });		
		
		if (!StevesCarts.isHalloween) {
			trickOrTreat.lock();
		}	
		
		//new ModuleData(98, "Pew Pew", ModuleShooterAdvSide.class, 1);
		
		new ModuleData(35, "Computer", ModuleComputer.class, 16);
			
			
		//System.out.println("Module Combinations: " + calculateCombinations());
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModels() {
		//Coal engine
		moduleList.get((byte)0)
		.addModel("Engine",  new ModelEngineFrame())
		.addModel("Fire",  new ModelEngineInside());
		
		//Tiny coal engine
		moduleList.get((byte)44)
		.addModel("Engine",  new ModelEngineFrame())
		.addModel("Fire",  new ModelEngineInside());	
		
		//solar engine
		moduleList.get((byte)1)
		.addModel("SolarPanelBase",new ModelSolarPanelBase())
		.addModel("SolarPanels",new ModelSolarPanelHeads(4));
	
		moduleList.get((byte)45)
		.addModel("SolarPanelBase",new ModelSolarPanelBase())
		.addModel("SolarPanels",new ModelSolarPanelHeads(2));

		moduleList.get((byte)56)
		.addModel("SolarPanelsSide",new ModelCompactSolarPanel());

		moduleList.get((byte)2)
		.addModel("SideChest", new ModelSideChests());

		moduleList.get((byte)3)
		.removeModel("Top")
		.addModel("TopChest", new ModelTopChest());	

		moduleList.get((byte)4)
		.addModel("FrontChest", new ModelFrontChest())
		.setModelMult(0.68F);	

		moduleList.get((byte)6)
		.addModel("SideChest", new ModelExtractingChests());

		moduleList.get((byte)7)
		.addModel("Torch", new ModelTorchplacer());

		moduleList.get((byte)8)
		.addModel("Drill", new ModelDrill(ResourceHelper.getResource("/models/drillModelDiamond.png")))
		.addModel("Plate", new ModelToolPlate());

		moduleList.get((byte)42)
		.addModel("Drill", new ModelDrill(ResourceHelper.getResource("/models/drillModelIron.png")))				
		.addModel("Plate", new ModelToolPlate());

		moduleList.get((byte)43)
		.addModel("Drill", new ModelDrill(ResourceHelper.getResource("/models/drillModelHardened.png")))				
		.addModel("Plate", new ModelToolPlate());

		moduleList.get((byte)9)
		.addModel("Drill", new ModelDrill(ResourceHelper.getResource("/models/drillModelMagic.png")))				
		.addModel("Plate", new ModelToolPlate());

		moduleList.get((byte)10)
		.addModel("Rails", new ModelRailer(3));

		moduleList.get((byte)11)
		.addModel("Rails", new ModelRailer(6));

		moduleList.get((byte)12)
		.addModel("Bridge", new ModelBridge())
		.addModel("Plate", new ModelToolPlate());

		moduleList.get((byte)13)
		.addModel("Remover", new ModelTrackRemover())
		.setModelMult(0.60F);

		moduleList.get((byte)14)
		.addModel("Farmer", new ModelFarmer(ResourceHelper.getResource("/models/farmerModelDiamond.png")))					
		.setModelMult(0.45F);

		moduleList.get((byte)84)
		.addModel("Farmer", new ModelFarmer(ResourceHelper.getResource("/models/farmerModelGalgadorian.png")))					
		.setModelMult(0.45F);

		moduleList.get((byte)15)
		.addModel("WoodCutter", new ModelWoodCutter(ResourceHelper.getResource("/models/woodCutterModelDiamond.png")))
		.addModel("Plate", new ModelToolPlate());

		moduleList.get((byte)79)
		.addModel("WoodCutter", new ModelWoodCutter(ResourceHelper.getResource("/models/woodCutterModelHardened.png")))				
		.addModel("Plate", new ModelToolPlate());

		moduleList.get((byte)80)
		.addModel("WoodCutter", new ModelWoodCutter(ResourceHelper.getResource("/models/woodCutterModelGalgadorian.png")))			
		.addModel("Plate", new ModelToolPlate());	

		moduleList.get((byte)20)
		.addModel("Sensor", new ModelLiquidSensors());

		moduleList.get((byte)25)
		.removeModel("Top")
		.addModel("Chair", new ModelSeat());	

		moduleList.get((byte)26)
		.addModel("Lever", new ModelLever(ResourceHelper.getResource("/models/leverModel.png")));

		moduleList.get((byte)27)
		.addModel("Lever", new ModelLever(ResourceHelper.getResource("/models/leverModel2.png")))
		.addModel("Wheel", new ModelWheel());

		//used for the guns model
		ArrayList<Integer> pipes = new ArrayList<Integer>();
		for (int i = 0; i < 9; i++) {
			if (i == 4) continue;
			/*if (i == 3) continue;
			if (i == 5) continue;*/
			pipes.add(i);
		}

		moduleList.get((byte)28)
		.addModel("Rig",  new ModelShootingRig())
		.addModel("Pipes", new ModelGun(pipes));	

		moduleList.get((byte)29)
		.addModel("Rig",  new ModelShootingRig())
		.addModel("MobDetector", new ModelMobDetector())
		.addModel("Pipes", new ModelSniperRifle());

		moduleList.get((byte)30)
		.addModel("Top",  new ModelHullTop(ResourceHelper.getResource("/models/cleanerModelTop.png"), false))
		.addModel("Cleaner", new ModelCleaner());

		moduleList.get((byte)31)
		.addModel("Tnt",new ModelDynamite());

		moduleList.get((byte)32)
		.addModel("Shield", new ModelShield())	
		.setModelMult(0.68F);

		moduleList.get((byte)37)
		.addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelWooden.png")))
		.addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelWoodenTop.png")));	

		moduleList.get((byte)38)
		.addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelStandard.png")))
		.addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelStandardTop.png")));

		moduleList.get((byte)39)
		.addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelLarge.png")))
		.addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelLargeTop.png")));


		moduleList.get((byte)47)
		.addModel("Hull", new ModelPumpkinHull(ResourceHelper.getResource("/models/hullModelPumpkin.png"), ResourceHelper.getResource("/models/hullModelWooden.png")))
		.addModel("Top",new ModelPumpkinHullTop(ResourceHelper.getResource("/models/hullModelPumpkinTop.png"), ResourceHelper.getResource("/models/hullModelWoodenTop.png")));


		moduleList.get((byte)62)
		.addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelPig.png")))
		.addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelPigTop.png")))
		.addModel("Head", new ModelPigHead())
		.addModel("Tail", new ModelPigTail())
		.addModel("Helmet", new ModelPigHelmet(false))
		.addModel("Helmet_Overlay", new ModelPigHelmet(true));

		moduleList.get((byte)76)
		.addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelCreative.png")))
		.addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelCreativeTop.png")));	


		moduleList.get((byte)81)
		.addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelGalgadorian.png")))
		.addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelGalgadorianTop.png")));

		moduleList.get((byte)40)
		.setModelMult(0.65F)			
		.addModel("Speakers",new ModelNote());

		moduleList.get((byte)50)
		.addModel("GiftStorage", new ModelGiftStorage());

		moduleList.get((byte)57)
		.removeModel("Top")
		.addModel("Cage", new ModelCage(false), false)	
		.addModel("Cage", new ModelCage(true), true)	
		.setModelMult(0.65F);

		moduleList.get((byte)64)
		.addModel("SideTanks", new ModelSideTanks());

		moduleList.get((byte)65)
		.addModel("TopTank", new ModelTopTank(false));

		moduleList.get((byte)66)
		.addModel("LargeTank", new ModelAdvancedTank())
		.removeModel("Top")	;

		moduleList.get((byte)67)
		.setModelMult(0.68F)			
		.addModel("FrontTank", new ModelFrontTank());

		moduleList.get((byte)73)
		.addModel("TopTank", new ModelTopTank(true));

		moduleList.get((byte)71)
		.addModel("Top",  new ModelHullTop(ResourceHelper.getResource("/models/cleanerModelTop.png"), false))
		.addModel("Cleaner", new ModelLiquidDrainer());

		moduleList.get((byte)74)
		.addModel("TopChest", new ModelEggBasket());

		moduleList.get((byte)85)
		.addModel("LawnMower", new ModelLawnMower())
		.setModelMult(0.4F);
		
		/*moduleList.get((byte)98)
		.addModel("SideGuns", new ModelSideGuns());*/	
		
		moduleList.get((byte)99)
		.addModel("Cake", new ModelCake());
		
		moduleList.get((byte)100)
		.addModel("Cake", new ModelCake());
	} 
	
	
	
	private byte id;
	private Class<? extends ModuleBase> moduleClass;
	private String name;
	private int modularCost;
	private int groupID;
	private ArrayList<SIDE> renderingSides;
	private boolean allowDuplicate;
	private ArrayList<ModuleData> nemesis = null;
	private ArrayList<ModuleDataGroup> requirement = null;
	private ModuleData parent = null;
	private boolean isValid;
	private boolean isLocked;
	private boolean defaultLock;
	private boolean hasRecipe;
	private ArrayList<String> message;
	private HashMap<String,ModelCartbase> models;
	private HashMap<String,ModelCartbase> modelsPlaceholder;
	private ArrayList<String> removedModels;
	private float modelMult = 0.75F;
	private boolean useExtraData;
	private byte extraDataDefaultValue;
	public ModuleData(int id, String name, Class<? extends ModuleBase> moduleClass, int modularCost) {
		this.id = (byte)id;
		this.moduleClass = moduleClass;
		this.name = name;
		this.modularCost = modularCost;
		
		groupID = moduleGroups.length;
		for (int i = 0; i < moduleGroups.length; i++) {
			if (moduleGroups[i].isAssignableFrom(moduleClass)) {
				groupID = i;
				break;
			}
		}
		
	
		if (moduleList.containsKey(this.id)) {
			System.out.println("WARNING! " + name + " can't be added with ID " + id + " since that ID is already occupied by " + moduleList.get(this.id).getName());
		}else{		
			moduleList.put(this.id, this);
		}
	}

	public Class<? extends ModuleBase> getModuleClass() {
		return moduleClass;
	}
	
	public boolean getIsValid() {
		return isValid;
	}
	
	public boolean getIsLocked() {
		return isLocked;
	}
	
	protected ModuleData lock() {
		isLocked = true;
		
		return this;
	}	
	
	public boolean getEnabledByDefault() {
		return !defaultLock;
	}
	
	protected ModuleData lockByDefault() {
		defaultLock = true;
		
		return this;
	}
	
	protected ModuleData setAllowDuplicate() {
		allowDuplicate = true;
		
		return this;
	}
	
	protected boolean getAllowDuplicate() {
		return allowDuplicate;
	}
	
	protected ModuleData addSide(SIDE side) {
		if (renderingSides == null) {
			renderingSides = new ArrayList<SIDE>();
		}
		renderingSides.add(side);
	
		if (side == SIDE.TOP) {
			removeModel("Rails");
		}
	
		return this;
	}
	
	
	public ModuleData useExtraData(byte defaultValue) {
		this.extraDataDefaultValue = defaultValue;
		this.useExtraData = true;
		
		return this;
	}
	
	public boolean isUsingExtraData() {
		return useExtraData;
	}
	
	public byte getDefaultExtraData() {
		return extraDataDefaultValue;
	}

	
	public ArrayList<SIDE> getRenderingSides() {
		return renderingSides;
	}
	
	protected ModuleData addSides(SIDE[] sides) {
		for (int i = 0; i < sides.length; i++) {
			addSide(sides[i]);
		}

		return this;
	}
	
	protected ModuleData addParent(ModuleData parent) {
		this.parent = parent;

		return this;
	}	
	
	protected ModuleData addMessage(String s) {
		if (message == null) {
			message = new ArrayList<String>();
		}
		message.add(s);
		
		return this;
	}
	
	protected void addNemesis(ModuleData nemesis) {
		if (this.nemesis == null) {
			this.nemesis = new ArrayList<ModuleData>();
		}
		this.nemesis.add(nemesis);
	}	
	
	protected ModuleData addRequirement(ModuleDataGroup requirement) {
		if (this.requirement == null) {
			this.requirement = new ArrayList<ModuleDataGroup>();
		}
		this.requirement.add(requirement);
		
		return this;
	}		

	protected static void addNemesis(ModuleData m1, ModuleData m2) {
		m2.addNemesis(m1);
		m1.addNemesis(m2);
	}	
	
	public float getModelMult() {
		return modelMult;
	}

	protected ModuleData setModelMult(float val) {
		modelMult = val;
		
		return this;
	}
	
	protected ModuleData addModel(String tag, ModelCartbase model) {
		addModel(tag, model, false);
		addModel(tag, model, true);
		return this;
	}
	protected ModuleData addModel(String tag, ModelCartbase model, boolean placeholder) {
		if (placeholder) {
			if (modelsPlaceholder == null) {
				modelsPlaceholder = new HashMap<String,ModelCartbase>();
			}
		
			modelsPlaceholder.put(tag, model);
		}else{
			if (models == null) {
				models = new HashMap<String,ModelCartbase>();
			}
		
			models.put(tag, model);		
		}
		
		return this;
	}
	
	public HashMap<String,ModelCartbase> getModels(boolean placeholder) {
		if (placeholder) {
			return modelsPlaceholder;
		}else{
			return models;
		}
	}
	
	public boolean haveModels(boolean placeholder) {
		if (placeholder) {
			return modelsPlaceholder != null;
		}else{
			return models != null;
		}
	}
	
	protected ModuleData removeModel(String tag) {
		if (removedModels == null) {
			removedModels = new ArrayList<String>();
		}
	
		if(!removedModels.contains(tag)) {
			removedModels.add(tag);
		}
		return this;
	}	
	
	public ArrayList<String> getRemovedModels() {
		return removedModels;
	}
	
	public boolean haveRemovedModels() {
		return removedModels != null;
	}

	public String getName() {
		return name;
	}

	public byte getID() {
		return id;
	}
	
	public int getCost() {
		return modularCost;
	}
	
	protected ModuleData getParent() {
		return parent;
	}
	
	protected ArrayList<ModuleData> getNemesis() {
		return nemesis;
	}
	
	protected ArrayList<ModuleDataGroup> getRequirement() {
		return requirement;
	}	
	
	public boolean getHasRecipe() {
		return hasRecipe;
	}		
	
	public String getModuleInfoText(byte b) {
		return null;
	}
	
	public String getCartInfoText(String name, byte b) {
		return name;
	}
	
	public static ArrayList<ItemStack> getModularItems(ItemStack cart) {
		ArrayList<ItemStack> modules = new ArrayList<ItemStack>();
		
		if (cart != null && cart.getItem() == StevesCarts.carts && cart.getTagCompound() != null) {
			NBTTagCompound info = cart.getTagCompound();
			if (info.hasKey("Modules")) {
				
				
				
				byte[] IDs = info.getByteArray("Modules");
				for (int i = 0; i < IDs.length; i++) {
					byte id = IDs[i];
					ItemStack module =  new ItemStack(StevesCarts.modules, 1, id);

					StevesCarts.modules.addExtraDataToModule(module, info , i);
					
					
					modules.add(module);
				}
			}
		}
		
		return modules;
	}	
	
	public static ItemStack createModularCart(MinecartModular parentcart) {
		ItemStack cart = new ItemStack(StevesCarts.carts, 1);
			
		NBTTagCompound save = new NBTTagCompound();
		byte [] moduleIDs = new byte[parentcart.getModules().size()];
		for (int i = 0; i < parentcart.getModules().size(); i++) {
			ModuleBase module = parentcart.getModules().get(i);
			for (ModuleData moduledata : moduleList.values()) {
				if (module.getClass() == moduledata.moduleClass) {
					moduleIDs[i] = moduledata.getID();
					break;
				}
			}
			
			StevesCarts.modules.addExtraDataToModule(save, module , i);
			

		}
		save.setByteArray("Modules", moduleIDs);				
		cart.setTagCompound(save);
		CartVersion.addVersion(cart);
		
		return cart;
	}
	
	/*public static ItemStack createModularCart(ArrayList<ModuleData> modules) {
		ItemStack cart = new ItemStack(StevesCarts.carts, 1);
			
		NBTTagCompound save = new NBTTagCompound();
		byte [] moduleIDs = new byte[modules.size()];
		for (int i = 0; i < moduleIDs.length; i++) {
			moduleIDs[i] = modules.get(i).getID();
		}
		save.setByteArray("Modules", moduleIDs);				
		cart.setTagCompound(save);
		CartVersion.addVersion(cart);
		
		return cart;
	}*/
	
	/*public static ArrayList<ModuleData> getModulesFromItems(ArrayList<ItemStack> modules) {
		ArrayList<ModuleData> moduledatas = new ArrayList<ModuleData>();
		for (ItemStack module : modules) {
			ModuleData data = StevesCarts.modules.getModuleData(module);
			if (data != null) {
				moduledatas.add(data);
			}
		}

		return moduledatas;
	}*/
	
	public static ItemStack createModularCartFromItems(ArrayList<ItemStack> modules) {
		ItemStack cart = new ItemStack(StevesCarts.carts, 1);
		
		NBTTagCompound save = new NBTTagCompound();
		byte [] moduleIDs = new byte[modules.size()];
		for (int i = 0; i < moduleIDs.length; i++) {
			moduleIDs[i] =(byte) modules.get(i).getItemDamage();
			
	
			StevesCarts.modules.addExtraDataToCart(save, modules.get(i) , i);
		}
		save.setByteArray("Modules", moduleIDs);				
		cart.setTagCompound(save);
		CartVersion.addVersion(cart);
		
		return cart;		
	}
	
	public static boolean isItemOfModularType(ItemStack itemstack, Class<? extends ModuleBase> validClass) {
		if (itemstack.getItem() == StevesCarts.modules) {
			ModuleData module = StevesCarts.modules.getModuleData(itemstack);
			if (module != null) {
				if (validClass.isAssignableFrom(module.moduleClass)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	private ArrayList<Object[][]> recipes;
	protected ModuleData addRecipe(Object[][] recipe) {
		if(this.recipes == null) {
			this.recipes = new ArrayList<Object[][]>();
		}
		
		this.recipes.add(recipe);
		
		return this;
	}
	
	public void loadRecipe() {
		if (!isLocked) {
			isValid = true;
			if(recipes != null) {
				hasRecipe = true;
				for (Object[][] recipe : recipes) {
					StevesCarts.addRecipe(getItemStack(),recipe);
				}
			}
		}
	}
	
	public ItemStack getItemStack() {
		ItemStack module = new ItemStack(StevesCarts.instance.modules, 1, id);
		
		if (isUsingExtraData()) {
			NBTTagCompound save = new NBTTagCompound("tag");

			save.setByte("Data", getDefaultExtraData());
			
			module.setTagCompound(save);			
		}
		
		return module;	
	}
	

	
	public static boolean isValidModuleItem(int validGroup, ItemStack itemstack) {
		if (itemstack.getItem() == StevesCarts.modules) {

			ModuleData module = StevesCarts.modules.getModuleData(itemstack);
			return isValidModuleItem(validGroup, module);
		}
		
		return false;
	}
	
	public static boolean isValidModuleItem(int validGroup, ModuleData module) {
		if (module != null) {
			if (validGroup >= 0) {
				if (moduleGroups[validGroup].isAssignableFrom(module.moduleClass)) {
					return true;
				}
			}else{
				for (int i = 0; i < moduleGroups.length ; i++) {
					if (moduleGroups[i].isAssignableFrom(module.moduleClass)) {
						return false;
					}
				}
				return true;
			}

		}

		return false;
	}	
	
	public static boolean isValidModuleCombo(ModuleDataHull hull, ArrayList<ModuleData> modules) {
		int[] max = new int[]{1,hull.getEngineMax(),1,4,hull.getAddonMax(),6};
		int[] current = new int[max.length];
		for (ModuleData module : modules) {
			int id = 5;
			for (int i = 0; i < 5; i++) {
				if (isValidModuleItem(i,module)) {
					id = i;
					break;
				}
			}
			current[id] += 1;
			if (current[id] > max[id]) {
				return false;
			}
		}
		return true;
	}

	public void addExtraMessage(List list) {
		if (message != null) {
			list.add("");
			for (String s : message) {
				list.add(ColorHelper.GRAY + "\u00a7o" + s + "\u00a7r");		
			}
		}
	}	
	
	public void addSpecificInformation(List list) {
		list.add(ColorHelper.LIGHTGRAY + "Modular cost: " + modularCost);
	}
	
	public final void addInformation(List list, NBTTagCompound compound) {
		addSpecificInformation(list);
		if (compound != null && compound.hasKey("Data")) {
			String extradatainfo = getModuleInfoText(compound.getByte("Data"));
			if (extradatainfo != null) {
				list.add(ColorHelper.WHITE + extradatainfo); 
			}
		}
		
		GuiScreen gui = Minecraft.getMinecraft().currentScreen;
		if (gui != null && gui.isShiftKeyDown()) {
			
			if (getRenderingSides() == null || getRenderingSides().size() == 0) {
				list.add(ColorHelper.CYAN + "Won't occupy any sides");
			}else{
				String str = ColorHelper.CYAN + "Will occupy the ";
				
				for (int i = 0; i < getRenderingSides().size(); i++) {
					SIDE side = getRenderingSides().get(i);
					
					if(i == 0) {	
						str += side.toString();					
					}else if (i == getRenderingSides().size() - 1) {
						str += " and " + side.toString();			
					}else{
						str += ", " + side.toString();
					}
				}
				if (getRenderingSides().size() == 1) {
					str += " side";
				}else{
					str += " sides";
				}
				
				list.add(str);
			}
			
			if (getNemesis() != null && getNemesis().size() != 0) {
				if (getRenderingSides() == null || getRenderingSides().size() == 0) {
					list.add(ColorHelper.RED + "Will however conflict with:");
				}else{
					list.add(ColorHelper.RED + "Will also conflict with:");
				}
				for (ModuleData module : getNemesis()) {
					list.add(ColorHelper.RED + module.getName());
				}
			}
			
			if (parent != null) {
				list.add(ColorHelper.YELLOW + "Requires " + parent.getName());	
			}
			
			if (getRequirement() != null && getRequirement().size() != 0) {
				for (ModuleDataGroup group : getRequirement()) {
					list.add(ColorHelper.YELLOW + "Requires " + group.getCountName() + " " + group.getName() + ( group.getCount() > 1 ? "s" : ""));	
				}
			}
			
			if (getAllowDuplicate()) {
				list.add(ColorHelper.LIME + "Duplicates are allowed");
			}
			
		}
		
		list.add(ColorHelper.LIGHTBLUE + "Type: " + moduleGroupNames[groupID]);	
		addExtraMessage(list);
	}
	
	
	/*
		0 -	Black
		1 - Blue
		2 - Green
		3 - Cyan
		4 - Red
		5 - Purple
		6 - Orange
		7 - Light Gray
		8 - Gray
		9 - Light blue
		10 - Lime
		11 - Turquise
		12 - Pink
		13 - Magenta
		14 - Yellow
		15 - White
	*/
	/*@Deprecated
	protected String getColorTag(int colorId) {
		if (colorId < 0) {
			colorId = 0;
		}else if(colorId > 15) {
			colorId = 15;
		}
		
		return "\u00a7" + Integer.toHexString(colorId);
	}*/

	
	//Sides that the different modules will claim that they occupy
	public enum SIDE {NONE, TOP, CENTER, BOTTOM, BACK, LEFT, RIGHT, FRONT}
	
	
	public static String checkForErrors(ModuleDataHull hull, ArrayList<ModuleData> modules) {
		//Normal errors here	
		if (getTotalCost(modules) > hull.getCapacity()) {
			return "Your cost is greater than the capacity of the hull.";			
		}				
		
		if (!isValidModuleCombo(hull,modules)) {
			return "The combination of module types of the modules given are not valid. This shouldn't be possible.";
		}

		for (int i = 0; i < modules.size(); i++) {
			ModuleData mod1 = modules.get(i);
			if (mod1.getCost() > hull.getComplexityMax()) {
				return mod1.getName() + " is too complex for this hull.";
			}
			if (mod1.getParent() != null && !modules.contains(mod1.getParent())) {
				return mod1.getName() + " requires " + mod1.getParent().getName() + " to work!";			
			}
			if (mod1.getNemesis() != null) {
				for (ModuleData nemisis : mod1.getNemesis()) {
					if (modules.contains(nemisis)) {
						return mod1.getName() + " won't work with " + nemisis.getName() + "!";			
					}
				}			
			}
			if (mod1.getRequirement() != null){
				for (ModuleDataGroup group : mod1.getRequirement()) {
					int count = 0;
					for (ModuleData mod2 : group.getModules()) {
						for (ModuleData mod3 : modules) {
							if (mod2.equals(mod3)) {
								count++;
							}
						}
					}
					if (count < group.getCount()) {
						return mod1.getName() + " requires " + group.getCountName() + " " + group.getName() + ( group.getCount() > 1 ? "s" : "") +" to work!";			
					}
				}
			}
		
			for (int j = i+1; j < modules.size(); j++) {
				ModuleData mod2 = modules.get(j);
				
				if (mod1 == mod2) {
					if (!mod1.getAllowDuplicate()) {
						return mod1.getName() + " is not allowed to be added twice.";
					}
				}else if (mod1.getRenderingSides() != null && mod2.getRenderingSides() != null) {
					SIDE clash = SIDE.NONE;
					for (SIDE side1 : mod1.getRenderingSides()) {
						for (SIDE side2 : mod2.getRenderingSides()) {
							if (side1 == side2) {											
								clash = side1;
								break;
							}										
						}		
						if (clash != SIDE.NONE) {
							break;
						}
					}
					if (clash != SIDE.NONE) {
						return mod1.getName() + " and " + mod2.getName() + " will clash at the " + clash.toString();
					}
				}					
			}
		}	
		
		return null;
	}
	
	public static int getTotalCost(ArrayList<ModuleData> modules) {
		int currentCost = 0;
		for (ModuleData module : modules) {
			currentCost += module.getCost();
		}
		return currentCost;		
	}	
	
	
	//Just for fun :) It's very slow but it does the trick and is solid(since it's using the built in 
	//error check, this means you won't have to bother with editing this code just if some modules shouldn't work together any more).
	//Calculated for alpha 23: 1,140,776,321
	private static long calculateCombinations() {
		long combinations = 0L;
		ArrayList<ModuleData> potential = new ArrayList<ModuleData>();		
		for (ModuleData module : moduleList.values()) {
			if (! (module instanceof ModuleDataHull)) {
				potential.add(module);
			}
		}
		
		for (ModuleData module : moduleList.values()) {
			if (module instanceof ModuleDataHull) {
				ArrayList<ModuleData> modules = new ArrayList<ModuleData>();			
				combinations += populateHull((ModuleDataHull)module,modules, (ArrayList<ModuleData>)potential.clone(),0);
				System.out.println("Hull added: " + combinations);
			}
		}
		return combinations;
	}
	
	private static long populateHull(ModuleDataHull hull, ArrayList<ModuleData> attached, ArrayList<ModuleData> potential, int depth) {
		if (checkForErrors(hull, attached) != null) {
			return 0L;
		}else{
			long combinations = 1L;
			Iterator itt = potential.iterator();
			while (itt.hasNext()) {
				ModuleData module = (ModuleData)itt.next();
				ArrayList<ModuleData> attachedCopy = (ArrayList<ModuleData>)attached.clone();
				attachedCopy.add(module);
				ArrayList<ModuleData> potentialCopy = (ArrayList<ModuleData>)potential.clone();
				itt.remove();

				
				
				combinations += populateHull(hull, attachedCopy, potentialCopy, depth+1);
				if (depth < 3) {
					System.out.println("Modular state[" + depth + "]: " + combinations);
				}
			}
			return combinations;
		}
	}
	
	public String getRawName() {
		return name.replace(":","").replace("'","").replace(" ","_").replace("-", "_").toLowerCase();
	}
	
	private Icon icon;
		
	@SideOnly(Side.CLIENT)
	public void createIcon(IconRegister register) {
		icon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + getRawName() + "_icon");	
	}
	
	@SideOnly(Side.CLIENT)
	public Icon getIcon() {
		return icon;
	}
	
}