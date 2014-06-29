package vswe.stevesvehicles.old.Buttons;
import vswe.stevesvehicles.module.cart.attachment.ModuleComputer;

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