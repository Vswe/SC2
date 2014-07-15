package vswe.stevesvehicles.client;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.client.ResourceHelper;

public class IconData {
	private IIcon icon;
	private String texture;
	
	public IconData(IIcon icon, String texture) {
		this.icon = icon;
		this.texture = texture;
	}
	
	public IIcon getIcon() {
		return icon;
	}
	
	public String getTexture() {
		return texture;
	}
	
	public ResourceLocation getResource() {
		return ResourceHelper.getResourceFromPath(texture);
	}
}