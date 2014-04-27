package vswe.stevescarts.Modules.Addons.Mobdetectors;
import net.minecraft.entity.Entity;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.Addons.ModuleAddon;

public abstract class ModuleMobdetector extends ModuleAddon {
	public ModuleMobdetector(MinecartModular cart) {
		super(cart);
	}

	public abstract String getName();
	public abstract boolean isValidTarget(Entity target);

}