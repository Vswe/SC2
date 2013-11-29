package vswe.stevescarts.Helpers;

import vswe.stevescarts.ModuleData.ModuleData;

public class ModuleCountPair {
	
	private ModuleData data;
	private int count;
	private String name;
	
	public ModuleCountPair(ModuleData data) {
		this.data = data;
		count = 1;
		name = data.getName();
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
	
	public String toString() {
		String ret = name;
		if (count != 1) {
			ret += " x" + count;
		}
		return ret;
	}
}
