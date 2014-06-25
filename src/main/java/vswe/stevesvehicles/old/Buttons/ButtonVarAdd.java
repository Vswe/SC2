package vswe.stevesvehicles.old.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevesvehicles.old.Modules.Workers.ModuleComputer;
import vswe.stevesvehicles.old.Computer.ComputerVar;

public class ButtonVarAdd extends ButtonAssembly {
	
    public ButtonVarAdd(ModuleComputer module, LOCATION loc)
    {
		super(module, loc);	
	}
	
	@Override
	public String toString() {
		return "Add Variable";
	}
		
	@Override
	public boolean isVisible() {
		return super.isVisible() && true;
	}
	
	@Override
	public int texture() {
		return 25;
	}
	
	@Override
	public boolean isEnabled() {
		return ((ModuleComputer)module).getCurrentProg() != null;
	}
	
	
	@Override
	public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey) {
		if (((ModuleComputer)module).getCurrentProg() != null) {
			ComputerVar var = new ComputerVar((ModuleComputer)module);
			var.setEditing(true);
			((ModuleComputer)module).getCurrentProg().getVars().add(var);
		}
	}	
	

}