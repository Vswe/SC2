package vswe.stevescarts.Modules.Storages.Tanks;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Models.Cart.ModelCartbase;
import vswe.stevescarts.Models.Cart.ModelAdvancedTank;
import java.util.HashMap;
public class ModuleAdvancedTank extends ModuleTank{
	public ModuleAdvancedTank(MinecartModular cart) {
		super(cart);
	}

	@Override
	protected String getTankName() {
		return "Advanced Tank";
	}
	
	@Override
	protected int getTankSize() {
		return 32000;
	}
	
	
}