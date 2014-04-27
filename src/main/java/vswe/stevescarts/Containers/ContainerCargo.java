package vswe.stevescarts.Containers;
import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import vswe.stevescarts.Slots.SlotCargo;
import vswe.stevescarts.TileEntities.TileEntityCargo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
