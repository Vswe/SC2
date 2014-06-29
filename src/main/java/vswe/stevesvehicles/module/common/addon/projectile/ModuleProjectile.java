package vswe.stevesvehicles.module.common.addon.projectile;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.module.common.addon.ModuleAddon;

public abstract class ModuleProjectile extends ModuleAddon {
	public ModuleProjectile(VehicleBase vehicleBase) {
		super(vehicleBase);
	}


	public abstract boolean isValidProjectile(ItemStack item);
	public abstract Entity createProjectile(Entity target, ItemStack item);	

}