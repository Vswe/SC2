package vswe.stevescarts.old.Slots;
import net.minecraft.inventory.IInventory;

public class SlotIncinerator extends SlotBase implements ISpecialSlotValidator
{
	


    public SlotIncinerator(IInventory inv, int id, int x, int y)
    {
        super(inv, id, x, y);
    }
	
	@Override
	public boolean isSlotValid() {
		return false;
	}
	
	@Override
	public int getSlotStackLimit()
    {
		return 1;
	}
	
}
