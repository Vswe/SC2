package vswe.stevescarts.Modules.Addons.Mobdetectors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.passive.EntityVillager;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;

public class ModuleVillager extends ModuleMobdetector {
	public ModuleVillager(MinecartModular cart) {
		super(cart);
	}

	public String getName() {
		return Localization.MODULES.ADDONS.DETECTOR_VILLAGERS.translate();
	}
	public boolean isValidTarget(Entity target) {
		return
		(
			target instanceof EntityGolem
			||
			target instanceof EntityVillager
		)
		;
	}
}