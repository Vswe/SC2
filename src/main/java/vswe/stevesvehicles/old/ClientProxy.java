package vswe.stevesvehicles.old;

import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import vswe.stevesvehicles.client.rendering.RenderVehicleItem;
import vswe.stevesvehicles.client.rendering.RendererCart;
import vswe.stevesvehicles.module.data.ModuleRegistry;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;
import vswe.stevesvehicles.old.Fancy.FancyPancyLoader;
import vswe.stevesvehicles.old.Helpers.*;
import vswe.stevesvehicles.old.Items.ModItems;
import vswe.stevesvehicles.client.rendering.RendererUpgrade;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy{

	public ClientProxy() {
		new FancyPancyLoader();
	}

	@Override
	public void renderInit() {
		RenderingRegistry.registerEntityRenderingHandler(EntityModularCart.class, new RendererCart());
		RenderingRegistry.registerEntityRenderingHandler(EntityEasterEgg.class, new RenderSnowball(ModItems.component, ComponentTypes.PAINTED_EASTER_EGG.getId()));
		StevesVehicles.instance.blockRenderer = new RendererUpgrade();
		new RenderVehicleItem();
		RenderingRegistry.registerEntityRenderingHandler(EntityCake.class, new RenderSnowball(Items.cake));

        if (StevesVehicles.instance.tradeHandler != null) {
            StevesVehicles.instance.tradeHandler.registerSkin();
        }
        for (ModuleData moduleData : ModuleRegistry.getAllModules()) {
            moduleData.loadModels();
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
