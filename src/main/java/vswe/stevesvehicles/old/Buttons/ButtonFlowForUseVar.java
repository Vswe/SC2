package vswe.stevesvehicles.old.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevesvehicles.module.cart.attachment.ModuleComputer;
import vswe.stevesvehicles.old.Computer.ComputerTask;

public abstract class ButtonFlowForUseVar extends ButtonFlowFor {
	
	private boolean use;
	
    public ButtonFlowForUseVar(ModuleComputer module, LOCATION loc, boolean use)
    {
		super(module, loc);	
		this.use = use;
	}
	
	
	@Override
	public String toString() {
		return use ? "Use " + getName() + " variable" : "Use " + getName() + " integer";
	}
	
	@Override
	public int texture() {
		return use ? 38 : 39;
	}
	
	@Override
	public boolean isEnabled() {
		for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
			if (use != getUseVar(task)) {
				return true;
			}
		}	
		return false;
	}
	

	@Override
	public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey) {
		for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
			setUseVar(task, use);
		}
	}	
	
	protected abstract boolean getUseVar(ComputerTask task);
	protected abstract void setUseVar(ComputerTask task, boolean val);
	protected abstract String getName();
	

}