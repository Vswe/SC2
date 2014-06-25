package vswe.stevescarts.old.Buttons;
import vswe.stevescarts.old.Modules.Workers.ModuleComputer;
import vswe.stevescarts.old.Computer.ComputerTask;

public class ButtonFlowForUseEndVar extends ButtonFlowForUseVar {
	

    public ButtonFlowForUseEndVar(ModuleComputer module, LOCATION loc, boolean use)
    {
		super(module, loc, use);	
	}
	

	protected boolean getUseVar(ComputerTask task) {
		return task.getFlowForUseEndVar();
	}
	
	protected void setUseVar(ComputerTask task, boolean val) {
		task.setFlowForUseEndVar(val);
	}
	
	protected String getName() {
		return "end";
	}
	

}