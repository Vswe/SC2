package vswe.stevesvehicles.module.common.storage.tank;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleTopTank extends ModuleTank{
	public ModuleTopTank(VehicleBase vehicleBase) {
		super(vehicleBase);
	}
	
	@Override
	protected int getTankSize() {
		return 14000;
	}


}