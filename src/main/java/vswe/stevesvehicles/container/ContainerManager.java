package vswe.stevesvehicles.container;
import vswe.stevesvehicles.container.ContainerBase;
import vswe.stevesvehicles.tileentity.TileEntityBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import vswe.stevesvehicles.tileentity.TileEntityManager;

public abstract class ContainerManager extends ContainerBase {
	public IInventory getMyInventory() {
		return manager;
	}
	
	public TileEntityBase getTileEntity() {
		return manager;
	}		
	
	
    private TileEntityManager manager;
    public ContainerManager(TileEntityManager manager) {
        this.manager = manager;		
    }

	protected void addPlayer(IInventory invPlayer) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, j * 18 + offsetX(), 104 + i * 18 + 36));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(invPlayer, i, i * 18 + offsetX(), 162 + 36));
        }	
    }
	
	public short lastHeader;
	public short lastColor;
	public short lastAmount;
	
	protected abstract int offsetX();
}
