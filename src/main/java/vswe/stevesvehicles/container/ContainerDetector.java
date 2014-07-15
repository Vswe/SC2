package vswe.stevesvehicles.container;
import vswe.stevesvehicles.tileentity.detector.LogicObjectOperator;
import vswe.stevesvehicles.tileentity.detector.OperatorObject;
import vswe.stevesvehicles.tileentity.TileEntityBase;
import net.minecraft.inventory.IInventory;
import vswe.stevesvehicles.tileentity.detector.LogicObject;
import vswe.stevesvehicles.tileentity.TileEntityDetector;

public class ContainerDetector extends ContainerBase {

	public IInventory getMyInventory() {
		return null;
	}
	
	public TileEntityBase getTileEntity() {
		return detector;
	}	
	
    private TileEntityDetector detector;
	public LogicObject mainObj;
    public ContainerDetector(TileEntityDetector detector) {
        this.detector = detector;

		
		mainObj = new LogicObjectOperator((byte)0, OperatorObject.MAIN);
    }


}
