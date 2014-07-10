package vswe.stevesvehicles.upgrade.effect.fuel;

import vswe.stevesvehicles.tileentity.TileEntityUpgrade;
import vswe.stevesvehicles.upgrade.effect.BaseEffect;

public abstract class RechargerBase extends BaseEffect {


    protected RechargerBase(TileEntityUpgrade upgrade) {
        super(upgrade);
    }

    private int cooldown;

    @Override
	public void update() {
		if (!upgrade.getWorldObj().isRemote && canGenerate()) {
			if (cooldown >= 1200 / getAmount()) {
                cooldown = 0;
				upgrade.getMaster().increaseFuel(1);
			}else{
                cooldown++;
			}
		}
	}
	
	protected abstract boolean canGenerate();
	protected abstract int getAmount();
	
	@Override
	public void init() {
        cooldown = 0;
	}

	
}
