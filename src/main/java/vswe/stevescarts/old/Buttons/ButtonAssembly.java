package vswe.stevescarts.old.Buttons;
import vswe.stevescarts.old.Modules.Workers.ModuleComputer;

public abstract class ButtonAssembly extends ButtonBase {
	

    public ButtonAssembly(ModuleComputer module, LOCATION loc)
    {
		super(module, loc);	
	}
	
		
	@Override
	public boolean isVisible() {
		return !((ModuleComputer)module).isWriting();
	}
	


}