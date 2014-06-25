package vswe.stevesvehicles.old.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevesvehicles.old.Modules.Workers.ModuleComputer;
import vswe.stevesvehicles.old.Computer.ComputerTask;

public class ButtonControlUseVar extends ButtonControl {
	
	private boolean use;
	
    public ButtonControlUseVar(ModuleComputer module, LOCATION loc, boolean use)
    {
		super(module, loc);	
		this.use = use;
	}
	
	
	@Override
	public String toString() {
		return use ? "Use variable" : "Use integer";
	}
	
	@Override
	public int texture() {
		return use ? 38 : 39;
	}
	
	@Override
	public boolean isEnabled() {
		for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
			if (use != task.getControlUseVar()) {
				return true;
			}
		}	
		return false;
	}
	

	@Override
	public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey) {
		for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
			task.setControlUseVar(use);
		}
	}	
	

	

}