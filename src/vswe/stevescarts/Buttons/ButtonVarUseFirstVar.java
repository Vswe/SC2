package vswe.stevescarts.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.ModuleComputer;
import vswe.stevescarts.Computer.ComputerProg;
import vswe.stevescarts.Computer.ComputerTask;

import java.util.ArrayList;
public class ButtonVarUseFirstVar extends ButtonVarUseVar {
	

    public ButtonVarUseFirstVar(ModuleComputer module, LOCATION loc, boolean use)
    {
		super(module, loc, use);	
	}
	
	@Override
	protected boolean getUseVar(ComputerTask task) {
		return task.getVarUseFirstVar();
	}
	
	@Override
	protected void setUseVar(ComputerTask task, boolean val) {
		task.setVarUseFirstVar(val);
	}
	
	@Override
	protected String getName() {
		return "first";
	}
	

}