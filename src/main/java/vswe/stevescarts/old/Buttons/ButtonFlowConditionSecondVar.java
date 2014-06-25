package vswe.stevescarts.old.Buttons;
import vswe.stevescarts.old.Modules.Workers.ModuleComputer;
import vswe.stevescarts.old.Computer.ComputerTask;

public class ButtonFlowConditionSecondVar extends ButtonFlowConditionVar {
	

    public ButtonFlowConditionSecondVar(ModuleComputer module, LOCATION loc, boolean increase)
    {
		super(module, loc, increase);	
	}
	

	@Override
	public boolean isVisible() {
		if (((ModuleComputer)module).getSelectedTasks() != null) {
			for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
				if (!task.getFlowConditionUseSecondVar()) {
					return false;
				}
			}
		}
		
		return super.isVisible();
	}
	
	@Override
	protected int getIndex(ComputerTask task) {
		return task.getFlowConditionSecondVarIndex();
	}
	
	@Override
	protected void setIndex(ComputerTask task, int val) {
		task.setFlowConditionSecondVar(val);
	}
	
}