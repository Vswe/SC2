package vswe.stevesvehicles.old.Upgrades;

import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.TileEntities.TileEntityUpgrade;

public class Solar extends RechargerBase {


	protected int getAmount(TileEntityUpgrade upgrade) {
		if (upgrade.yCoord > upgrade.getMaster().yCoord) {
			return 400;
		}else if(upgrade.yCoord < upgrade.getMaster().yCoord) {
			return 0;
		}else{
			return 240;
		}
	}
	
	@Override
	protected boolean canGenerate(TileEntityUpgrade upgrade) {
		return upgrade.getWorldObj().getBlockLightValue(upgrade.xCoord, upgrade.yCoord, upgrade.zCoord) == 15 &&
				upgrade.getWorldObj().canBlockSeeTheSky(upgrade.xCoord, upgrade.yCoord+1, upgrade.zCoord);
	}

	@Override
	public String getName() {
		return Localization.UPGRADES.SOLAR.translate();
	}
	
	

}
