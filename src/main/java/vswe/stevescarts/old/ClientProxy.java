package vswe.stevescarts.old;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.Fancy.FancyPancyLoader;
import vswe.stevescarts.old.Helpers.*;
import vswe.stevescarts.old.Items.ModItems;
import vswe.stevescarts.old.Renders.RendererMinecart;
import vswe.stevescarts.old.Renders.RendererMinecartItem;
import vswe.stevescarts.old.Renders.RendererUpgrade;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import vswe.stevescarts.old.ModuleData.ModuleData;
public class ClientProxy extends CommonProxy{

	public ClientProxy() {
		new FancyPancyLoader();
	}

	@Override
	public void renderInit() {
		RenderingRegistry.registerEntityRenderingHandler(EntityModularCart.class, new RendererMinecart());
		RenderingRegistry.registerEntityRenderingHandler(EntityEasterEgg.class, new RenderSnowball(ModItems.component, ComponentTypes.PAINTED_EASTER_EGG.getId()));
		StevesCarts.instance.blockRenderer = new RendererUpgrade();
		new RendererMinecartItem();
		RenderingRegistry.registerEntityRenderingHandler(EntityCake.class, new RenderSnowball(Items.cake));
		ModuleData.initModels();
        if (StevesCarts.instance.tradeHandler != null) {
            StevesCarts.instance.tradeHandler.registerSkin();
        }
	}
	
	@Override
	public void soundInit() {
		new SoundHandler();
        new MinecartSoundMuter();
	}

	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}
	
}
