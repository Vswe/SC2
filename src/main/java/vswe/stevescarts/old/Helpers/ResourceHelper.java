package vswe.stevescarts.old.Helpers;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ResourceHelper {

	
	public static ResourceLocation getResource(String path) {
		return new ResourceLocation("stevescarts", "textures" + path);
	}
	
	public static ResourceLocation getResourceFromPath(String path) {
		return new ResourceLocation("textures" + path);
	}
	
	public static void bindResource(ResourceLocation resource) {
		if (resource == null) {
			//System.out.println("null");
		}else{
            Minecraft.getMinecraft().getTextureManager().bindTexture(resource);
		}
	}

	private static HashMap<String, ResourceLocation> resources = new HashMap<String, ResourceLocation>();
	
	public static void bindResource(String path) {
		if (resources.containsKey(path)) {
			bindResource(resources.get(path));
		}else{
			ResourceLocation resource = getResource(path);
			resources.put(path, resource);
			bindResource(resource);
		}
	}

	private static HashMap<String, ResourceLocation> pathResources = new HashMap<String, ResourceLocation>();
	
	public static void bindResourcePath(String path) {
		if (pathResources.containsKey(path)) {
			bindResource(pathResources.get(path));
		}else{
			ResourceLocation resource = getResourceFromPath(path);
			pathResources.put(path, resource);
			bindResource(resource);
		}
		
	}
	
}
