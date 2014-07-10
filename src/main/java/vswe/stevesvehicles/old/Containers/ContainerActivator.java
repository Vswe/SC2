package vswe.stevesvehicles.old.Containers;
import java.util.ArrayList;

import vswe.stevesvehicles.container.ContainerBase;
import vswe.stevesvehicles.tileentity.TileEntityBase;
import net.minecraft.inventory.IInventory;
import vswe.stevesvehicles.old.Helpers.ActivatorOption;
import vswe.stevesvehicles.tileentity.TileEntityActivator;

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
