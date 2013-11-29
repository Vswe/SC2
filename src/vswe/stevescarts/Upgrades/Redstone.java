package vswe.stevescarts.Upgrades;

import vswe.stevescarts.TileEntities.TileEntityUpgrade;

public class Redstone extends BaseEffect {



	public Redstone() {
		super();
	}
	
	@Override
	public String getName() {
		return "Enables redstone controlled assembling";
	}
	
	@Override
	public void update(TileEntityUpgrade upgrade) {
		if (upgrade.worldObj.isBlockIndirectlyGettingPowered(upgrade.xCoord, upgrade.yCoord, upgrade.zCoord)) {
			if (upgrade.getMaster() != null) {
				upgrade.getMaster().doAssemble();
			}
		}
	}
	

}