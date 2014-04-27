package vswe.stevescarts.Modules.Addons.Projectiles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;

public class ModuleFireball extends ModuleProjectile {
	public ModuleFireball(MinecartModular cart) {
		super(cart);
	}


	public boolean isValidProjectile(ItemStack item) {
		return item.getItem().itemID == Item.fireballCharge.itemID;
	}
	public Entity createProjectile(Entity target, ItemStack item) {
		return new EntitySmallFireball(getCart().worldObj);
	}

}