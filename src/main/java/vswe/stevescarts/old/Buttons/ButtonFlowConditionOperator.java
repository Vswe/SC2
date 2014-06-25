package vswe.stevescarts.old.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.old.Modules.Workers.ModuleComputer;
import vswe.stevescarts.old.Computer.ComputerTask;

public class ButtonFlowConditionOperator extends ButtonFlowCondition {
	
	private int typeId;
	
    public ButtonFlowConditionOperator(ModuleComputer module, LOCATION loc, int typeId)
    {
		super(module, loc);	
		this.typeId = typeId;
	}
	
	
	@Override
	public String toString() {
		return "Change to " + ComputerTask.getFlowOperatorName(typeId, true);
	}
	
	@Override
	public int texture() {
		return 32 + typeId;
	}
	
	@Override
	public boolean isEnabled() {
		for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
			if (typeId != task.getFlowConditionOperator()) {
				return true;
			}
		}	
		return false;
	}
	

	@Override
	public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey) {
		for (ComputerTask task : ((ModuleComputer)module).getSelectedTasks()) {
			task.setFlowConditionOperator(typeId);
		}
	}	
	

}