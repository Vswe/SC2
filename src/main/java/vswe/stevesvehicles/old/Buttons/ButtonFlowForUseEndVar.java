package vswe.stevesvehicles.old.Buttons;
import vswe.stevesvehicles.module.cart.attachment.ModuleComputer;
import vswe.stevesvehicles.old.Computer.ComputerTask;

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