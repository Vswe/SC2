package vswe.stevesvehicles.module.cart.attachment;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraftforge.fluids.FluidRegistry;
import vswe.stevesvehicles.module.cart.ModuleWorker;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.cart.tool.ModuleFarmer;
public class ModuleHydrater extends ModuleWorker {
	public ModuleHydrater(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	//lower numbers are prioritized
	public byte getWorkPriority() {
		return 82;
	}


	
	private int range = 1;
	
	public void init() {
		super.init();
	
		for (ModuleBase module : getVehicle().getModules()) {
			if (module instanceof ModuleFarmer) {
				range = ((ModuleFarmer)module).getExternalRange();
				break;
			}
		}
	}		

	//return true when the work is done, false allow other modules to continue the work
	public boolean work() {
       //get the next block so the cart knows where to mine
        Vec3 next = getNextBlock();
        //save thee coordinates for easy access
        int x = (int) next.xCoord;
        int y = (int) next.yCoord;
        int z = (int) next.zCoord;

        //loop through the blocks in the "hole" in front of the cart

        for (int i = -range; i <= range; i++)
        {
            for (int j = -range; j <= range; j++)
            {
                //calculate the coordinates of this "hole"
                int coordX = x + i;
                int coordY = y - 1;
                int coordZ = z + j;

				if (hydrate(coordX, coordY, coordZ))
                {
                    return true;
                }
            }
        }

		return false;
    }

   private boolean hydrate(int x, int y, int z) {
       Block b = getVehicle().getWorld().getBlock(x, y, z);
       int m = getVehicle().getWorld().getBlockMetadata(x, y, z);

        if (b == Blocks.farmland && m != 7) {
            int waterCost = 7 - m;
			waterCost = getVehicle().drain(FluidRegistry.WATER, waterCost, false);
			
            if (waterCost > 0) {
                if (doPreWork()) {
                    startWorking(2 + waterCost);
                    return true;
                } else {
                    stopWorking();

                    if (!getVehicle().hasCreativeSupplies()) {
                        getVehicle().drain(FluidRegistry.WATER, waterCost, true);
                    }
                    getVehicle().getWorld().setBlockMetadataWithNotify(x, y, z, m + waterCost, 3);
                }
            }
        }

        return false;
   }

}