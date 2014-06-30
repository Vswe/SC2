package vswe.stevesvehicles.container.slots;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotBase extends Slot {
	private int x;
	private int y;
    public SlotBase(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
		this.x = x;
		this.y = y;
    }
		
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public boolean containsValidItem() {
		return getStack() != null && isItemValid(getStack());
	}	

}
