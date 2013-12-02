package vswe.stevescarts.Upgrades;


import vswe.stevescarts.Helpers.Localization;

public class FuelCapacity extends BaseEffect {


	private int capacity;
	public FuelCapacity(int capacity) {
		super();
		this.capacity = capacity;
	}
	
	@Override
	public String getName() {
		return Localization.UPGRADES.FUEL_CAPACITY.translate((capacity >= 0 ? "+" : "") + capacity);
	}
	
	public int getFuelCapacity() {
		return capacity;
	}


}