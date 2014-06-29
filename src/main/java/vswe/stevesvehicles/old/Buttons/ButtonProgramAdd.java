package vswe.stevesvehicles.old.Buttons;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevesvehicles.module.cart.attachment.ModuleComputer;
import vswe.stevesvehicles.old.Computer.ComputerProg;

public class ButtonProgramAdd extends ButtonAssembly {
	
    public ButtonProgramAdd(ModuleComputer module, LOCATION loc)
    {
		super(module, loc);	
	}
	
	@Override
	public String toString() {
		return "Add new program";
	}
		
	@Override
	public boolean isVisible() {
		return super.isVisible() && true;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	
	@Override
	public void onServerClick(EntityPlayer player, int mousebutton, boolean ctrlKey, boolean shiftKey) {
		((ModuleComputer)module).setCurrentProg(new ComputerProg((ModuleComputer)module));
	}	
	

}