package vswe.stevescarts.Modules.Storages.Tanks;
import net.minecraft.block.Block;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevescarts.Carts.MinecartModular;
public class ModuleOpenTank extends ModuleTank{
	public ModuleOpenTank(MinecartModular cart) {
		super(cart);
	}


	@Override
	protected int getTankSize() {
		return 7000;
	}


	int cooldown = 0;
	
	
	@Override
	public void update() {
		super.update();
		
		if (cooldown > 0) {
			cooldown--;
		}else{
			cooldown = 20;
			
			if (
				getCart().worldObj.isRaining() && 
				getCart().worldObj.canBlockSeeTheSky(getCart().x(), getCart().y() + 1, getCart().z()) && 
				getCart().worldObj.getPrecipitationHeight(getCart().x(), getCart().z()) < getCart().y() + 1
			) {
				fill(new FluidStack(FluidRegistry.WATER, getCart().worldObj.getBiomeGenForCoords(getCart().x(), getCart().z()).getEnableSnow() ? 2 : 5), true);
			}
		}
	}
	
}