package vswe.stevescarts.old.Upgrades;


import vswe.stevescarts.old.Helpers.Localization;

public class TimeFlatRemoved extends TimeFlat {


	public TimeFlatRemoved(int ticks) {
		super(ticks);
	}
	
	@Override
	public String getName() {
		return Localization.UPGRADES.FLAT_REMOVED.translate((getSeconds() >= 0 ? "+" : "") + getSeconds(), String.valueOf(getSeconds()));
	}
	

}