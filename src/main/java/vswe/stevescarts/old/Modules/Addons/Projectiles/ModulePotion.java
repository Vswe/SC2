package vswe.stevescarts.old.Modules.Addons.Projectiles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.vehicles.entities.EntityModularCart;

public class ModulePotion extends ModuleProjectile {
	public ModulePotion(EntityModularCart cart) {
		super(cart);
	}


	public boolean isValidProjectile(ItemStack item) {
		return item.getItem() == Items.potionitem && ItemPotion.isSplash(item.getItemDamage());
	}
	public Entity createProjectile(Entity target, ItemStack item) {
		EntityPotion potion = new EntityPotion(getCart().worldObj);
		potion.setPotionDamage(item.getItemDamage());
		return potion;
	}

}