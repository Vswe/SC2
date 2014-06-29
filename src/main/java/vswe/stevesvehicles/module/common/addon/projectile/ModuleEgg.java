package vswe.stevesvehicles.module.common.addon.projectile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.vehicle.VehicleBase;


public class ModuleEgg extends ModuleProjectile {
	public ModuleEgg(VehicleBase vehicleBase) {
		super(vehicleBase);
	}


	public boolean isValidProjectile(ItemStack item) {
		return item.getItem() == Items.egg;
	}
	public Entity createProjectile(Entity target, ItemStack item) {
		return new EntityEgg(getVehicle().getWorld());
	}

}