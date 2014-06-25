package vswe.stevescarts.old.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.old.Modules.Workers.ModuleComputer;
import vswe.stevescarts.old.Computer.ComputerTask;

public class ButtonControlVar extends ButtonControl {
	
	protected boolean increase;
	
    public ButtonControlVar(ModuleComputer module, LOCATION loc, boolean increase)
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
		if (((ModuleComputer)module).getSelectedTasks() != null) {
			for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
				if (!task.getControlUseVar()) {
					return false;
				}
			}
		}
		
		return super.isVisible();
	}	
	
	@Override
	public int texture() {
		return increase ? 30 : 31;
	}
	
	@Override
	public boolean isEnabled() {
		for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
			if (increase && task.getControlVarIndex() < task.getProgram().getVars().size() - 1) {
				return true;
			}else if(!increase && task.getControlVarIndex() > -1) {
				return true;
			}
		}	
		return false;
	}
	

	@Override
	public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey) {
		for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
			task.setControlVar(task.getControlVarIndex() + (increase ? 1 : -1));
		}
	}	
	

}