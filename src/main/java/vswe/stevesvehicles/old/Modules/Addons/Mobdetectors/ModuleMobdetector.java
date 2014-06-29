package vswe.stevesvehicles.old.Modules.Addons.Mobdetectors;
import net.minecraft.entity.Entity;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;
import vswe.stevesvehicles.old.Modules.Addons.ModuleAddon;

public abstract class ModuleMobdetector extends ModuleAddon {
	public ModuleMobdetector(EntityModularCart cart) {
		super(cart);
	}

	public abstract String getName();
	public abstract boolean isValidTarget(Entity target);

}