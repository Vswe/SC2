package vswe.stevesvehicles.old.Buttons;
import vswe.stevesvehicles.module.cart.attachment.ModuleComputer;
import vswe.stevesvehicles.old.Computer.ComputerTask;

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