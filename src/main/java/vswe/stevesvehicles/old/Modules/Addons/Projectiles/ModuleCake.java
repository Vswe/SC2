package vswe.stevesvehicles.old.Modules.Addons.Projectiles;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;
import vswe.stevesvehicles.old.Helpers.EntityCake;


public class ModuleCake extends ModuleProjectile {
	public ModuleCake(EntityModularCart cart) {
		super(cart);
	}


	public boolean isValidProjectile(ItemStack item) {
		return item.getItem() == Items.cake;
	}
	public Entity createProjectile(Entity target, ItemStack item) {
		return new EntityCake(getCart().worldObj);
	}

}