package vswe.stevescarts.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.ModuleComputer;
import vswe.stevescarts.Computer.ComputerProg;
import vswe.stevescarts.Computer.ComputerTask;

import java.util.ArrayList;
public class ButtonVarUseSecondVar extends ButtonVarUseVar {
	

    public ButtonVarUseSecondVar(ModuleComputer module, LOCATION loc, boolean use)
    {
		super(module, loc, use);	
	}
	
	@Override
	protected boolean getUseVar(ComputerTask task) {
		return task.getVarUseSecondVar();
	}
	
	@Override
	protected void setUseVar(ComputerTask task, boolean val) {
		task.setVarUseSecondVar(val);
	}
	
	@Override
	protected String getName() {
		return "second";
	}
	
	@Override
	protected boolean isSecondValue() {
		return true;
	}
}