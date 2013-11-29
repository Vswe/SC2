package vswe.stevescarts.Upgrades;


public class TimeFlat extends BaseEffect {


	private int ticks;
	public TimeFlat(int ticks) {
		super();
		this.ticks = ticks;
	}
	
	@Override
	public String getName() {
		return "Module assembling time " + (getSeconds() >= 0 ? "+" : "") + getSeconds() + " seconds";
	}
	
	protected int getSeconds() {
		return ticks / 20;
	}
	
	public int getTicks() {
		return ticks;
	}


}