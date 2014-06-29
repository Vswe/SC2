package vswe.stevesvehicles.module.common.addon.projectile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleFireball extends ModuleProjectile {
	public ModuleFireball(VehicleBase vehicleBase) {
		super(vehicleBase);
	}


	public boolean isValidProjectile(ItemStack item) {
		return item.getItem() == Items.fire_charge;
	}
	public Entity createProjectile(Entity target, ItemStack item) {
		return new EntitySmallFireball(getVehicle().getWorld());
	}

}