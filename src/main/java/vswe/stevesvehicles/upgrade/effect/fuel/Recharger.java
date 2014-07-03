package vswe.stevesvehicles.upgrade.effect.fuel;

import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.TileEntities.TileEntityUpgrade;
import vswe.stevesvehicles.upgrade.IEffectInfo;
import vswe.stevesvehicles.upgrade.Upgrade;

public class Recharger extends RechargerBase {

	protected int amount;
	
	public Recharger(TileEntityUpgrade upgrade, Integer amount) {
        super(upgrade);
		this.amount = amount;
	}
	
	@Override
	protected int getAmount() {
		return amount;
	}
	
	@Override
	protected boolean canGenerate() {
		return true;
	}



}
