package vswe.stevescarts.Modules.Addons.Projectiles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;

public class ModulePotion extends ModuleProjectile {
	public ModulePotion(MinecartModular cart) {
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