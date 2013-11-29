package vswe.stevescarts.Containers;
import java.util.ArrayList;
import java.util.Iterator;

import vswe.stevescarts.TileEntities.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import vswe.stevescarts.Helpers.ActivatorOption;
import vswe.stevescarts.TileEntities.TileEntityActivator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
