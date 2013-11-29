package vswe.stevescarts.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.ModuleComputer;
import vswe.stevescarts.Computer.ComputerProg;
import vswe.stevescarts.Computer.ComputerTask;

import java.util.ArrayList;
public class ButtonVarSecondVar extends ButtonVarVar {
	

    public ButtonVarSecondVar(ModuleComputer module, LOCATION loc, boolean increase)
    {
		super(module, loc, increase);	
	}
	

	protected int getIndex(ComputerTask task) {
		return task.getVarSecondVarIndex();
	}
	
	protected void setIndex(ComputerTask task, int val) {
		task.setVarSecondVar(val);
	}
	
	protected String getName() {
		return "second";
	}
	
	protected boolean isVarVisible(ComputerTask task) {
		return task.getVarUseSecondVar();
	}

	@Override
	protected boolean isSecondValue() {
		return true;
	}	
}