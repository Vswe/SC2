package vswe.stevescarts.old.Buttons;
import vswe.stevescarts.old.Modules.Workers.ModuleComputer;
import vswe.stevescarts.old.Computer.ComputerTask;

public class ButtonFlowForEndInteger extends ButtonFlowForInteger {
	

    public ButtonFlowForEndInteger(ModuleComputer module, LOCATION loc, int dif)
    {
		super(module, loc, dif);	
	}
	

	@Override
	protected String getName() {
		return "end";
	}
	
	@Override
	protected boolean isVarVisible(ComputerTask task) {
		return task.getFlowForUseEndVar();
	}
	
	@Override
	protected int getInteger(ComputerTask task) {
		return task.getFlowForEndInteger();
	}
	
	@Override
	protected void setInteger(ComputerTask task, int val) {
		task.setFlowForEndInteger(val);
	}
	

}