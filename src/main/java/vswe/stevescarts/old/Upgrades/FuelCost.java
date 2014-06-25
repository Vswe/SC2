package vswe.stevescarts.old.Upgrades;


import vswe.stevescarts.old.Helpers.Localization;

public class FuelCost extends BaseEffect {


	private float cost;
	public FuelCost(float cost) {
		super();
		this.cost = cost;
	}
	
	@Override
	public String getName() {
		return Localization.UPGRADES.FUEL_COST.translate((getPercentage() >= 0 ? "+" : "") + getPercentage());
	}
	
	private int getPercentage() {
		return (int)(cost * 100);
	}
	
	public float getCost() {
		return cost;
	}


}