package vswe.stevesvehicles.old;

import cpw.mods.fml.common.network.FMLEventChannel;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.RecipeSorter;
import org.apache.logging.log4j.Logger;
import vswe.stevesvehicles.block.ModBlocks;
import vswe.stevesvehicles.detector.modulestate.registry.ModuleStateRegistry;
import vswe.stevesvehicles.module.data.registry.ModuleRegistry;
import vswe.stevesvehicles.network.PacketHandler;
import vswe.stevesvehicles.recipe.ModuleRecipeShaped;
import vswe.stevesvehicles.recipe.ModuleRecipeShapeless;
import vswe.stevesvehicles.registry.RegistrySynchronizer;
import vswe.stevesvehicles.tab.CreativeTabLoader;
import vswe.stevesvehicles.upgrade.registry.UpgradeRegistry;
import vswe.stevesvehicles.vehicle.VehicleRegistry;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;
import vswe.stevesvehicles.old.Helpers.CraftingHandler;
import vswe.stevesvehicles.old.Helpers.EntityCake;
import vswe.stevesvehicles.old.Helpers.EntityEasterEgg;
import vswe.stevesvehicles.old.Helpers.GeneratedInfo;
import vswe.stevesvehicles.old.Helpers.GiftItem;
import vswe.stevesvehicles.old.Helpers.TradeHandler;
import vswe.stevesvehicles.old.Helpers.WoodFuelHandler;
import vswe.stevesvehicles.old.Items.*;
import vswe.stevesvehicles.old.Listeners.ChunkListener;
import vswe.stevesvehicles.old.Listeners.MobDeathListener;
import vswe.stevesvehicles.old.Listeners.MobInteractListener;
import vswe.stevesvehicles.old.Listeners.OverlayRenderer;
import vswe.stevesvehicles.old.Listeners.PlayerSleepListener;
import vswe.stevesvehicles.old.Listeners.TicketListener;
import vswe.stevesvehicles.tileentity.TileEntityCargo;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;

@Mod(modid = "StevesVehicles", name = "Steve's Vehicles", version = GeneratedInfo.version)
public class StevesVehicles {
	public static boolean hasGreenScreen = false;
	public static boolean isChristmas = false;
	public static boolean isHalloween = false;
	public static boolean isEaster = false;	
	public static boolean freezeCartSimulation = false;
	public static boolean renderSteve = false;
	public static boolean arcadeDevOperator = false;

    public static final String CHANNEL = "SC2";
	
	public final String texturePath = "/assets/stevescarts/textures";
	//public final String soundPath = "/assets/stevescarts/sounds";
	public final String textureHeader = "stevescarts";
	public static final String localStart = "SC2:";

	@SidedProxy(clientSide = "vswe.stevesvehicles.old.ClientProxy", serverSide = "vswe.stevesvehicles.old.CommonProxy")
	public static CommonProxy proxy;
	@Instance("StevesVehicles")
	public static StevesVehicles instance;


	
	public ISimpleBlockRenderingHandler blockRenderer;

	public int maxDynamites = 50;
	public boolean useArcadeSounds;
	public boolean useArcadeMobSounds;

    public static FMLEventChannel packetHandler;

	public static Logger logger;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
        //TODO make sure everything here is called in the correct order and still allow other mods to hook into it and still maintaining the correct sequence
        packetHandler = NetworkRegistry.INSTANCE.newEventDrivenChannel(CHANNEL);

        VehicleRegistry.init();
        ModuleRegistry.init();
        UpgradeRegistry.init();
        ModuleStateRegistry.init();

        CreativeTabLoader.init();
		logger = event.getModLog();

		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		maxDynamites = Math.min(maxDynamites, config.get("Settings", "MaximumNumberOfDynamites", maxDynamites).getInt(maxDynamites));	
		useArcadeSounds = config.get("Settings", "useArcadeSounds", true).getBoolean(true);	
		useArcadeMobSounds = config.get("Settings", "useTetrisMobSounds", true).getBoolean(true);	

        ModItems.preBlockInit(config);
        ItemBlockStorage.init();
        ModBlocks.init();
        ModItems.postBlockInit(config);

        initCart(0, EntityModularCart.class);


		EntityRegistry.registerModEntity(EntityEasterEgg.class, "Egg.Vswe", 2, instance, 80, 3, true);
		EntityRegistry.registerModEntity(EntityCake.class, "Cake.Vswe", 3, instance, 80, 3, true);
		proxy.soundInit();

        config.save();
	}

    public TradeHandler tradeHandler;

    @EventHandler
	public void load(FMLInitializationEvent evt) {
        CreativeTabLoader.postInit();
        RecipeSorter.register("steves_vehicles:shaped", ModuleRecipeShaped.class, RecipeSorter.Category.SHAPED, "before:minecraft:shaped before:steves_vehicles:shapeless");
        RecipeSorter.register("steves_vehicles:shapeless", ModuleRecipeShapeless.class, RecipeSorter.Category.SHAPELESS, "after:steves_vehicles:shaped");

        packetHandler.register(new PacketHandler());

	
		new OverlayRenderer();
		new TicketListener();
		new ChunkListener();
		new CraftingHandler();
		new WoodFuelHandler();
		if (isChristmas) {
            tradeHandler = new TradeHandler();
			new MobDeathListener();
			new MobInteractListener();
			new PlayerSleepListener();
		}
        new RegistrySynchronizer();

		GiftItem.init();

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		proxy.renderInit();

		TileEntityCargo.loadSelectionSettings();

        ModItems.addRecipes();
        ModBlocks.addRecipes();

	}
	
	private void initCart(int ID, Class<? extends EntityModularCart> cart) {
		EntityRegistry.registerModEntity(cart, "Minecart.Vswe." + ID, ID, instance, 80, 3, true);
		//MinecartRegistry.registerMinecart(cart, new ItemStack(cart, 1, ID));
	}
}