package vswe.stevescarts.Upgrades;


public class FuelCost extends BaseEffect {


	private float cost;
	public FuelCost(float cost) {
		super();
		this.cost = cost;
	}
	
	@Override
	public String getName() {
		return "Fuel cost " + (getPercentage() >= 0 ? "+" : "") + getPercentage() + "%";
	}
	
	private int getPercentage() {
		return (int)(cost * 100);
	}
	
	public float getCost() {
		return cost;
	}


}