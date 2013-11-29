package vswe.stevescarts.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.ModuleComputer;
import vswe.stevescarts.Computer.ComputerProg;
import vswe.stevescarts.Computer.ComputerTask;

import java.util.ArrayList;
public abstract class ButtonFlowForInteger extends ButtonFlowFor {
	
	private int dif;
	
    public ButtonFlowForInteger(ModuleComputer module, LOCATION loc, int dif)
    {
		super(module, loc);	
		this.dif = dif;
	}
	
	
	@Override
	public String toString() {
		if (dif < 0) {
			return "Decrease " + getName() + " by " + (-1 * dif);
		}else{
			return "Increase " + getName() + " by " + dif;
		}
	}
	
	@Override
	public int texture() {
		if (dif == 1) {
			return 40;
		}else if(dif == -1) {
			return 41;
		}else if(dif == 10) {
			return 42;
		}else if(dif == -10) {
			return 43;
		}
	
		return super.texture();
	}
	
	
	@Override
	public boolean isVisible() {
		if (((ModuleComputer)module).getSelectedTasks() != null) {
			for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
				if (isVarVisible(task)) {
					return false;
				}
			}
		}
		
		return super.isVisible();
	}
	
	@Override
	public boolean isEnabled() {
		for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
			if (-128 <= getInteger(task) + dif && getInteger(task) + dif <= 127) {
				return true;
			}
		}	
		return false;
	}
	

	@Override
	public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey) {
		for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
			setInteger(task, getInteger(task) + dif);
		}
	}	
	
	protected abstract String getName();
	protected abstract boolean isVarVisible(ComputerTask task);
	protected abstract int getInteger(ComputerTask task);
	protected abstract void setInteger(ComputerTask task, int val);
	

}