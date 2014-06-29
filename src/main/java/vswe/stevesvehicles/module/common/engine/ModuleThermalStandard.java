package vswe.stevesvehicles.module.common.engine;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleThermalStandard extends ModuleThermalBase {
	public ModuleThermalStandard(VehicleBase vehicleBase) {
		super(vehicleBase);
	}
	
	@Override
	protected int getEfficiency() {
		return 25;
	}
	
	@Override
	protected int getCoolantEfficiency() {
		return 0;
	}
	
}