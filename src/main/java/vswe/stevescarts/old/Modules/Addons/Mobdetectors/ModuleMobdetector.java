package vswe.stevescarts.old.Modules.Addons.Mobdetectors;
import net.minecraft.entity.Entity;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.Modules.Addons.ModuleAddon;

public abstract class ModuleMobdetector extends ModuleAddon {
	public ModuleMobdetector(EntityModularCart cart) {
		super(cart);
	}

	public abstract String getName();
	public abstract boolean isValidTarget(Entity target);

}