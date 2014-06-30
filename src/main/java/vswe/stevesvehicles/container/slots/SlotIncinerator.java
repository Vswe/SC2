package vswe.stevesvehicles.container.slots;
import net.minecraft.inventory.IInventory;

public class SlotIncinerator extends SlotBase implements ISpecialSlotValidator {

    public SlotIncinerator(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }
	
	@Override
	public boolean isSlotValid() {
		return false;
	}
	
	@Override
	public int getSlotStackLimit() {
		return 1;
	}
	
}
