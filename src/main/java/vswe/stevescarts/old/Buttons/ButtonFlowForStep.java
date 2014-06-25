package vswe.stevescarts.old.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.old.Modules.Workers.ModuleComputer;
import vswe.stevescarts.old.Computer.ComputerTask;

public class ButtonFlowForStep extends ButtonFlowFor {
	
	private boolean decrease;
	
    public ButtonFlowForStep(ModuleComputer module, LOCATION loc, boolean decrease)
    {
		super(module, loc);	
		this.decrease = decrease;
	}
	
	
	@Override
	public String toString() {
		return decrease ? "Set step to -1" : "Set step to +1";
	}
	
	@Override
	public int texture() {
		return decrease ? 45 : 44;
	}
	
	@Override
	public boolean isEnabled() {
		for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
			if (decrease != task.getFlowForDecrease()) {
				return true;
			}
		}	
		return false;
	}
	

	@Override
	public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey) {
		for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
			task.setFlowForDecrease(decrease);
		}
	}	
	

}