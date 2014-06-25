package vswe.stevesvehicles.old.Buttons;
import vswe.stevesvehicles.old.Modules.Workers.ModuleComputer;
import vswe.stevesvehicles.old.Computer.ComputerTask;

public abstract class ButtonFlowFor extends ButtonAssembly {
	
	
    public ButtonFlowFor(ModuleComputer module, LOCATION loc)
    {
		super(module, loc);	
	}
	

		
	@Override
	public boolean isVisible() {
		if (!super.isVisible()) {
			return false;
		}
	
		if (((ModuleComputer)module).getSelectedTasks() != null && ((ModuleComputer)module).getSelectedTasks().size() > 0) {
			for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
				if (!task.isFlowFor()) {
					return false;
				}
			}	
			return true;
		}else{
			return false;
		}
	}
	
}