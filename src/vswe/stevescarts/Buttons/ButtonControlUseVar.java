package vswe.stevescarts.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.ModuleComputer;
import vswe.stevescarts.Computer.ComputerProg;
import vswe.stevescarts.Computer.ComputerTask;

import java.util.ArrayList;
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