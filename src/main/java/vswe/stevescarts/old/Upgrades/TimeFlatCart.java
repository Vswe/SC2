package vswe.stevescarts.old.Upgrades;


import vswe.stevescarts.old.Helpers.Localization;

public class TimeFlatCart extends BaseEffect {


	private int ticks;
	public TimeFlatCart(int ticks) {
		super();
		this.ticks = ticks;
	}
	
	@Override
	public String getName() {
        return Localization.UPGRADES.CART_FLAT.translate((getSeconds() >= 0 ? "+" : "") + getSeconds(), String.valueOf(getSeconds()));
	}
	
	protected int getSeconds() {
		return ticks / 20;
	}
	
	public int getTicks() {
		return ticks;
	}


}