package vswe.stevesvehicles.module.common.addon.mobdetector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityWolf;
import vswe.stevesvehicles.localization.entry.module.LocalizationShooter;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleMonster extends ModuleEntityDetector {
	public ModuleMonster(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	public String getName() {
		return LocalizationShooter.MONSTER_TITLE.translate();
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