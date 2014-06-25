package vswe.stevescarts.old.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.old.Modules.Workers.ModuleComputer;
import vswe.stevescarts.old.Computer.ComputerTask;

public class ButtonFlowConditionVar extends ButtonFlowCondition {
	
	protected boolean increase;
	
    public ButtonFlowConditionVar(ModuleComputer module, LOCATION loc, boolean increase)
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
		return task.getFlowConditionVarIndex();
	}
	
	protected void setIndex(ComputerTask task, int val) {
		task.setFlowConditionVar(val);
	}
	

}