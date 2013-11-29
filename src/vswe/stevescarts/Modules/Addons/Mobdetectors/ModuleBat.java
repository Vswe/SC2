package vswe.stevescarts.Modules.Addons.Mobdetectors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityBat;
import vswe.stevescarts.Carts.MinecartModular;

public class ModuleBat extends ModuleMobdetector {
	public ModuleBat(MinecartModular cart) {
		super(cart);
	}

	public String getName() {
		return "Bats";
	}
	public boolean isValidTarget(Entity target) {
		return
		(
			target instanceof EntityBat		
		)
		;
	}
}