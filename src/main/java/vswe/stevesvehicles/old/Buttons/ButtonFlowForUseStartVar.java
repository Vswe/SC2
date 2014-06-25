package vswe.stevesvehicles.old.Buttons;
import vswe.stevesvehicles.old.Modules.Workers.ModuleComputer;
import vswe.stevesvehicles.old.Computer.ComputerTask;

public class ButtonFlowForUseStartVar extends ButtonFlowForUseVar {
	

    public ButtonFlowForUseStartVar(ModuleComputer module, LOCATION loc, boolean use)
    {
		super(module, loc, use);	
	}
	

	protected boolean getUseVar(ComputerTask task) {
		return task.getFlowForUseStartVar();
	}
	
	protected void setUseVar(ComputerTask task, boolean val) {
		task.setFlowForUseStartVar(val);
	}
	
	protected String getName() {
		return "start";
	}
	

}