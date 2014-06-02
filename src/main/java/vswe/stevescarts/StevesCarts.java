package vswe.stevescarts;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;
import vswe.stevescarts.Blocks.*;
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
import vswe.stevescarts.Items.*;
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
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "StevesCarts", name = "Steve's Carts 2", version = GeneratedInfo.version)
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
	public static final String localStart = "SC2:";

	@SidedProxy(clientSide = "vswe.stevescarts.ClientProxy", serverSide = "vswe.stevescarts.CommonProxy")
	public static CommonProxy proxy;
	@Instance("StevesCarts")
	public static StevesCarts instance;

	public static CreativeTabSC2 tabsSC2 = new CreativeTabSC2("SC2Modules");
	public static CreativeTabSC2 tabsSC2Components = new CreativeTabSC2("SC2Items");
	public static CreativeTabSC2 tabsSC2Blocks = new CreativeTabSC2("SC2Blocks");
	
	public ISimpleBlockRenderingHandler blockRenderer;

	public int maxDynamites = 50;
	public boolean useArcadeSounds;
	public boolean useArcadeMobSounds;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		maxDynamites = Math.min(maxDynamites, config.get("Settings", "MaximumNumberOfDynamites", maxDynamites).getInt(maxDynamites));	
		useArcadeSounds = config.get("Settings", "useArcadeSounds", true).getBoolean(true);	
		useArcadeMobSounds = config.get("Settings", "useTetrisMobSounds", true).getBoolean(true);	

        Items.preBlockInit(config);
        ItemBlockStorage.init();
        Blocks.init(config);
        Items.postBlockInit(config);
        AssemblerUpgrade.init();

        initCart(0, MinecartModular.class);


		EntityRegistry.registerModEntity(EntityEasterEgg.class, "Egg.Vswe", 2, instance, 80, 3, true);
		EntityRegistry.registerModEntity(EntityCake.class, "Cake.Vswe", 3, instance, 80, 3, true);
		proxy.soundInit();

        config.save();
	}

    public TradeHandler tradeHandler;

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
		if (isChristmas) {
            tradeHandler = new TradeHandler();
			new MobDeathListener();
			new MobInteractListener();
			new PlayerSleepListener();
		}

		GiftItem.init();

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		proxy.renderInit();

		tabsSC2Blocks.setIcon(new ItemStack(Blocks.CART_ASSEMBLER.getBlock(), 1));

		TileEntityCargo.loadSelectionSettings();

        Items.addRecipes();
        Blocks.addRecipes();
	}
	
	private void initCart(int ID, Class<? extends MinecartModular> cart) {	
		EntityRegistry.registerModEntity(cart, "Minecart.Vswe." + ID, ID, instance, 80, 3, true);
		//MinecartRegistry.registerMinecart(cart, new ItemStack(carts, 1, ID));
	}
}