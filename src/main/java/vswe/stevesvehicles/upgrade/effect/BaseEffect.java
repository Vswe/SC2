package vswe.stevesvehicles.upgrade.effect;

import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.tileentity.TileEntityUpgrade;


public abstract class BaseEffect {


    protected final TileEntityUpgrade upgrade;

	public BaseEffect(TileEntityUpgrade upgrade) {
	    this.upgrade = upgrade;
	}
	
	public void update() {}
	public void init() {}
	public void removed() {}
	
	public void load(NBTTagCompound compound) {}
	public void save(NBTTagCompound compound) {}

    public final TileEntityUpgrade getUpgrade() {
        return upgrade;
    }


}