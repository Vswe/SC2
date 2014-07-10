package vswe.stevesvehicles.upgrade.effect.fuel;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevesvehicles.tileentity.TileEntityUpgrade;
import vswe.stevesvehicles.upgrade.effect.util.TankEffect;

public class ThermalFuel extends TankEffect {


	public ThermalFuel(TileEntityUpgrade upgrade) {
		super(upgrade);
	}

	@Override
	public int getTankSize() {
		return 12000;
	}

	public static final int LAVA_EFFICIENCY = 3;
	
	@Override
	public void update() {
		super.update();
		
		if (!upgrade.getWorldObj().isRemote && upgrade.getMaster() != null) {
			if (upgrade.getFluid() != null && upgrade.getFluid().getFluid().equals(FluidRegistry.LAVA)) {
				int fuelspace = upgrade.getMaster().getMaxFuelLevel() - upgrade.getMaster().getFuelLevel();				
				int unitspace = Math.min(fuelspace / LAVA_EFFICIENCY, 200);
				
				if (unitspace > 100) {	
					
					FluidStack drain = upgrade.drain(unitspace, false);
					if (drain != null && drain.amount > 0) {
						upgrade.getMaster().setFuelLevel(upgrade.getMaster().getFuelLevel() + drain.amount * LAVA_EFFICIENCY);
						upgrade.drain(unitspace, true);
					}
				}
			}
		}
	}
	

	
}
