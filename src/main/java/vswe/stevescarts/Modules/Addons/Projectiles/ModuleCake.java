package vswe.stevescarts.Modules.Addons.Projectiles;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.EntityCake;


public class ModuleCake extends ModuleProjectile {
	public ModuleCake(MinecartModular cart) {
		super(cart);
	}


	public boolean isValidProjectile(ItemStack item) {
		return item.getItem() == Items.cake;
	}
	public Entity createProjectile(Entity target, ItemStack item) {
		return new EntityCake(getCart().worldObj);
	}

}