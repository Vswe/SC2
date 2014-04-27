package vswe.stevescarts.Upgrades;


import vswe.stevescarts.Helpers.Localization;

public class TimeFlat extends BaseEffect {


	private int ticks;
	public TimeFlat(int ticks) {
		super();
		this.ticks = ticks;
	}
	
	@Override
	public String getName() {
		return Localization.UPGRADES.FLAT.translate((getSeconds() >= 0 ? "+" : "") + getSeconds(), String.valueOf(getSeconds()));
	}
	
	protected int getSeconds() {
		return ticks / 20;
	}
	
	public int getTicks() {
		return ticks;
	}


}