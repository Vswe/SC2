package vswe.stevescarts.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.ModuleComputer;
import vswe.stevescarts.Computer.ComputerProg;
import vswe.stevescarts.Computer.ComputerTask;

import java.util.ArrayList;
public class ButtonTaskType extends ButtonAssembly {
	
	private int typeId;
	
    public ButtonTaskType(ModuleComputer module, LOCATION loc, int id)
    {
		super(module, loc);	
		typeId = id;
	}
	
	@Override
	public String toString() {
		if (haveTasks()) {
			return "Change to " + ComputerTask.getTypeName(typeId);
		}else{
			return "Add " + ComputerTask.getTypeName(typeId) + " task";
		}
	}
		
	@Override
	public boolean isVisible() {
		return super.isVisible() && true;
	}
	
	@Override
	public int texture()
    {
		if (typeId < 4) {
			return typeId * 2 + (haveTasks() ? 1 : 0);
		}else if(typeId == 4) {
			return 66 + (haveTasks() ? 1 : 0);
		}else{
			return typeId * 2 + (haveTasks() ? 1 : 0) - 2;
		}
    }
	
	@Override
	public boolean isEnabled() {
		if (module instanceof ModuleComputer && ((ModuleComputer)module).getCurrentProg() != null) {
			if (haveTasks()) {
				for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
					if (task.getType() != typeId) {
						return true;
					}
				}
				return false;
			}else{
				return true;
			}
		}else{
			return false;
		}		
	}
	
	private boolean haveTasks() {
		return  ((ModuleComputer)module).getSelectedTasks().size() > 0;
	}

	
	@Override
	public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey) {
		if (haveTasks()) {
			for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
				task.setType(typeId);
			}
		}else{
			ComputerProg program = ((ModuleComputer)module).getCurrentProg();
			if (program != null) {
				ComputerTask task = new ComputerTask((ModuleComputer)module, program);
				task.setType(typeId);
				program.getTasks().add(task);
			}	
		}
		
		
	}	
	

}