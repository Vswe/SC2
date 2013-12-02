package vswe.stevescarts.Modules.Storages.Tanks;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Models.Cart.ModelCartbase;
import vswe.stevescarts.Models.Cart.ModelTopTank;
import java.util.HashMap;

import net.minecraftforge.fluids.FluidTankInfo;
public class ModuleTopTank extends ModuleTank{
	public ModuleTopTank(MinecartModular cart) {
		super(cart);
	}
	
	@Override
	protected int getTankSize() {
		return 14000;
	}


}