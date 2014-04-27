package vswe.stevescarts.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.ModuleComputer;
import vswe.stevescarts.Computer.ComputerProg;
import vswe.stevescarts.Computer.ComputerTask;

import java.util.ArrayList;
public class ButtonFlowForUseStartVar extends ButtonFlowForUseVar {
	

    public ButtonFlowForUseStartVar(ModuleComputer module, LOCATION loc, boolean use)
    {
		super(module, loc, use);	
	}
	

	protected boolean getUseVar(ComputerTask task) {
		return task.getFlowForUseStartVar();
	}
	
	protected void setUseVar(ComputerTask task, boolean val) {
		task.setFlowForUseStartVar(val);
	}
	
	protected String getName() {
		return "start";
	}
	

}