package vswe.stevesvehicles.old.Containers;
import vswe.stevesvehicles.container.ContainerBase;
import vswe.stevesvehicles.old.TileEntities.TileEntityBase;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.IInventory;
import vswe.stevesvehicles.old.TileEntities.TileEntityUpgrade;
import vswe.stevesvehicles.old.Upgrades.InventoryEffect;
public class ContainerUpgrade extends ContainerBase
{

	public IInventory getMyInventory() {
		return upgrade;
	}
	
	public TileEntityBase getTileEntity() {
		return upgrade;
	}	

    private TileEntityUpgrade upgrade;
    public ContainerUpgrade(IInventory invPlayer, TileEntityUpgrade upgrade)
    {
        this.upgrade = upgrade;
		
		if (upgrade.getUpgrade() == null || upgrade.getUpgrade().getInventoryEffect() == null) {
			return;
		}

		InventoryEffect inventory = upgrade.getUpgrade().getInventoryEffect();
		inventory.clear();
	
        for (int id = 0; id < inventory.getInventorySize(); id++)
         {	
			Slot slot = inventory.createSlot(upgrade, id);
			addSlotToContainer(slot);
			inventory.addSlot(slot);
        }
		
		
		for (int i = 0; i < 3; i++)
        {
            for (int k = 0; k < 9; k++)
            {
                addSlotToContainer(new Slot(invPlayer, k + i * 9 + 9, offsetX() + k * 18, i * 18 + offsetY()));
            }
        }

        for (int j = 0; j < 9; j++)
        {
            addSlotToContainer(new Slot(invPlayer, j, offsetX() + j * 18, 58 + offsetY()));
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
