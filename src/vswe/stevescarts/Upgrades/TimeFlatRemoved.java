package vswe.stevescarts.Upgrades;


public class TimeFlatRemoved extends TimeFlat {


	public TimeFlatRemoved(int ticks) {
		super(ticks);
	}
	
	@Override
	public String getName() {
		return "Module disassembling time " + (getSeconds() >= 0 ? "+" : "") + getSeconds() + " seconds";
	}
	

}