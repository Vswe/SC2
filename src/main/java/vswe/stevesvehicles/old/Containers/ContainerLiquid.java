package vswe.stevesvehicles.old.Containers;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevesvehicles.old.Slots.SlotLiquidFilter;
import vswe.stevesvehicles.old.Slots.SlotLiquidManagerInput;
import vswe.stevesvehicles.old.Slots.SlotLiquidOutput;
import vswe.stevesvehicles.old.TileEntities.TileEntityLiquid;

public class ContainerLiquid extends ContainerManager
{

    public ContainerLiquid(IInventory invPlayer, TileEntityLiquid liquid)
    {
        super(liquid);
		oldLiquids = new FluidStack[4];
		
		for (int i = 0; i < 4; i++) {
			int x = i % 2;
			int y = i / 2;
			addSlotToContainer(new SlotLiquidManagerInput(liquid, i, i * 3,x == 0 ? 6 : 208, y == 0 ? 17 : 80));
			addSlotToContainer(new SlotLiquidOutput(liquid, i * 3 + 1,x == 0 ? 6 : 208, y == 0 ? 42 : 105));			
			addSlotToContainer(new SlotLiquidFilter(liquid, i*3 + 2,x == 0 ? 66 : 148, y == 0 ? 12 : 110));
		}


		addPlayer(invPlayer);
    }

    
 	@Override
	protected int offsetX() {
		return 35;
	}   
	
	public FluidStack[] oldLiquids;
}
