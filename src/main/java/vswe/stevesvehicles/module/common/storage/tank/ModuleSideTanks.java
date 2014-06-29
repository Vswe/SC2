package vswe.stevesvehicles.module.common.storage.tank;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleSideTanks extends ModuleTank{
	public ModuleSideTanks(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	@Override
	protected int getTankSize() {
		return 8000;
	}

	
}