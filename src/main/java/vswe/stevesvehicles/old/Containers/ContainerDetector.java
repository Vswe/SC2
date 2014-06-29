package vswe.stevesvehicles.old.Containers;
import vswe.stevesvehicles.container.ContainerBase;
import vswe.stevesvehicles.old.TileEntities.TileEntityBase;
import net.minecraft.inventory.IInventory;
import vswe.stevesvehicles.old.Helpers.LogicObject;
import vswe.stevesvehicles.old.TileEntities.TileEntityDetector;

public class ContainerDetector extends ContainerBase
{


	public IInventory getMyInventory() {
		return null;
	}
	
	public TileEntityBase getTileEntity() {
		return detector;
	}	
	
    private TileEntityDetector detector;
	public LogicObject mainObj;
    public ContainerDetector(IInventory invPlayer, TileEntityDetector detector)
    {
        this.detector = detector;

		
		mainObj = new LogicObject((byte)1, (byte)0);
    }


}
