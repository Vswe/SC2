package vswe.stevescarts.Modules.Storages.Tanks;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Models.Cart.ModelCartbase;
import vswe.stevescarts.Models.Cart.ModelSideTanks;
import java.util.HashMap;
public class ModuleSideTanks extends ModuleTank{
	public ModuleSideTanks(MinecartModular cart) {
		super(cart);
	}

	@Override
	protected String getTankName() {
		return "Side Tanks";
	}
	
	@Override
	protected int getTankSize() {
		return 8000;
	}

	
}