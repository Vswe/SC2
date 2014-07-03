package vswe.stevesvehicles.upgrade.effect.time;


import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.TileEntities.TileEntityUpgrade;
import vswe.stevesvehicles.upgrade.IEffectInfo;
import vswe.stevesvehicles.upgrade.Upgrade;
import vswe.stevesvehicles.upgrade.effect.BaseEffect;

public class TimeFlatCart extends BaseEffect {

	private int ticks;
	public TimeFlatCart(TileEntityUpgrade upgrade, Integer ticks) {
		super(upgrade);
		this.ticks = ticks;
	}
	

	protected int getSeconds() {
		return ticks / 20;
	}
	
	public int getTicks() {
		return ticks;
	}


}