package vswe.stevescarts.old.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.old.Modules.Workers.ModuleComputer;
import vswe.stevescarts.old.Computer.ComputerTask;

public class ButtonInfoVar extends ButtonAssembly {
		
	protected boolean increase;
	
    public ButtonInfoVar(ModuleComputer module, LOCATION loc, boolean increase)
    {
		super(module, loc);	
		this.increase = increase;
	}
	
	@Override
	public String toString() {
		if (increase) {
			return "Next variable";
		}else{
			return "Previous variable";
		}
	}
		
	@Override
	public boolean isVisible() {
		if (!super.isVisible()) {
			return false;
		}
	
		if (((ModuleComputer)module).getSelectedTasks() != null && ((ModuleComputer)module).getSelectedTasks().size() > 0) {
			for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
				if (!task.isInfo(task.getType()) || task.isInfoEmpty()) {
					return false;
				}
			}	
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public int texture() {
		return increase ? 30 : 31;
	}
	
	@Override
	public boolean isEnabled() {
		for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
			if (increase && task.getInfoVarIndex() < task.getProgram().getVars().size() - 1) {
				return true;
			}else if(!increase && task.getInfoVarIndex() > -1) {
				return true;
			}
		}	
		return false;
	}	
	
	@Override
	public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey) {
		for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
			task.setInfoVar(task.getInfoVarIndex() + (increase ? 1 : -1));
		}
	}		
	
}