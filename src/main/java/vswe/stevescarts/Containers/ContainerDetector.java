package vswe.stevescarts.Containers;
import java.util.Iterator;
import vswe.stevescarts.TileEntities.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import vswe.stevescarts.Helpers.LogicObject;
import vswe.stevescarts.TileEntities.TileEntityDetector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
