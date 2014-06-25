package vswe.stevescarts.old.Containers;
import java.util.ArrayList;

import net.minecraft.inventory.IInventory;
import vswe.stevescarts.old.Slots.SlotCargo;
import vswe.stevescarts.old.TileEntities.TileEntityCargo;

public class ContainerCargo extends ContainerManager
{



    public ContainerCargo(IInventory invPlayer, TileEntityCargo cargo)
    {
		super(cargo);
		
		cargo.cargoSlots = new ArrayList<SlotCargo>();
		cargo.lastLayout = -1;
		for (int i = 0; i < 60; i++) {
			SlotCargo slot = new SlotCargo(cargo, i);
			addSlotToContainer(slot);
			cargo.cargoSlots.add(slot);
		}
		
		addPlayer(invPlayer);
    }

    
	public short lastTarget;

	@Override
	protected int offsetX() {
		return 73;
	}
}
