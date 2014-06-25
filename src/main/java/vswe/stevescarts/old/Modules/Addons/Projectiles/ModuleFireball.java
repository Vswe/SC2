package vswe.stevescarts.old.Modules.Addons.Projectiles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.vehicles.entities.EntityModularCart;

public class ModuleFireball extends ModuleProjectile {
	public ModuleFireball(EntityModularCart cart) {
		super(cart);
	}


	public boolean isValidProjectile(ItemStack item) {
		return item.getItem() == Items.fire_charge;
	}
	public Entity createProjectile(Entity target, ItemStack item) {
		return new EntitySmallFireball(getCart().worldObj);
	}

}