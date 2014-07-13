package vswe.stevesvehicles.container;
import java.util.ArrayList;

import vswe.stevesvehicles.container.ContainerBase;
import vswe.stevesvehicles.tileentity.TileEntityBase;
import net.minecraft.inventory.IInventory;
import vswe.stevesvehicles.old.Helpers.DistributorSide;
import vswe.stevesvehicles.tileentity.TileEntityDistributor;

public class ContainerDistributor extends ContainerBase {

	public IInventory getMyInventory() {
		return null;
	}
	
	public TileEntityBase getTileEntity() {
		return distributor;
	}		
	

    private TileEntityDistributor distributor;
    public ContainerDistributor(TileEntityDistributor distributor) {
        this.distributor = distributor;

        cachedValues = new ArrayList<Short>();
        for (DistributorSide ignored : distributor.getSides()) {
            cachedValues.add((short) 0);
            cachedValues.add((short) 0);
        }
    }

   
	
	public ArrayList<Short> cachedValues;
	
}
