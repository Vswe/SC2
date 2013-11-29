package vswe.stevescarts;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.CapeHandler;
import vswe.stevescarts.Helpers.EntityCake;
import vswe.stevescarts.Helpers.EntityEasterEgg;
import vswe.stevescarts.Helpers.SoundHandler;
import vswe.stevescarts.Items.Items;
import vswe.stevescarts.Renders.RendererMinecart;
import vswe.stevescarts.Renders.RendererMinecartItem;
import vswe.stevescarts.Renders.RendererUpgrade;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import vswe.stevescarts.ModuleData.ModuleData;
public class ClientProxy extends CommonProxy{

	public ClientProxy() {
		new CapeHandler();
	}

	@Override
	public void renderInit() {
		RenderingRegistry.registerEntityRenderingHandler(MinecartModular.class, new RendererMinecart());
		RenderingRegistry.registerEntityRenderingHandler(EntityEasterEgg.class, new RenderSnowball(Items.component, 70));
		StevesCarts.instance.blockRenderer = new RendererUpgrade();
		new RendererMinecartItem();
		RenderingRegistry.registerEntityRenderingHandler(EntityCake.class, new RenderSnowball(Item.cake));
		ModuleData.initModels();
	}
	
	@Override
	public void soundInit() {
		new SoundHandler();
	}

	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}
	
}
