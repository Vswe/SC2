package vswe.stevescarts.old.Modules.Workers;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraftforge.fluids.FluidRegistry;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.old.Modules.Workers.Tools.ModuleFarmer;
public class ModuleHydrater extends ModuleWorker {
	public ModuleHydrater(EntityModularCart cart) {
		super(cart);
	}

	//lower numbers are prioritized
	public byte getWorkPriority() {
		return 82;
	}


	
	private int range = 1;
	
	public void init() {
		super.init();
	
		for (ModuleBase module : getCart().getModules()) {
			if (module instanceof ModuleFarmer) {
				range = ((ModuleFarmer)module).getExternalRange();
				break;
			}
		}
	}		

	//return true when the work is done, false allow other modules to continue the work
	public boolean work() {
       //get the next block so the cart knows where to mine
        Vec3 next = getNextblock();
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

   private boolean hydrate(int x, int y, int z)
   {
       Block b = getCart().worldObj.getBlock(x, y, z);
       int m = getCart().worldObj.getBlockMetadata(x, y, z);

        if (b == Blocks.farmland && m != 7)
        {
            int waterCost = 7 - m;
			waterCost = getCart().drain(FluidRegistry.WATER, waterCost, false);
			
            if (waterCost > 0)
            {
                if (doPreWork())
                {
                    startWorking(2 + waterCost);
                    return true;
                }
                else
                {
                    stopWorking();

                    getCart().drain(FluidRegistry.WATER, waterCost, true);
                    getCart().worldObj.setBlockMetadataWithNotify(x, y, z, m + waterCost, 3);
                }
            }
        }

        return false;
   }

}