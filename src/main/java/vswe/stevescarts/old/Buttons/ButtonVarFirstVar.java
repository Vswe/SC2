package vswe.stevescarts.old.Buttons;
import vswe.stevescarts.old.Modules.Workers.ModuleComputer;
import vswe.stevescarts.old.Computer.ComputerTask;

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