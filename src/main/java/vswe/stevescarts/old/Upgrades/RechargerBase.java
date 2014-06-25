package vswe.stevescarts.old.Upgrades;

import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.old.TileEntities.TileEntityUpgrade;

public abstract class RechargerBase extends BaseEffect {

	
	@Override
	public void update(TileEntityUpgrade upgrade) {
		if (!upgrade.getWorldObj().isRemote && canGenerate(upgrade)) {
			NBTTagCompound comp = upgrade.getCompound();
			
			if (comp.getShort("GenerateCooldown") >= 1200 / getAmount(upgrade)) {
				comp.setShort("GenerateCooldown", (short)0);
				upgrade.getMaster().increaseFuel(1);
			}else{
				comp.setShort("GenerateCooldown", (short)(comp.getShort("GenerateCooldown") + 1));	
			}
		}
	}
	
	protected abstract boolean canGenerate(TileEntityUpgrade upgrade);
	protected abstract int getAmount(TileEntityUpgrade upgrade);
	
	@Override
	public void init(TileEntityUpgrade upgrade) {
		upgrade.getCompound().setShort("GenerateCooldown", (short)0);		
	}

	
}
