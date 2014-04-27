package vswe.stevescarts.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.ModuleComputer;
import vswe.stevescarts.Computer.ComputerProg;
import vswe.stevescarts.Computer.ComputerTask;

import java.util.ArrayList;
public class ButtonFlowType extends ButtonAssembly {
	
	private int typeId;
	
    public ButtonFlowType(ModuleComputer module, LOCATION loc, int id)
    {
		super(module, loc);	
		typeId = id;
	}
	
	@Override
	public String toString() {
		return "Change to " + ComputerTask.getFlowTypeName(typeId);
	}
		
	@Override
	public boolean isVisible() {
		if (!super.isVisible()) {
			return false;
		}
	
		if (module instanceof ModuleComputer && ((ModuleComputer)module).getSelectedTasks() != null && ((ModuleComputer)module).getSelectedTasks().size() > 0) {
			for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
				if (!ComputerTask.isFlow(task.getType())) {
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
        return ComputerTask.getFlowImage(typeId);
    }
	
	@Override
	public int ColorCode()
    {
        return 1;
    }	
	
	@Override
	public boolean isEnabled() {
		for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
			if (task.getFlowType() != typeId) {
				return true;
			}
		}	
		return false;
	}
	

	@Override
	public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey) {
		for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
			task.setFlowType(typeId);
		}
	}	
	

}