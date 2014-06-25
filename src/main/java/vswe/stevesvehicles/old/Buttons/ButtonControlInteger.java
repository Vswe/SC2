package vswe.stevesvehicles.old.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevesvehicles.old.Modules.Workers.ModuleComputer;
import vswe.stevesvehicles.old.Computer.ComputerTask;

public class ButtonControlInteger extends ButtonControl {
	
	private int dif;
	
    public ButtonControlInteger(ModuleComputer module, LOCATION loc, int dif)
    {
		super(module, loc);	
		this.dif = dif;
	}
	
	
	@Override
	public String toString() {
		if (dif < 0) {
			return "Decrease by " + (-1 * dif);
		}else{
			return "Increase by " + dif;
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
				if (task.getControlUseVar() || !task.getControlUseBigInteger(Math.abs(dif))) {
					return false;
				}
			}
		}
		
		return super.isVisible();
	}
	
	@Override
	public boolean isEnabled() {
		for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
			if (task.getControlMinInteger() <= task.getControlInteger() + dif && task.getControlInteger() + dif <= task.getControlMaxInteger()) {
				return true;
			}
		}	
		return false;
	}
	

	@Override
	public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey) {
		for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
			task.setControlInteger(task.getControlInteger() + dif);
		}
	}	

	

}