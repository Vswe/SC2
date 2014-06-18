package vswe.stevescarts.Upgrades;

import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;

public class Redstone extends BaseEffect {



	public Redstone() {
		super();
	}
	
	@Override
	public String getName() {
		return Localization.UPGRADES.REDSTONE.translate();
	}
	
	@Override
	public void update(TileEntityUpgrade upgrade) {
		if (upgrade.getWorldObj().isBlockIndirectlyGettingPowered(upgrade.xCoord, upgrade.yCoord, upgrade.zCoord)) {
			if (upgrade.getMaster() != null) {
				upgrade.getMaster().doAssemble();
			}
		}
	}
	

}