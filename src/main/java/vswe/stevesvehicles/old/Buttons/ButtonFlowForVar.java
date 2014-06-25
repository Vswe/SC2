package vswe.stevesvehicles.old.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevesvehicles.old.Modules.Workers.ModuleComputer;
import vswe.stevesvehicles.old.Computer.ComputerTask;

public class ButtonFlowForVar extends ButtonFlowFor {
	
	protected boolean increase;
	
    public ButtonFlowForVar(ModuleComputer module, LOCATION loc, boolean increase)
    {
		super(module, loc);	
		this.increase = increase;
	}
	
	
	@Override
	public String toString() {
		if (increase) {
			return "Next " + getName() + " variable";
		}else{
			return "Previous " + getName() + " variable";
		}
	}
	
	@Override
	public boolean isVisible() {
		if (((ModuleComputer)module).getSelectedTasks() != null) {
			for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
				if (!isVarVisible(task)) {
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
			if (increase && getIndex(task) < task.getProgram().getVars().size() - 1) {
				return true;
			}else if(!increase && getIndex(task) > -1) {
				return true;
			}
		}	
		return false;
	}
	

	@Override
	public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey) {
		for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
			setIndex(task, getIndex(task) + (increase ? 1 : -1));
		}
	}	
	
	protected int getIndex(ComputerTask task) {
		return task.getFlowForVarIndex();
	}
	
	protected void setIndex(ComputerTask task, int val) {
		task.setFlowForVar(val);
	}
	
	protected String getName() {
		return "loop";
	}
	
	protected boolean isVarVisible(ComputerTask task) {
		return true;
	}

}