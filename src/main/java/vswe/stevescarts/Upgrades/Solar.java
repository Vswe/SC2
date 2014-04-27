package vswe.stevescarts.Upgrades;

import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Arcade.Unit.UPDATE_RESULT;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;

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
		return upgrade.worldObj.getBlockLightValue(upgrade.xCoord, upgrade.yCoord, upgrade.zCoord) == 15 && 
				upgrade.worldObj.canBlockSeeTheSky(upgrade.xCoord, upgrade.yCoord+1, upgrade.zCoord);		
	}

	@Override
	public String getName() {
		return Localization.UPGRADES.SOLAR.translate();
	}
	
	

}
