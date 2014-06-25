package vswe.stevesvehicles.old.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevesvehicles.old.Modules.Workers.ModuleComputer;
import vswe.stevesvehicles.old.Computer.ComputerTask;

public class ButtonLabelId extends ButtonAssembly {
	
	private boolean increase;
	
    public ButtonLabelId(ModuleComputer module, LOCATION loc, boolean increase)
    {
		super(module, loc);	
		this.increase = increase;
	}
	
	@Override
	public String toString() {
		return increase ? "Increase ID" : "Decrease ID";
	}
		
	@Override
	public boolean isVisible() {
		if (!super.isVisible()) {
			return false;
		}
	
		if (module instanceof ModuleComputer && ((ModuleComputer)module).getSelectedTasks() != null && ((ModuleComputer)module).getSelectedTasks().size() > 0) {
			for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
				if (!task.isFlowLabel() && !task.isFlowGoto()) {
					return false;
				}
			}	
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public int texture()
    {
        return increase ? 23 : 24;
    }

	
	@Override
	public boolean isEnabled() {
		for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
			if ((increase && task.getFlowLabelId() < 31) || (!increase && task.getFlowLabelId() > 0)) {
				return true;
			}			
		}	
		return false;
	}
	

	@Override
	public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey) {
		for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
			task.setFlowLabelId(task.getFlowLabelId() + (increase ? 1 : -1));
		}
	}	
	

}