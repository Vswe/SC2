package vswe.stevesvehicles.upgrade.effect.time;


import vswe.stevesvehicles.old.TileEntities.TileEntityUpgrade;
import vswe.stevesvehicles.upgrade.effect.BaseEffect;

public class TimeFlat extends BaseEffect {

	private int ticks;
	public TimeFlat(TileEntityUpgrade upgrade, Integer ticks) {
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