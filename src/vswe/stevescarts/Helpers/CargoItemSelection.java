package vswe.stevescarts.Helpers;
import net.minecraft.item.ItemStack;
	
public class CargoItemSelection {

	private Class validSlot;
	private ItemStack icon;
	private String name;
	
	public CargoItemSelection(String name, Class validSlot, ItemStack icon) {
		this.name = name;
		this.validSlot = validSlot;
		this.icon = icon;
	}
	
	public Class getValidSlot() {
		return validSlot;
	}
	
	public ItemStack getIcon() {
		return icon;
	}
	
	public String getName() {
		return	name;
	}
	
}