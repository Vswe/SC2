package vswe.stevesvehicles.old.Buttons;
import vswe.stevesvehicles.old.Modules.Workers.ModuleComputer;
import vswe.stevesvehicles.old.Computer.ComputerTask;

public class ButtonFlowForEndVar extends ButtonFlowForVar {
	

    public ButtonFlowForEndVar(ModuleComputer module, LOCATION loc, boolean increase)
    {
		super(module, loc, increase);	
	}
	

	protected int getIndex(ComputerTask task) {
		return task.getFlowForEndVarIndex();
	}
	
	protected void setIndex(ComputerTask task, int val) {
		task.setFlowForEndVar(val);
	}
	
	protected String getName() {
		return "end";
	}
	
	protected boolean isVarVisible(ComputerTask task) {
		return task.getFlowForUseEndVar();
	}

}