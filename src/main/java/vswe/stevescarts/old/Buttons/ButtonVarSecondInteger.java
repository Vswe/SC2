package vswe.stevescarts.old.Buttons;
import vswe.stevescarts.old.Modules.Workers.ModuleComputer;
import vswe.stevescarts.old.Computer.ComputerTask;

public class ButtonVarSecondInteger extends ButtonVarInteger {
	

    public ButtonVarSecondInteger(ModuleComputer module, LOCATION loc, int dif)
    {
		super(module, loc, dif);	
	}
	

	@Override
	protected String getName() {
		return "second";
	}
	
	@Override
	protected boolean isVarVisible(ComputerTask task) {
		return task.getVarUseSecondVar();
	}
	
	@Override
	protected int getInteger(ComputerTask task) {
		return task.getVarSecondInteger();
	}
	
	@Override
	protected void setInteger(ComputerTask task, int val) {
		task.setVarSecondInteger(val);
	}
	
	@Override
	protected boolean isSecondValue() {
		return true;
	}
}