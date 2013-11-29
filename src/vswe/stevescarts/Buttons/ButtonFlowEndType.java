package vswe.stevescarts.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.ModuleComputer;
import vswe.stevescarts.Computer.ComputerProg;
import vswe.stevescarts.Computer.ComputerTask;

import java.util.ArrayList;
public class ButtonFlowEndType extends ButtonAssembly {
	
	private int typeId;
	
    public ButtonFlowEndType(ModuleComputer module, LOCATION loc, int typeId)
    {
		super(module, loc);	
		this.typeId = typeId;
	}
	
	
	@Override
	public String toString() {
		return "Change to End " + ComputerTask.getEndTypeName(typeId);
	}
	
	@Override
	public int texture() {
		return ComputerTask.getEndImage(typeId);
	}
	
	
	@Override
	public boolean isVisible() {
		if (!super.isVisible()) {
			return false;
		}
	
		if (((ModuleComputer)module).getSelectedTasks() != null && ((ModuleComputer)module).getSelectedTasks().size() > 0) {
			for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
				if (!task.isFlowEnd()) {
					return false;
				}
			}	
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public boolean isEnabled() {
		for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
			if (typeId != task.getFlowEndType()) {
				return true;
			}
		}	
		return false;
	}
	

	@Override
	public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey) {
		for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
			task.setFlowEndType(typeId);
		}
	}	
	

}