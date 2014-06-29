package vswe.stevesvehicles.old.Buttons;
import vswe.stevesvehicles.module.cart.attachment.ModuleComputer;
import vswe.stevesvehicles.old.Computer.ComputerTask;

public abstract class ButtonFlowCondition extends ButtonAssembly {
	
	
    public ButtonFlowCondition(ModuleComputer module, LOCATION loc)
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
				if (!task.isFlowCondition()) {
					return false;
				}
			}	
			return true;
		}else{
			return false;
		}
	}
	
}