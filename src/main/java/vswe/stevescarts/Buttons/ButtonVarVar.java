package vswe.stevescarts.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.ModuleComputer;
import vswe.stevescarts.Computer.ComputerProg;
import vswe.stevescarts.Computer.ComputerTask;

import java.util.ArrayList;
public class ButtonVarVar extends ButtonVar {
	
	protected boolean increase;
	
    public ButtonVarVar(ModuleComputer module, LOCATION loc, boolean increase)
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
		return task.getVarVarIndex();
	}
	
	protected void setIndex(ComputerTask task, int val) {
		task.setVarVar(val);
	}
	
	protected String getName() {
		return "main";
	}
	
	protected boolean isVarVisible(ComputerTask task) {
		return true;
	}

}