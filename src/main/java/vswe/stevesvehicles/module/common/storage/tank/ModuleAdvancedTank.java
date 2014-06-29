package vswe.stevesvehicles.module.common.storage.tank;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleAdvancedTank extends ModuleTank{
	public ModuleAdvancedTank(VehicleBase vehicleBase) {
		super(vehicleBase);
	}
	
	@Override
	protected int getTankSize() {
		return 32000;
	}
	
	
}