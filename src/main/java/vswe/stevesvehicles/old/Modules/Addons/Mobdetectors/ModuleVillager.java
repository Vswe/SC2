package vswe.stevesvehicles.old.Modules.Addons.Mobdetectors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.passive.EntityVillager;
import vswe.stevesvehicles.vehicles.entities.EntityModularCart;
import vswe.stevesvehicles.old.Helpers.Localization;

public class ModuleVillager extends ModuleMobdetector {
	public ModuleVillager(EntityModularCart cart) {
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