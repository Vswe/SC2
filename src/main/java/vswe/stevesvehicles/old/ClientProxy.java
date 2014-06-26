package vswe.stevesvehicles.old;
import net.minecraft.client.renderer.entity.RenderMinecart;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import vswe.stevesvehicles.client.rendering.RenderVehicleItem;
import vswe.stevesvehicles.vehicles.entities.EntityModularCart;
import vswe.stevesvehicles.old.Fancy.FancyPancyLoader;
import vswe.stevesvehicles.old.Helpers.*;
import vswe.stevesvehicles.old.Items.ModItems;
import vswe.stevesvehicles.client.rendering.RendererUpgrade;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import vswe.stevesvehicles.old.ModuleData.ModuleData;
public class ClientProxy extends CommonProxy{

	public ClientProxy() {
		new FancyPancyLoader();
	}

	@Override
	public void renderInit() {
		RenderingRegistry.registerEntityRenderingHandler(EntityModularCart.class, new RenderMinecart());
		RenderingRegistry.registerEntityRenderingHandler(EntityEasterEgg.class, new RenderSnowball(ModItems.component, ComponentTypes.PAINTED_EASTER_EGG.getId()));
		StevesVehicles.instance.blockRenderer = new RendererUpgrade();
		new RenderVehicleItem();
		RenderingRegistry.registerEntityRenderingHandler(EntityCake.class, new RenderSnowball(Items.cake));
		ModuleData.initModels();
        if (StevesVehicles.instance.tradeHandler != null) {
            StevesVehicles.instance.tradeHandler.registerSkin();
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
