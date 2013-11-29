package vswe.stevescarts.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.ModuleComputer;
import vswe.stevescarts.Computer.ComputerProg;
import vswe.stevescarts.Computer.ComputerTask;

import java.util.ArrayList;
public class ButtonProgramAdd extends ButtonAssembly {
	
    public ButtonProgramAdd(ModuleComputer module, LOCATION loc)
    {
		super(module, loc);	
	}
	
	@Override
	public String toString() {
		return "Add new program";
	}
		
	@Override
	public boolean isVisible() {
		return super.isVisible() && true;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	
	@Override
	public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey) {
		((ModuleComputer)module).setCurrentProg(new ComputerProg((ModuleComputer)module));
	}	
	

}