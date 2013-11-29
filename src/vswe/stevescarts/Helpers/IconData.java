package vswe.stevescarts.Helpers;

import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;

public class IconData {
	private Icon icon;
	private String texture;
	
	public IconData(Icon icon, String texture) {
		this.icon = icon;
		this.texture = texture;
	}
	
	public Icon getIcon() {
		return icon;
	}
	
	public String getTexture() {
		return texture;
	}
	
	public ResourceLocation getResource() {
		return ResourceHelper.getResourceFromPath(texture);	
	}
}