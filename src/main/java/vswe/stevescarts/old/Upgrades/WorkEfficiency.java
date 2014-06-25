package vswe.stevescarts.old.Upgrades;


import vswe.stevescarts.old.Helpers.Localization;

public class WorkEfficiency extends BaseEffect {


	private float efficiency;
	public WorkEfficiency(float efficiency) {
		super();
		this.efficiency = efficiency;
	}
	
	@Override
	public String getName() {
		return Localization.UPGRADES.EFFICIENCY.translate((getPercentage() >= 0 ? "+" : "") + getPercentage());
	}
	
	private int getPercentage() {
		return (int)(efficiency * 100);
	}
	
	public float getEfficiency() {
		return efficiency;
	}


}