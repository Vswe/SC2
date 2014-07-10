package vswe.stevesvehicles.upgrade.effect.external;

import vswe.stevesvehicles.tileentity.TileEntityUpgrade;
import vswe.stevesvehicles.upgrade.effect.BaseEffect;

public class Redstone extends BaseEffect {

	public Redstone(TileEntityUpgrade upgrade) {
		super(upgrade);
	}

	
	@Override
	public void update() {
		if (upgrade.getWorldObj().isBlockIndirectlyGettingPowered(upgrade.xCoord, upgrade.yCoord, upgrade.zCoord)) {
			if (upgrade.getMaster() != null) {
				upgrade.getMaster().doAssemble();
			}
		}
	}
	

}