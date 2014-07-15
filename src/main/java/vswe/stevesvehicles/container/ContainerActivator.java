package vswe.stevesvehicles.container;
import java.util.ArrayList;

import vswe.stevesvehicles.tileentity.TileEntityBase;
import net.minecraft.inventory.IInventory;
import vswe.stevesvehicles.tileentity.toggler.TogglerOption;
import vswe.stevesvehicles.tileentity.TileEntityActivator;

public class ContainerActivator extends ContainerBase {

	
	public IInventory getMyInventory() {
		return null;
	}
	
	public TileEntityBase getTileEntity() {
		return activator;
	}

    private TileEntityActivator activator;
    public ContainerActivator(TileEntityActivator activator) {
        this.activator = activator;

		lastOptions = new ArrayList<Integer>();
		for (TogglerOption option : activator.getOptions()) {
			lastOptions.add(option.getOption());
		}
    }

	public ArrayList<Integer> lastOptions;
	
}
