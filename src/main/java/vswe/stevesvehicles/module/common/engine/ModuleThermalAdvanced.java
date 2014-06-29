package vswe.stevesvehicles.module.common.engine;
import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleThermalAdvanced extends ModuleThermalBase {
	public ModuleThermalAdvanced(VehicleBase vehicleBase) {
		super(vehicleBase);
	}
	
	@Override
	protected int getEfficiency() {
		return 100;
	}
	
	@Override
	protected int getCoolantEfficiency() {
		return 150;
	}
	
}