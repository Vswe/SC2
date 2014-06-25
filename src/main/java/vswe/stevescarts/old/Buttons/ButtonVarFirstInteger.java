package vswe.stevescarts.old.Buttons;
import vswe.stevescarts.old.Modules.Workers.ModuleComputer;
import vswe.stevescarts.old.Computer.ComputerTask;

public class ButtonVarFirstInteger extends ButtonVarInteger {
	

    public ButtonVarFirstInteger(ModuleComputer module, LOCATION loc, int dif)
    {
		super(module, loc, dif);	
	}
	

	@Override
	protected String getName() {
		return "first";
	}
	
	@Override
	protected boolean isVarVisible(ComputerTask task) {
		return task.getVarUseFirstVar();
	}
	
	@Override
	protected int getInteger(ComputerTask task) {
		return task.getVarFirstInteger();
	}
	
	@Override
	protected void setInteger(ComputerTask task, int val) {
		task.setVarFirstInteger(val);
	}
	

}