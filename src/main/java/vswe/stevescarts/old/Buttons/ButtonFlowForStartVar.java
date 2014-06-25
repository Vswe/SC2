package vswe.stevescarts.old.Buttons;
import vswe.stevescarts.old.Modules.Workers.ModuleComputer;
import vswe.stevescarts.old.Computer.ComputerTask;

public class ButtonFlowForStartVar extends ButtonFlowForVar {
	

    public ButtonFlowForStartVar(ModuleComputer module, LOCATION loc, boolean increase)
    {
		super(module, loc, increase);	
	}
	

	protected int getIndex(ComputerTask task) {
		return task.getFlowForStartVarIndex();
	}
	
	protected void setIndex(ComputerTask task, int val) {
		task.setFlowForStartVar(val);
	}
	
	protected String getName() {
		return "start";
	}
	
	protected boolean isVarVisible(ComputerTask task) {
		return task.getFlowForUseStartVar();
	}

}