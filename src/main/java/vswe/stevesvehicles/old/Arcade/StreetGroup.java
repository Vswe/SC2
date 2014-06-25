package vswe.stevesvehicles.old.Arcade;

public class StreetGroup extends PropertyGroup {

	private float [] color;
	private int houseCost;
	public StreetGroup(int houseCost, int[] color) {
		this.houseCost = houseCost;
		this.color = new float[] {(float)color[0] / 256, (float)color[1] / 256, (float)color[2] / 256};
	}

	public float[] getColor() {
		return color;
	}
	
	public int getStructureCost() {
		return houseCost;
	}
	
}
