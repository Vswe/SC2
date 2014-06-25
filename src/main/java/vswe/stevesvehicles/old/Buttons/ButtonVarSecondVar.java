package vswe.stevesvehicles.old.Buttons;
import vswe.stevesvehicles.old.Modules.Workers.ModuleComputer;
import vswe.stevesvehicles.old.Computer.ComputerTask;

public class ButtonVarSecondVar extends ButtonVarVar {
	

    public ButtonVarSecondVar(ModuleComputer module, LOCATION loc, boolean increase)
    {
		super(module, loc, increase);	
	}
	

	protected int getIndex(ComputerTask task) {
		return task.getVarSecondVarIndex();
	}
	
	protected void setIndex(ComputerTask task, int val) {
		task.setVarSecondVar(val);
	}
	
	protected String getName() {
		return "second";
	}
	
	protected boolean isVarVisible(ComputerTask task) {
		return task.getVarUseSecondVar();
	}

	@Override
	protected boolean isSecondValue() {
		return true;
	}	
}