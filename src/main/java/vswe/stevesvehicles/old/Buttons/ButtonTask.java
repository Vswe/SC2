package vswe.stevesvehicles.old.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevesvehicles.old.Modules.Workers.ModuleComputer;
import vswe.stevesvehicles.old.Computer.ComputerProg;
import vswe.stevesvehicles.old.Computer.ComputerTask;

import java.util.ArrayList;
public class ButtonTask extends ButtonAssembly {
	
	private int id;
	
    public ButtonTask(ModuleComputer module, LOCATION loc, int id)
    {
		super(module, loc);	
		this.id = id;
	}
	
	@Override
	public String toString() {
		ComputerTask task = getTask();
		if (task == null) {
			return "Something went wrong";
		}else{
			return task.toString();
		}
	}
		
	@Override
	public boolean isVisible() {
		return super.isVisible() && getTask() != null;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public int borderID() {
		ComputerTask task = getTask();
		if (task != null) {
			boolean selected = task.getIsActivated();
			boolean running = false;
			if (module instanceof ModuleComputer) {
				ComputerProg program = ((ModuleComputer)module).getActiveProgram();
				if (program != null) {
					running = program.getActiveId() == id;
				}
			}
			
		
			if (running && selected) {
				return 2;
			}else if(running) {
				return 1;
			}else if(selected) {
				return 0;
			}
		}
		return super.borderID();
	}	
	
	@Override
    public int ColorCode()
    {
		ComputerTask task = getTask();
		if (task != null) {
			return task.getType();
		}else{
			return 0;
		}
    }	
	
	@Override
	public int texture() {
		ComputerTask task = getTask();
		if (task != null) {
			return task.getImage();
		}else{
			return super.texture();
		}
	}
	
	@Override
	public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey) {
		ComputerTask task = getTask();
		
		if (!ctrlKey) {
			if (module instanceof ModuleComputer) {
				ComputerProg program = ((ModuleComputer)module).getCurrentProg();
				if (program != null) {
					for (ComputerTask t : program.getTasks()) {
						if (t != task) {
							t.setIsActivated(false);
						}
					}
				}
			}
		}	
		
		

		task.setIsActivated(!task.getIsActivated());
				
	}	
	
	private ComputerTask getTask() {
		ComputerProg program = ((ModuleComputer)module).getCurrentProg();
		if (program != null) {
			ArrayList<ComputerTask>	tasks = program.getTasks();
			if (id >= 0 && id < tasks.size()) {
				return tasks.get(id);
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
}