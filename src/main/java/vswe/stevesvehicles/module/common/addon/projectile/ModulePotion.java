package vswe.stevesvehicles.module.common.addon.projectile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModulePotion extends ModuleProjectile {
	public ModulePotion(VehicleBase vehicleBase) {
		super(vehicleBase);
	}


	public boolean isValidProjectile(ItemStack item) {
		return item.getItem() == Items.potionitem && ItemPotion.isSplash(item.getItemDamage());
	}
	public Entity createProjectile(Entity target, ItemStack item) {
		EntityPotion potion = new EntityPotion(getVehicle().getWorld());
		potion.setPotionDamage(item.getItemDamage());
		return potion;
	}

}