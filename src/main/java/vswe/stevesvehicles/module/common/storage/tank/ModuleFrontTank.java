package vswe.stevesvehicles.module.common.storage.tank;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleFrontTank extends ModuleTank{
	public ModuleFrontTank(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	
	@Override
	protected int getTankSize() {
		return 8000;
	}

	
}