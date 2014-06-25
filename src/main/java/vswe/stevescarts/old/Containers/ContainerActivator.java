package vswe.stevescarts.old.Containers;
import java.util.ArrayList;

import vswe.stevescarts.containers.ContainerBase;
import vswe.stevescarts.old.TileEntities.TileEntityBase;
import net.minecraft.inventory.IInventory;
import vswe.stevescarts.old.Helpers.ActivatorOption;
import vswe.stevescarts.old.TileEntities.TileEntityActivator;

public class ContainerActivator extends ContainerBase
{

	
	public IInventory getMyInventory() {
		return null;
	}
	
	public TileEntityBase getTileEntity() {
		return activator;
	}

    private TileEntityActivator activator;
    public ContainerActivator(IInventory invPlayer, TileEntityActivator activator)
    {
        this.activator = activator;

		lastOptions = new ArrayList<Integer>();
		for (ActivatorOption option : activator.getOptions()) {
			lastOptions.add(option.getOption());
		}
    }


	public ArrayList<Integer> lastOptions;
	
}
