package vswe.stevesvehicles.container;
import vswe.stevesvehicles.container.ContainerBase;
import vswe.stevesvehicles.tileentity.TileEntityBase;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.IInventory;
import vswe.stevesvehicles.tileentity.TileEntityUpgrade;
import vswe.stevesvehicles.upgrade.effect.util.InventoryEffect;
public class ContainerUpgrade extends ContainerBase {

	public IInventory getMyInventory() {
		return upgrade;
	}
	
	public TileEntityBase getTileEntity() {
		return upgrade;
	}	

    private TileEntityUpgrade upgrade;
    public ContainerUpgrade(IInventory invPlayer, TileEntityUpgrade upgrade) {
        this.upgrade = upgrade;
		
		if (upgrade.getEffects() == null || upgrade.getInventoryEffect() == null) {
			return;
		}

		InventoryEffect inventory = upgrade.getInventoryEffect();
		inventory.clear();
	
        for (int id = 0; id < inventory.getInventorySize(); id++) {
			Slot slot = inventory.createSlot(id);
			addSlotToContainer(slot);
			inventory.addSlot(slot);
        }
		
		
		for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, offsetX() + j * 18, i * 18 + offsetY()));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(invPlayer, i, offsetX() + i * 18, 58 + offsetY()));
        }		
    }


    protected int offsetX()
    {
        return 48;
    }

    protected int offsetY()
    {
        return 108;
    }	
	
	//temporary solution, make a proper one later
    public Object olddata;
}
