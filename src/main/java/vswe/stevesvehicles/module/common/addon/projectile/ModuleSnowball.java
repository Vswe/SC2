package vswe.stevesvehicles.module.common.addon.projectile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleSnowball extends ModuleProjectile {
	public ModuleSnowball(VehicleBase vehicleBase) {
		super(vehicleBase);
	}


	public boolean isValidProjectile(ItemStack item) {
		return item.getItem() == Items.snowball;
	}
	public Entity createProjectile(Entity target, ItemStack item) {
		return new EntitySnowball(getVehicle().getWorld());
	}

}