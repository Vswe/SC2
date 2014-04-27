package vswe.stevescarts.Containers;
import java.util.ArrayList;
import java.util.Iterator;
import vswe.stevescarts.TileEntities.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import vswe.stevescarts.Helpers.DistributorSide;
import vswe.stevescarts.TileEntities.TileEntityDistributor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerDistributor extends ContainerBase
{

	public IInventory getMyInventory() {
		return null;
	}
	
	public TileEntityBase getTileEntity() {
		return distributor;
	}		
	

    private TileEntityDistributor distributor;
    public ContainerDistributor(IInventory invPlayer, TileEntityDistributor distributor)
    {
        this.distributor = distributor;

		cachedValues = new ArrayList<Short>();
		for (DistributorSide side : distributor.getSides()) {
			cachedValues.add((short)0);
			cachedValues.add((short)0);
		}		
    }

   
	
	public ArrayList<Short> cachedValues;
	
}
