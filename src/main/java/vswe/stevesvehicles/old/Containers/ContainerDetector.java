package vswe.stevesvehicles.old.Containers;
import vswe.stevesvehicles.container.ContainerBase;
import vswe.stevesvehicles.detector.LogicObjectOperator;
import vswe.stevesvehicles.detector.OperatorObject;
import vswe.stevesvehicles.old.TileEntities.TileEntityBase;
import net.minecraft.inventory.IInventory;
import vswe.stevesvehicles.detector.LogicObject;
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

		
		mainObj = new LogicObjectOperator((byte)0, OperatorObject.MAIN);
    }


}
