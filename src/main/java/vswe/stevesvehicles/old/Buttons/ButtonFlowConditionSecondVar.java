package vswe.stevesvehicles.old.Buttons;
import vswe.stevesvehicles.old.Modules.Workers.ModuleComputer;
import vswe.stevesvehicles.old.Computer.ComputerTask;

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