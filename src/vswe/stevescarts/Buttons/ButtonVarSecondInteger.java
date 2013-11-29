package vswe.stevescarts.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.ModuleComputer;
import vswe.stevescarts.Computer.ComputerProg;
import vswe.stevescarts.Computer.ComputerTask;

import java.util.ArrayList;
public class ButtonVarSecondInteger extends ButtonVarInteger {
	

    public ButtonVarSecondInteger(ModuleComputer module, LOCATION loc, int dif)
    {
		super(module, loc, dif);	
	}
	

	@Override
	protected String getName() {
		return "second";
	}
	
	@Override
	protected boolean isVarVisible(ComputerTask task) {
		return task.getVarUseSecondVar();
	}
	
	@Override
	protected int getInteger(ComputerTask task) {
		return task.getVarSecondInteger();
	}
	
	@Override
	protected void setInteger(ComputerTask task, int val) {
		task.setVarSecondInteger(val);
	}
	
	@Override
	protected boolean isSecondValue() {
		return true;
	}
}