package vswe.stevescarts.Upgrades;


public class FuelCapacity extends BaseEffect {


	private int capacity;
	public FuelCapacity(int capacity) {
		super();
		this.capacity = capacity;
	}
	
	@Override
	public String getName() {
		return "Fuel capacity " + (capacity >= 0 ? "+" : "") + capacity;
	}
	
	public int getFuelCapacity() {
		return capacity;
	}


}