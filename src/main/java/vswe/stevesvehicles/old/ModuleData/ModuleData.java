package vswe.stevesvehicles.old.ModuleData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import vswe.stevesvehicles.client.rendering.models.ModelAdvancedTank;
import vswe.stevesvehicles.client.rendering.models.ModelBridge;
import vswe.stevesvehicles.client.rendering.models.ModelCage;
import vswe.stevesvehicles.client.rendering.models.ModelCake;
import vswe.stevesvehicles.client.rendering.models.ModelCleaner;
import vswe.stevesvehicles.client.rendering.models.ModelCompactSolarPanel;
import vswe.stevesvehicles.client.rendering.models.ModelDrill;
import vswe.stevesvehicles.client.rendering.models.ModelDynamite;
import vswe.stevesvehicles.client.rendering.models.ModelEggBasket;
import vswe.stevesvehicles.client.rendering.models.ModelEngineFrame;
import vswe.stevesvehicles.client.rendering.models.ModelEngineInside;
import vswe.stevesvehicles.client.rendering.models.ModelExtractingChests;
import vswe.stevesvehicles.client.rendering.models.ModelFarmer;
import vswe.stevesvehicles.client.rendering.models.ModelFrontChest;
import vswe.stevesvehicles.client.rendering.models.ModelFrontTank;
import vswe.stevesvehicles.client.rendering.models.ModelGiftStorage;
import vswe.stevesvehicles.client.rendering.models.ModelGun;
import vswe.stevesvehicles.client.rendering.models.ModelHull;
import vswe.stevesvehicles.client.rendering.models.ModelHullTop;
import vswe.stevesvehicles.client.rendering.models.ModelLawnMower;
import vswe.stevesvehicles.client.rendering.models.ModelLever;
import vswe.stevesvehicles.client.rendering.models.ModelLiquidDrainer;
import vswe.stevesvehicles.client.rendering.models.ModelLiquidSensors;
import vswe.stevesvehicles.client.rendering.models.ModelMobDetector;
import vswe.stevesvehicles.client.rendering.models.ModelNote;
import vswe.stevesvehicles.client.rendering.models.ModelPigHead;
import vswe.stevesvehicles.client.rendering.models.ModelPigHelmet;
import vswe.stevesvehicles.client.rendering.models.ModelPigTail;
import vswe.stevesvehicles.client.rendering.models.ModelPumpkinHull;
import vswe.stevesvehicles.client.rendering.models.ModelPumpkinHullTop;
import vswe.stevesvehicles.client.rendering.models.ModelRailer;
import vswe.stevesvehicles.client.rendering.models.ModelSeat;
import vswe.stevesvehicles.client.rendering.models.ModelShield;
import vswe.stevesvehicles.client.rendering.models.ModelShootingRig;
import vswe.stevesvehicles.client.rendering.models.ModelSideChests;
import vswe.stevesvehicles.client.rendering.models.ModelSideTanks;
import vswe.stevesvehicles.client.rendering.models.ModelSniperRifle;
import vswe.stevesvehicles.client.rendering.models.ModelSolarPanelBase;
import vswe.stevesvehicles.client.rendering.models.ModelSolarPanelHeads;
import vswe.stevesvehicles.client.rendering.models.ModelToolPlate;
import vswe.stevesvehicles.client.rendering.models.ModelTopChest;
import vswe.stevesvehicles.client.rendering.models.ModelTopTank;
import vswe.stevesvehicles.client.rendering.models.ModelTorchplacer;
import vswe.stevesvehicles.client.rendering.models.ModelTrackRemover;
import vswe.stevesvehicles.client.rendering.models.ModelVehicle;
import vswe.stevesvehicles.client.rendering.models.ModelWheel;
import vswe.stevesvehicles.client.rendering.models.ModelWoodCutter;
import vswe.stevesvehicles.modules.data.ModuleDataGroup;
import vswe.stevesvehicles.modules.hull.*;
import vswe.stevesvehicles.old.Helpers.*;
import vswe.stevesvehicles.old.Items.ModItems;
import vswe.stevesvehicles.old.Modules.Addons.*;
import vswe.stevesvehicles.old.Modules.Workers.*;
import vswe.stevesvehicles.old.StevesVehicles;
import vswe.stevesvehicles.vehicles.entities.EntityModularCart;
import vswe.stevesvehicles.modules.ModuleBase;
import vswe.stevesvehicles.old.Modules.Addons.Mobdetectors.ModuleAnimal;
import vswe.stevesvehicles.old.Modules.Addons.Mobdetectors.ModuleBat;
import vswe.stevesvehicles.old.Modules.Addons.Mobdetectors.ModuleMonster;
import vswe.stevesvehicles.old.Modules.Addons.Mobdetectors.ModulePlayer;
import vswe.stevesvehicles.old.Modules.Addons.Mobdetectors.ModuleVillager;
import vswe.stevesvehicles.old.Modules.Addons.Plants.ModuleModTrees;
import vswe.stevesvehicles.old.Modules.Addons.Plants.ModuleNetherwart;
import vswe.stevesvehicles.old.Modules.Addons.Plants.ModulePlantSize;
import vswe.stevesvehicles.old.Modules.Addons.Projectiles.ModuleCake;
import vswe.stevesvehicles.old.Modules.Addons.Projectiles.ModuleEgg;
import vswe.stevesvehicles.old.Modules.Addons.Projectiles.ModuleFireball;
import vswe.stevesvehicles.old.Modules.Addons.Projectiles.ModulePotion;
import vswe.stevesvehicles.old.Modules.Addons.Projectiles.ModuleSnowball;
import vswe.stevesvehicles.old.Modules.Engines.ModuleCheatEngine;
import vswe.stevesvehicles.old.Modules.Engines.ModuleCoalStandard;
import vswe.stevesvehicles.old.Modules.Engines.ModuleCoalTiny;
import vswe.stevesvehicles.old.Modules.Engines.ModuleEngine;
import vswe.stevesvehicles.old.Modules.Engines.ModuleSolarBasic;
import vswe.stevesvehicles.old.Modules.Engines.ModuleSolarCompact;
import vswe.stevesvehicles.old.Modules.Engines.ModuleSolarStandard;
import vswe.stevesvehicles.old.Modules.Engines.ModuleThermalAdvanced;
import vswe.stevesvehicles.old.Modules.Engines.ModuleThermalStandard;
import vswe.stevesvehicles.modules.hull.HullGalgadorian;
import vswe.stevesvehicles.modules.hull.HullPig;
import vswe.stevesvehicles.modules.hull.HullPumpkin;
import vswe.stevesvehicles.modules.hull.HullReinforced;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleAdvControl;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleArcade;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleCage;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleCakeServer;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleCakeServerDynamite;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleCleaner;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleDynamite;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleExperience;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleFirework;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleFlowerRemover;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleMilker;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleNote;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleSeat;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleShooter;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleShooterAdv;
import vswe.stevesvehicles.old.Modules.Storages.ModuleStorage;
import vswe.stevesvehicles.old.Modules.Storages.Chests.ModuleEggBasket;
import vswe.stevesvehicles.old.Modules.Storages.Chests.ModuleExtractingChests;
import vswe.stevesvehicles.old.Modules.Storages.Chests.ModuleFrontChest;
import vswe.stevesvehicles.old.Modules.Storages.Chests.ModuleGiftStorage;
import vswe.stevesvehicles.old.Modules.Storages.Chests.ModuleInternalStorage;
import vswe.stevesvehicles.old.Modules.Storages.Chests.ModuleSideChests;
import vswe.stevesvehicles.old.Modules.Storages.Chests.ModuleTopChest;
import vswe.stevesvehicles.old.Modules.Storages.Tanks.ModuleAdvancedTank;
import vswe.stevesvehicles.old.Modules.Storages.Tanks.ModuleCheatTank;
import vswe.stevesvehicles.old.Modules.Storages.Tanks.ModuleFrontTank;
import vswe.stevesvehicles.old.Modules.Storages.Tanks.ModuleInternalTank;
import vswe.stevesvehicles.old.Modules.Storages.Tanks.ModuleOpenTank;
import vswe.stevesvehicles.old.Modules.Storages.Tanks.ModuleSideTanks;
import vswe.stevesvehicles.old.Modules.Storages.Tanks.ModuleTopTank;
import vswe.stevesvehicles.old.Modules.Workers.Tools.ModuleDrillDiamond;
import vswe.stevesvehicles.old.Modules.Workers.Tools.ModuleDrillGalgadorian;
import vswe.stevesvehicles.old.Modules.Workers.Tools.ModuleDrillHardened;
import vswe.stevesvehicles.old.Modules.Workers.Tools.ModuleDrillIron;
import vswe.stevesvehicles.old.Modules.Workers.Tools.ModuleFarmerDiamond;
import vswe.stevesvehicles.old.Modules.Workers.Tools.ModuleFarmerGalgadorian;
import vswe.stevesvehicles.old.Modules.Workers.Tools.ModuleTool;
import vswe.stevesvehicles.old.Modules.Workers.Tools.ModuleWoodcutterDiamond;
import vswe.stevesvehicles.old.Modules.Workers.Tools.ModuleWoodcutterGalgadorian;
import vswe.stevesvehicles.old.Modules.Workers.Tools.ModuleWoodcutterHardened;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.vehicles.versions.VehicleVersion;

public class ModuleData {


	private static HashMap<Byte,ModuleData> moduleList;		
	private static Class[] moduleGroups;
	private static Localization.MODULE_INFO[] moduleGroupNames;
	
	public static HashMap<Byte,ModuleData> getList() {
		return moduleList;
	}
	
	public static Collection<ModuleData> getModules() {
		return getList().values();
	}
	
	public static void init() {
		String planks = "plankWood"; //ore dict
		String wood = "logWood"; //ore dict
		ItemStack woodSingleSlab = new ItemStack(Blocks.wooden_slab, 1, -1);
		ItemStack bonemeal = new ItemStack(Items.dye, 1, 15);


		
		moduleGroups = new Class[] {ModuleHull.class, ModuleEngine.class, ModuleTool.class,ModuleStorage.class, ModuleAddon.class};
		moduleGroupNames = new Localization.MODULE_INFO[] {Localization.MODULE_INFO.HULL_CATEGORY, Localization.MODULE_INFO.ENGINE_CATEGORY, Localization.MODULE_INFO.TOOL_CATEGORY, Localization.MODULE_INFO.STORAGE_CATEGORY, Localization.MODULE_INFO.ADDON_CATEGORY, Localization.MODULE_INFO.ATTACHMENT_CATEGORY};
	
		moduleList = new HashMap<Byte,ModuleData> ();
		

		

		new ModuleData(7, "Torch Placer", ModuleTorch.class,14).addSides(new SIDE[] {SIDE.RIGHT, SIDE.LEFT})
			.addRecipe(new Object[][] {
				{ComponentTypes.TRI_TORCH.getItemStack(),null, ComponentTypes.TRI_TORCH.getItemStack()},
				{Items.iron_ingot,null,Items.iron_ingot},
				{Items.iron_ingot,Items.iron_ingot,Items.iron_ingot}
			});				
		
		
		


		
		ModuleData cleaner = new ModuleData(30, "Cleaning Machine", ModuleCleaner.class,23).addSide(SIDE.CENTER)		
			.addRecipe(new Object[][] {{ComponentTypes.CLEANING_TUBE.getItemStack(), ComponentTypes.CLEANING_CORE.getItemStack(), ComponentTypes.CLEANING_TUBE.getItemStack()},
									   {ComponentTypes.CLEANING_TUBE.getItemStack(), null, ComponentTypes.CLEANING_TUBE.getItemStack()},
									   {ComponentTypes.CLEANING_TUBE.getItemStack(), null , ComponentTypes.CLEANING_TUBE.getItemStack()}
									  });	
							  
		addNemesis(frontChest, cleaner);

		
		new ModuleData(31, "Dynamite Carrier", ModuleDynamite.class,3).addSide(SIDE.TOP)
			.addRecipe(new Object[][]{{null, ComponentTypes.DYNAMITE.getItemStack(), null},
                    {ComponentTypes.DYNAMITE.getItemStack(), Items.flint_and_steel, ComponentTypes.DYNAMITE.getItemStack()},
                    {null, ComponentTypes.DYNAMITE.getItemStack(), null}
            });
		
		new ModuleData(32, "Divine Shield", ModuleShield.class, 60)			
			.addRecipe(new Object[][] {{Blocks.obsidian,  ComponentTypes.REFINED_HARDENER.getItemStack(),  Blocks.obsidian},
									   {ComponentTypes.REFINED_HARDENER.getItemStack(), Blocks.diamond_block, ComponentTypes.REFINED_HARDENER.getItemStack()},
									   {Blocks.obsidian,  ComponentTypes.REFINED_HARDENER.getItemStack(), Blocks.obsidian}
									  });

		
		ModuleData melter = new ModuleData(33, "Melter", ModuleMelter.class,10)
			.addRecipe(new Object[][] {{Blocks.nether_brick,  Blocks.glowstone,  Blocks.nether_brick},
									   {Items.glowstone_dust, Blocks.furnace, Items.glowstone_dust},
									   {Blocks.nether_brick,  Blocks.glowstone, Blocks.nether_brick}
									  });
		ModuleData extrememelter = new ModuleData(34, "Extreme Melter", ModuleMelterExtreme.class,19)
			.addRecipe(new Object[][] {{Blocks.nether_brick,  Blocks.obsidian,  Blocks.nether_brick},
									   {melter.getItemStack(), Items.lava_bucket, melter.getItemStack()},
									   {Blocks.nether_brick,  Blocks.obsidian, Blocks.nether_brick}
									  });	
		addNemesis(melter, extrememelter);
								  


		//new ModuleData(46, "Commander", realtimeModuleCommand.class, 15);
		
		new ModuleData(49, "Chunk Loader", ModuleChunkLoader.class, 84)
			.addRecipe(new Object[][] {
									   {null, Items.ender_pearl, null},
									   {ComponentTypes.SIMPLE_PCB.getItemStack(), Items.iron_ingot, ComponentTypes.SIMPLE_PCB.getItemStack()},
									   {ComponentTypes.REINFORCED_METAL.getItemStack(), ComponentTypes.ADVANCED_PCB.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack()}
									  });	
		
		
		ModuleData gift = new ModuleData(50, "Gift Storage", ModuleGiftStorage.class, 12) {
			@Override
			public String getModuleInfoText(byte b) {
				if (b == 0) {
					return Localization.MODULE_INFO.STORAGE_EMPTY.translate();
				}else{
					return Localization.MODULE_INFO.GIFT_STORAGE_FULL.translate();
				}				
			}
			
			@Override
			public String getCartInfoText(String name, byte b) {
				if (b == 0) {
					return Localization.MODULE_INFO.STORAGE_EMPTY.translate() + " " + name;
				}else{
					return Localization.MODULE_INFO.STORAGE_FULL.translate() + " " + name;
				}
			}		
		}.addSides(new SIDE[] {SIDE.RIGHT, SIDE.LEFT}).useExtraData((byte)1)			
			.addRecipe(new Object[][] {
				{ComponentTypes.YELLOW_GIFT_RIBBON.getItemStack(),null, ComponentTypes.RED_GIFT_RIBBON.getItemStack()},
				{ComponentTypes.RED_WRAPPING_PAPER.getItemStack(),ComponentTypes.CHEST_LOCK.getItemStack(),ComponentTypes.GREEN_WRAPPING_PAPER.getItemStack()},
				{ComponentTypes.RED_WRAPPING_PAPER.getItemStack(),ComponentTypes.STUFFED_SOCK.getItemStack(),ComponentTypes.GREEN_WRAPPING_PAPER.getItemStack()}
			});			
		if (!StevesVehicles.isChristmas) {
			gift.lock();
		}	
		

	
		ModuleData snowgenerator = new ModuleData(55, "Freezer", ModuleSnowCannon.class, 24)
			.addRecipe(new Object[][] {	{Blocks.snow, Items.water_bucket, Blocks.snow},
										{Items.water_bucket, ComponentTypes.SIMPLE_PCB.getItemStack(), Items.water_bucket},
										{Blocks.snow, Items.water_bucket, Blocks.snow}
									});	
		if (!StevesVehicles.isChristmas) {
			snowgenerator.lock();
		}	
		addNemesis(snowgenerator, melter);
		addNemesis(snowgenerator, extrememelter);
				



		addNemesis(frontTank, cleaner);

		

		tankGroup.add(internalTank).add(sideTank).add(topTank).add(advancedTank).add(frontTank).add(creativeTank).add(topTankOpen);
		

		ModuleData cleanerliquid = new ModuleData(71, "Liquid Cleaner", ModuleLiquidDrainer.class, 30).addSide(SIDE.CENTER).addParent(liquidsensors).addRequirement(tankGroup)
			.addRecipe(new Object[][] {{ComponentTypes.LIQUID_CLEANING_TUBE.getItemStack(), ComponentTypes.LIQUID_CLEANING_CORE.getItemStack(), ComponentTypes.LIQUID_CLEANING_TUBE.getItemStack()},
									   {ComponentTypes.LIQUID_CLEANING_TUBE.getItemStack(), null, ComponentTypes.LIQUID_CLEANING_TUBE.getItemStack()},
									   {ComponentTypes.LIQUID_CLEANING_TUBE.getItemStack(), null , ComponentTypes.LIQUID_CLEANING_TUBE.getItemStack()}
									  });
									  
		addNemesis(frontTank, cleanerliquid);
		addNemesis(frontChest, cleanerliquid);		
		

		ItemStack yellowWool = new ItemStack(Blocks.wool, 1, 4);
		ModuleData eggBasket = new ModuleData(74, "Egg Basket", ModuleEggBasket.class, 14) {
			@Override
			public String getModuleInfoText(byte b) {
				if (b == 0) {
					return Localization.MODULE_INFO.STORAGE_EMPTY.translate();
				}else{
					return Localization.MODULE_INFO.EGG_STORAGE_FULL.translate();
				}				
			}
			
			@Override
			public String getCartInfoText(String name, byte b) {
				if (b == 0) {
					return Localization.MODULE_INFO.STORAGE_EMPTY.translate() + " " + name;
				}else{
					return Localization.MODULE_INFO.STORAGE_FULL.translate() + " " + name;
				}
			}
		}.addSide(SIDE.TOP).useExtraData((byte)1)	
			.addRecipe(new Object[][] {{yellowWool, yellowWool, yellowWool},
									   {ComponentTypes.EXPLOSIVE_EASTER_EGG.getItemStack(), ComponentTypes.CHEST_LOCK.getItemStack(), ComponentTypes.BURNING_EASTER_EGG.getItemStack()},
									   {ComponentTypes.GLISTERING_EASTER_EGG.getItemStack(), ComponentTypes.BASKET.getItemStack() , ComponentTypes.CHOCOLATE_EASTER_EGG.getItemStack()}
									  });
		
		if (!StevesVehicles.isEaster) {
			eggBasket.lock();
		}			
		


		new ModuleData(77, "Power Observer", ModulePowerObserver.class, 12).addRequirement(engineGroup)
			.addRecipe(new Object[][] {{null, Blocks.piston, null},
									   {Items.iron_ingot, Items.redstone, Items.iron_ingot},
									   {Items.redstone, ComponentTypes.ADVANCED_PCB.getItemStack() , Items.redstone}
									  });			
				


		

		
		ModuleDataGroup toolGroup = ModuleDataGroup.getCombinedGroup(Localization.MODULE_INFO.TOOL_GROUP, drillGroup, woodcutterGroup);
		toolGroup.add(farmerGroup);

		
		ModuleDataGroup enchantableGroup = ModuleDataGroup.getCombinedGroup(Localization.MODULE_INFO.TOOL_OR_SHOOTER_GROUP, toolGroup, shooterGroup);
		
		new ModuleData(82, "Enchanter", ModuleEnchants.class, 72).addRequirement(enchantableGroup)
			.addRecipe(new Object[][] {{null, ComponentTypes.GALGADORIAN_METAL.getItemStack(), null},
					   {Items.book, Blocks.enchanting_table, Items.book},
					   {Items.redstone,  ComponentTypes.ADVANCED_PCB.getItemStack(), Items.redstone}
					  });	
		

		n

		new ModuleData(95, "Experience Bank", ModuleExperience.class, 36)
		.addRecipe(new Object[][] {{null, Items.redstone,  null},
				{Items.glowstone_dust, Items.emerald, Items.glowstone_dust},
			   {ComponentTypes.SIMPLE_PCB.getItemStack(), Blocks.cauldron, ComponentTypes.SIMPLE_PCB.getItemStack()}
			  });		
		

	
		new ModuleData(97, "Creative Supplies", ModuleCreativeSupplies.class, 1);
		
		new ModuleData(99, "Cake Server", ModuleCakeServer.class, 10).addSide(SIDE.TOP)
		.addMessage(Localization.MODULE_INFO.ALPHA_MESSAGE)
		.addRecipe(new Object[][]{{null, Items.cake, null},
                {"slabWood", "slabWood", "slabWood"},
                {null, ComponentTypes.SIMPLE_PCB.getItemStack(), null}
        });
		
		ModuleData trickOrTreat = new ModuleData(100, "Trick-or-Treat Cake Server", ModuleCakeServerDynamite.class, 15).addSide(SIDE.TOP)
			.addRecipe(new Object[][] {{null, Items.cake,  null},
					{"slabWood", "slabWood", "slabWood"},
				   {ComponentTypes.DYNAMITE.getItemStack(), ComponentTypes.SIMPLE_PCB.getItemStack(), ComponentTypes.DYNAMITE.getItemStack()}
				  });		
		
		if (!StevesVehicles.isHalloween) {
			trickOrTreat.lock();
		}	
		
		//new ModuleData(98, "Pew Pew", ModuleShooterAdvSide.class, 1);
		
		//new ModuleData(35, "Computer", ModuleComputer.class, 16);
			
			
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
		.addModel("SolarPanels" ,new ModelSolarPanelHeads(4));
	
		moduleList.get((byte)45)
		.addModel("SolarPanelBase",new ModelSolarPanelBase())
		.addModel("SolarPanels",new ModelSolarPanelHeads(2));

		moduleList.get((byte)56)
		.addModel("SolarPanelSide",new ModelCompactSolarPanel());

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
	private ArrayList<Localization.MODULE_INFO> message;
	private HashMap<String,ModelVehicle> models;
	private HashMap<String,ModelVehicle> modelsPlaceholder;
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
	
	protected ModuleData addMessage(Localization.MODULE_INFO s) {
		if (message == null) {
			message = new ArrayList<Localization.MODULE_INFO>();
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
	
	protected ModuleData addModel(String tag, ModelVehicle model) {
		addModel(tag, model, false);
		addModel(tag, model, true);
		return this;
	}
	protected ModuleData addModel(String tag, ModelVehicle model, boolean placeholder) {
		if (placeholder) {
			if (modelsPlaceholder == null) {
				modelsPlaceholder = new HashMap<String,ModelVehicle>();
			}
		
			modelsPlaceholder.put(tag, model);
		}else{
			if (models == null) {
				models = new HashMap<String,ModelVehicle>();
			}
		
			models.put(tag, model);		
		}
		
		return this;
	}
	
	public HashMap<String,ModelVehicle> getModels(boolean placeholder) {
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
		return StatCollector.translateToLocal(getUnlocalizedName());
	}

    public String getUnlocalizedName() {
        return "item." + StevesVehicles.localStart + getRawName() + ".name";
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
		
		if (cart != null && cart.getItem() == ModItems.carts && cart.getTagCompound() != null) {
			NBTTagCompound info = cart.getTagCompound();
			if (info.hasKey("Modules")) {
				
				
				
				byte[] IDs = info.getByteArray("Modules");
				for (int i = 0; i < IDs.length; i++) {
					byte id = IDs[i];
					ItemStack module =  new ItemStack(ModItems.modules, 1, id);

					ModItems.modules.addExtraDataToModule(module, info , i);
					
					
					modules.add(module);
				}
			}
		}
		
		return modules;
	}	
	
	public static ItemStack createModularCart(EntityModularCart parentcart) {
		ItemStack cart = new ItemStack(ModItems.carts, 1);
			
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
			
			ModItems.modules.addExtraDataToModule(save, module , i);
			

		}
		save.setByteArray("Modules", moduleIDs);				
		cart.setTagCompound(save);
		VehicleVersion.addVersion(cart);
		
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
		ItemStack cart = new ItemStack(ModItems.carts, 1);
		
		NBTTagCompound save = new NBTTagCompound();
		byte [] moduleIDs = new byte[modules.size()];
		for (int i = 0; i < moduleIDs.length; i++) {
			moduleIDs[i] =(byte) modules.get(i).getItemDamage();
			
	
			ModItems.modules.addExtraDataToCart(save, modules.get(i) , i);
		}
		save.setByteArray("Modules", moduleIDs);				
		cart.setTagCompound(save);
		VehicleVersion.addVersion(cart);
		
		return cart;		
	}
	
	public static boolean isItemOfModularType(ItemStack itemstack, Class<? extends ModuleBase> validClass) {
		if (itemstack.getItem() == ModItems.modules) {
			ModuleData module = ModItems.modules.getModuleData(itemstack);
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
					RecipeHelper.addRecipe(getItemStack(), recipe);
				}
			}
		}
	}
	
	public ItemStack getItemStack() {
		ItemStack module = new ItemStack(ModItems.modules, 1, id);
		
		if (isUsingExtraData()) {
			NBTTagCompound save = new NBTTagCompound();

			save.setByte("Data", getDefaultExtraData());
			
			module.setTagCompound(save);			
		}
		
		return module;	
	}
	

	
	public static boolean isValidModuleItem(int validGroup, ItemStack itemstack) {
		if (itemstack.getItem() == ModItems.modules) {

			ModuleData module = ModItems.modules.getModuleData(itemstack);
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

    private static final int MAX_MESSAGE_ROW_LENGTH = 30;
	public void addExtraMessage(List list) {
		if (message != null) {
			list.add("");
			for (Localization.MODULE_INFO m : message) {
			    String str = m.translate();
                if (str.length() <= MAX_MESSAGE_ROW_LENGTH) {
                    addExtraMessage(list, str);
                }else{
                    String[] words = str.split(" ");
                    String row = "";
                    for (String word : words) {
                        String next = (row + " " + word).trim();
                        if (next.length() <= MAX_MESSAGE_ROW_LENGTH) {
                            row = next;
                        }else{
                            addExtraMessage(list, row);
                            row = word;
                        }
                    }
                    addExtraMessage(list, row);
                }
			}
		}
	}

    private void addExtraMessage(List list, String str) {
        list.add(ColorHelper.GRAY + "\u00a7o" + str + "\u00a7r");
    }
	
	public void addSpecificInformation(List list) {
		list.add(ColorHelper.LIGHT_GRAY + Localization.MODULE_INFO.MODULAR_COST.translate() + ": " + modularCost);
	}
	
	public final void addInformation(List list, NBTTagCompound compound) {
		addSpecificInformation(list);
		if (compound != null && compound.hasKey("Data")) {
			String extradatainfo = getModuleInfoText(compound.getByte("Data"));
			if (extradatainfo != null) {
				list.add(ColorHelper.WHITE + extradatainfo); 
			}
		}
		

		if (GuiScreen.isShiftKeyDown()) {
			
			if (getRenderingSides() == null || getRenderingSides().size() == 0) {
				list.add(ColorHelper.CYAN + Localization.MODULE_INFO.NO_SIDES.translate());
			}else{
                String sides = "";
				for (int i = 0; i < getRenderingSides().size(); i++) {
					SIDE side = getRenderingSides().get(i);
					
					if(i == 0) {
                        sides += side.toString();
					}else if (i == getRenderingSides().size() - 1) {
                        sides += " " + Localization.MODULE_INFO.AND.translate() + " " + side.toString();
					}else{
                        sides += ", " + side.toString();
					}
				}

				
				list.add(ColorHelper.CYAN + Localization.MODULE_INFO.OCCUPIED_SIDES.translate(sides, String.valueOf(getRenderingSides().size())));
			}
			
			if (getNemesis() != null && getNemesis().size() != 0) {
				if (getRenderingSides() == null || getRenderingSides().size() == 0) {
					list.add(ColorHelper.RED + Localization.MODULE_INFO.CONFLICT_HOWEVER.translate() + ":");
				}else{
					list.add(ColorHelper.RED + Localization.MODULE_INFO.CONFLICT_ALSO.translate() + ":");
				}
				for (ModuleData module : getNemesis()) {
					list.add(ColorHelper.RED + module.getName());
				}
			}
			
			if (parent != null) {
				list.add(ColorHelper.YELLOW + Localization.MODULE_INFO.REQUIREMENT.translate() + " " + parent.getName());
			}
			
			if (getRequirement() != null && getRequirement().size() != 0) {
				for (ModuleDataGroup group : getRequirement()) {
					list.add(ColorHelper.YELLOW + Localization.MODULE_INFO.REQUIREMENT.translate() + " " + group.getCountName() + " " + group.getName());
				}
			}
			
			if (getAllowDuplicate()) {
				list.add(ColorHelper.LIME + Localization.MODULE_INFO.DUPLICATES.translate());
			}
			
		}
		
		list.add(ColorHelper.LIGHT_BLUE + Localization.MODULE_INFO.TYPE.translate() + ": " + moduleGroupNames[groupID].translate());
		addExtraMessage(list);
	}
	
	

	
	//Sides that the different modules will claim that they occupy
	public enum SIDE {
        NONE(Localization.MODULE_INFO.SIDE_NONE),
        TOP(Localization.MODULE_INFO.SIDE_TOP),
        CENTER(Localization.MODULE_INFO.SIDE_CENTER),
        BOTTOM(Localization.MODULE_INFO.SIDE_BOTTOM),
        BACK(Localization.MODULE_INFO.SIDE_BACK),
        LEFT(Localization.MODULE_INFO.SIDE_LEFT),
        RIGHT(Localization.MODULE_INFO.SIDE_RIGHT),
        FRONT(Localization.MODULE_INFO.SIDE_FRONT);

        private Localization.MODULE_INFO name;
        private SIDE(Localization.MODULE_INFO name) {
            this.name = name;
        }


        @Override
        public String toString() {
            return name.translate();
        }
    }
	
	
	public static String checkForErrors(ModuleDataHull hull, ArrayList<ModuleData> modules) {
		//Normal errors here	
		if (getTotalCost(modules) > hull.getCapacity()) {
			return Localization.MODULE_INFO.CAPACITY_ERROR.translate();
		}				
		
		if (!isValidModuleCombo(hull,modules)) {
			return Localization.MODULE_INFO.COMBINATION_ERROR.translate();
		}

		for (int i = 0; i < modules.size(); i++) {
			ModuleData mod1 = modules.get(i);
			if (mod1.getCost() > hull.getComplexityMax()) {
				return Localization.MODULE_INFO.COMPLEXITY_ERROR.translate(mod1.getName());
			}
			if (mod1.getParent() != null && !modules.contains(mod1.getParent())) {
				return Localization.MODULE_INFO.PARENT_ERROR.translate(mod1.getName(), mod1.getParent().getName());
			}
			if (mod1.getNemesis() != null) {
				for (ModuleData nemesis : mod1.getNemesis()) {
					if (modules.contains(nemesis)) {
						return Localization.MODULE_INFO.NEMESIS_ERROR.translate(mod1.getName(), nemesis.getName());
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
						return Localization.MODULE_INFO.PARENT_ERROR.translate(mod1.getName(), group.getCountName() + " " + group.getName());
					}
				}
			}
		
			for (int j = i+1; j < modules.size(); j++) {
				ModuleData mod2 = modules.get(j);
				
				if (mod1 == mod2) {
					if (!mod1.getAllowDuplicate()) {
						return Localization.MODULE_INFO.DUPLICATE_ERROR.translate(mod1.getName());
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
						return Localization.MODULE_INFO.CLASH_ERROR.translate(mod1.getName(), mod2.getName(), clash.toString());
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
	
	private IIcon icon;
		
	@SideOnly(Side.CLIENT)
	public void createIcon(IIconRegister register) {
		icon = register.registerIcon(StevesVehicles.instance.textureHeader + ":" + getRawName() + "_icon");
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon() {
		return icon;
	}
	
}