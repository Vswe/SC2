package vswe.stevesvehicles.old.Modules.Addons.Projectiles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.vehicles.entities.EntityModularCart;

public class ModuleSnowball extends ModuleProjectile {
	public ModuleSnowball(EntityModularCart cart) {
		super(cart);
	}


	public boolean isValidProjectile(ItemStack item) {
		return item.getItem() == Items.snowball;
	}
	public Entity createProjectile(Entity target, ItemStack item) {
		return new EntitySnowball(getCart().worldObj);
	}

}