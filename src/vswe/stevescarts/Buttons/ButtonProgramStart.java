package vswe.stevescarts.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.ModuleComputer;
import vswe.stevescarts.Computer.ComputerProg;
import vswe.stevescarts.Computer.ComputerTask;

import java.util.ArrayList;
public class ButtonProgramStart extends ButtonAssembly {
	
    public ButtonProgramStart(ModuleComputer module, LOCATION loc)
    {
		super(module, loc);	
	}
	
	@Override
	public String toString() {
		return "Start Program";
	}
		
	@Override
	public boolean isVisible() {
		return super.isVisible() && true;
	}
	
	@Override
	public boolean isEnabled() {
		return ((ModuleComputer)module).getCurrentProg() != null;
	}
	
	
	@Override
	public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey) {

		ComputerProg program = ((ModuleComputer)module).getCurrentProg();
		if (program != null) {
			program.start();
		}
		
	}	
	

}