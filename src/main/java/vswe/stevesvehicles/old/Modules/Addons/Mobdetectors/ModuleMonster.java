package vswe.stevesvehicles.old.Modules.Addons.Mobdetectors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityWolf;
import vswe.stevesvehicles.vehicles.entities.EntityModularCart;
import vswe.stevesvehicles.old.Helpers.Localization;

public class ModuleMonster extends ModuleMobdetector {
	public ModuleMonster(EntityModularCart cart) {
		super(cart);
	}

	public String getName() {
		return Localization.MODULES.ADDONS.DETECTOR_MONSTERS.translate();
	}
	public boolean isValidTarget(Entity target) {
		return
		(
			target instanceof EntityMob
			||
			target instanceof EntityDragon
			||
			target instanceof EntityGhast
			||
			target instanceof EntitySlime
			||
			target instanceof EntityEnderCrystal
			||
			(
				(target instanceof EntityWolf)
				&&
				((EntityWolf)target).isAngry()
			)
		)
		&&
		!(target instanceof EntityEnderman)	//projectiles can't hit them anyways
		;
	}
}