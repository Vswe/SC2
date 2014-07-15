package vswe.stevesvehicles.module.common.addon.projectile;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.vehicle.VehicleBase;


public class ModuleCake extends ModuleProjectile {
	public ModuleCake(VehicleBase vehicleBase) {
		super(vehicleBase);
	}


	public boolean isValidProjectile(ItemStack item) {
		return item.getItem() == Items.cake;
	}
	public Entity createProjectile(Entity target, ItemStack item) {
		return new EntityCake(getVehicle().getWorld());
	}

}