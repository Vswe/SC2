package vswe.stevesvehicles.upgrade.effect.fuel;

import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.TileEntities.TileEntityUpgrade;
import vswe.stevesvehicles.upgrade.IEffectInfo;
import vswe.stevesvehicles.upgrade.Upgrade;

public class Solar extends RechargerBase {

    public Solar(TileEntityUpgrade upgrade) {
        super(upgrade);
    }

    protected int getAmount() {
		if (upgrade.yCoord > upgrade.getMaster().yCoord) {
			return 400;
		}else if(upgrade.yCoord < upgrade.getMaster().yCoord) {
			return 0;
		}else{
			return 240;
		}
	}
	
	@Override
	protected boolean canGenerate() {
		return upgrade.getWorldObj().getBlockLightValue(upgrade.xCoord, upgrade.yCoord, upgrade.zCoord) == 15 &&
				upgrade.getWorldObj().canBlockSeeTheSky(upgrade.xCoord, upgrade.yCoord+1, upgrade.zCoord);
	}


}
