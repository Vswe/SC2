package vswe.stevescarts.Upgrades;


public class TimeFlatCart extends BaseEffect {


	private int ticks;
	public TimeFlatCart(int ticks) {
		super();
		this.ticks = ticks;
	}
	
	@Override
	public String getName() {
		return "Cart assembling time " + (getSeconds() >= 0 ? "+" : "") + getSeconds() + " seconds";
	}
	
	protected int getSeconds() {
		return ticks / 20;
	}
	
	public int getTicks() {
		return ticks;
	}


}