package vswe.stevescarts.Helpers;

import net.minecraft.util.StatCollector;
import vswe.stevescarts.ModuleData.ModuleData;

public class ModuleCountPair {
	
	private ModuleData data;
	private int count;
	private String name;
    private byte extraData;
	
	public ModuleCountPair(ModuleData data) {
		this.data = data;
		count = 1;
		name = data.getUnlocalizedName();
	}
	
	public int getCount() {
		return count;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void increase() {
		count++;
	}
	
	public boolean isContainingData(ModuleData data) {
		return this.data.equals(data);
	}
	
	public ModuleData getData() {
		return data;
	}

    public void setExtraData(byte b) {
        extraData = b;
    }
	
	public String toString() {
		String ret = data.getCartInfoText(StatCollector.translateToLocal(name), extraData);
		if (count != 1) {
			ret += " x" + count;
		}
		return ret;
	}
}
