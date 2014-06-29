package vswe.stevesvehicles.module.cart.tool;


import vswe.stevesvehicles.vehicle.VehicleBase;

public class ModuleWoodcutterDiamond extends ModuleWoodcutter {

	public ModuleWoodcutterDiamond(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	@Override
	public int getPercentageDropChance() {
		return 80;
	}	

}
