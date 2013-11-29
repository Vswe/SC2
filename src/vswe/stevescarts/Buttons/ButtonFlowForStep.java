package vswe.stevescarts.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.ModuleComputer;
import vswe.stevescarts.Computer.ComputerProg;
import vswe.stevescarts.Computer.ComputerTask;

import java.util.ArrayList;
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