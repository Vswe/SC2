package vswe.stevesvehicles.module.common.storage.tank;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleOpenTank extends ModuleTank{
	public ModuleOpenTank(VehicleBase vehicleBase) {
		super(vehicleBase);
	}


	@Override
	protected int getTankSize() {
		return 7000;
	}

	int cooldown = 0;
    private static final int RAIN_UPDATE_COOLDOWN = 20;
    private static final int RAIN_INCREASE = 20;
    private static final int SNOW_INCREASE = 5;

	@Override
	public void update() {
		super.update();
		
		if (cooldown > 0) {
			cooldown--;
		}else{
			cooldown = RAIN_UPDATE_COOLDOWN;
			
			if (
				getVehicle().getWorld().isRaining() &&
				getVehicle().getWorld().canBlockSeeTheSky(getVehicle().x(), getVehicle().y() + 1, getVehicle().z()) &&
				getVehicle().getWorld().getPrecipitationHeight(getVehicle().x(), getVehicle().z()) < getVehicle().y() + 1
			) {
				fill(new FluidStack(FluidRegistry.WATER, getVehicle().getWorld().getBiomeGenForCoords(getVehicle().x(), getVehicle().z()).getEnableSnow() ? SNOW_INCREASE : RAIN_INCREASE), true);
			}
		}
	}
	
}