package vswe.stevescarts.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.ModuleComputer;
import vswe.stevescarts.Computer.ComputerProg;
import vswe.stevescarts.Computer.ComputerTask;

import java.util.ArrayList;
public class ButtonFlowForStartVar extends ButtonFlowForVar {
	

    public ButtonFlowForStartVar(ModuleComputer module, LOCATION loc, boolean increase)
    {
		super(module, loc, increase);	
	}
	

	protected int getIndex(ComputerTask task) {
		return task.getFlowForStartVarIndex();
	}
	
	protected void setIndex(ComputerTask task, int val) {
		task.setFlowForStartVar(val);
	}
	
	protected String getName() {
		return "start";
	}
	
	protected boolean isVarVisible(ComputerTask task) {
		return task.getFlowForUseStartVar();
	}

}