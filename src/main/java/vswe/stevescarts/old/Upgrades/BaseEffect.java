package vswe.stevescarts.old.Upgrades;

import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.old.TileEntities.TileEntityUpgrade;

public abstract class BaseEffect {



	public BaseEffect() {
	
	}
	
	public void update(TileEntityUpgrade upgrade) {}
	public void init(TileEntityUpgrade upgrade) {}
	public void removed(TileEntityUpgrade upgrade) {}
	
	public void load(TileEntityUpgrade upgrade, NBTTagCompound compound) {}
	public void save(TileEntityUpgrade upgrade, NBTTagCompound compound) {}
	
	public abstract String getName();
	
}