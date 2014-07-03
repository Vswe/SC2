package vswe.stevesvehicles.upgrade.effect.fuel;


import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.TileEntities.TileEntityUpgrade;
import vswe.stevesvehicles.upgrade.IEffectInfo;
import vswe.stevesvehicles.upgrade.Upgrade;
import vswe.stevesvehicles.upgrade.effect.BaseEffect;

public class FuelCost extends BaseEffect {

	private float cost;
	public FuelCost(TileEntityUpgrade upgrade, Float cost) {
		super(upgrade);
		this.cost = cost;
	}

	public float getCost() {
		return cost;
	}


}