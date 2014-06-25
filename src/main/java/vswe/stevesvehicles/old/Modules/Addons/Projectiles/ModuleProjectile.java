package vswe.stevesvehicles.old.Modules.Addons.Projectiles;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.vehicles.entities.EntityModularCart;
import vswe.stevesvehicles.old.Modules.Addons.ModuleAddon;

public abstract class ModuleProjectile extends ModuleAddon {
	public ModuleProjectile(EntityModularCart cart) {
		super(cart);
	}


	public abstract boolean isValidProjectile(ItemStack item);
	public abstract Entity createProjectile(Entity target, ItemStack item);	

}