package vswe.stevesvehicles.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ResourceHelper {

	
	@SuppressWarnings("SpellCheckingInspection")
    public static ResourceLocation getResource(String path) {
		return new ResourceLocation("stevescarts", "textures" + path);
	}
	
	public static ResourceLocation getResourceFromPath(String path) {
		return new ResourceLocation("textures" + path);
	}
	
	public static void bindResource(ResourceLocation resource) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(resource);
	}

	
}
