package vswe.stevescarts.Containers;
import java.util.ArrayList;
import java.util.Iterator;
import vswe.stevescarts.TileEntities.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import vswe.stevescarts.Slots.SlotCargo;
import vswe.stevescarts.TileEntities.TileEntityManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ContainerManager extends ContainerBase
{
	public IInventory getMyInventory() {
		return manager;
	}
	
	public TileEntityBase getTileEntity() {
		return manager;
	}		
	
	
    private TileEntityManager manager;
    public ContainerManager(TileEntityManager manager)
    {
        this.manager = manager;		
    }

	protected void addPlayer(IInventory invPlayer) {
        for (int k = 0; k < 3; k++)
        {
            for (int j1 = 0; j1 < 9; j1++)
            {
                addSlotToContainer(new Slot(invPlayer, j1 + k * 9 + 9, j1 * 18 + offsetX(), 104 + k * 18 + 36));
            }
        }

        for (int l = 0; l < 9; l++)
        {
            addSlotToContainer(new Slot(invPlayer, l, l * 18 + offsetX(), 162 + 36));
        }	
    }
	
	public short lastHeader;
	public short lastColor;
	public short lastAmount;
	
	protected abstract int offsetX();
}
