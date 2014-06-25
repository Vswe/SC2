package vswe.stevescarts.old.Buttons;
import vswe.stevescarts.old.Modules.Workers.ModuleComputer;
import vswe.stevescarts.old.Computer.ComputerTask;

public class ButtonVarUseSecondVar extends ButtonVarUseVar {
	

    public ButtonVarUseSecondVar(ModuleComputer module, LOCATION loc, boolean use)
    {
		super(module, loc, use);	
	}
	
	@Override
	protected boolean getUseVar(ComputerTask task) {
		return task.getVarUseSecondVar();
	}
	
	@Override
	protected void setUseVar(ComputerTask task, boolean val) {
		task.setVarUseSecondVar(val);
	}
	
	@Override
	protected String getName() {
		return "second";
	}
	
	@Override
	protected boolean isSecondValue() {
		return true;
	}
}