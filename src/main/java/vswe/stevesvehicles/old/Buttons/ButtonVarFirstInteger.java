package vswe.stevesvehicles.old.Buttons;
import vswe.stevesvehicles.module.cart.attachment.ModuleComputer;
import vswe.stevesvehicles.old.Computer.ComputerTask;

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