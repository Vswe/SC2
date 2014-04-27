package vswe.stevescarts.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.ModuleComputer;
import vswe.stevescarts.Computer.ComputerProg;
import vswe.stevescarts.Computer.ComputerTask;

import java.util.ArrayList;
public class ButtonVarFirstVar extends ButtonVarVar {
	

    public ButtonVarFirstVar(ModuleComputer module, LOCATION loc, boolean increase)
    {
		super(module, loc, increase);	
	}
	

	protected int getIndex(ComputerTask task) {
		return task.getVarFirstVarIndex();
	}
	
	protected void setIndex(ComputerTask task, int val) {
		task.setVarFirstVar(val);
	}
	
	protected String getName() {
		return "first";
	}
	
	protected boolean isVarVisible(ComputerTask task) {
		return task.getVarUseFirstVar();
	}

}