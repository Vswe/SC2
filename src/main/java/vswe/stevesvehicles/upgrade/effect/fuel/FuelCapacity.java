package vswe.stevesvehicles.upgrade.effect.fuel;


import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.TileEntities.TileEntityUpgrade;
import vswe.stevesvehicles.upgrade.IEffectInfo;
import vswe.stevesvehicles.upgrade.Upgrade;
import vswe.stevesvehicles.upgrade.effect.BaseEffect;

public class FuelCapacity extends BaseEffect {

	private int capacity;
	public FuelCapacity(TileEntityUpgrade upgrade, Integer capacity) {
		super(upgrade);
		this.capacity = capacity;
	}

	public int getFuelCapacity() {
		return capacity;
	}


}