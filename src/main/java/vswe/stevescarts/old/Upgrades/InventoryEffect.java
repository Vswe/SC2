package vswe.stevescarts.old.Upgrades;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import vswe.stevescarts.old.TileEntities.TileEntityUpgrade;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;

public abstract class InventoryEffect extends InterfaceEffect {

	protected ArrayList<Slot> slots;

	public InventoryEffect() {
		super();
		
		slots = new ArrayList<Slot>();
	}
	

	
	public Class<? extends Slot> getSlot(int id) {
		return Slot.class;
	}
	
	public void onInventoryChanged(TileEntityUpgrade upgrade) {}

	public abstract int getInventorySize();
	public abstract int getSlotX(int id);
	public abstract int getSlotY(int id);

	public void addSlot(Slot slot) {
		slots.add(slot);
	}
	
	public void clear() {
		slots.clear();
	}
	
	public boolean isItemValid(int slotId, ItemStack item) {
		if (slotId >= 0 && slotId < slots.size()) {
			return slots.get(slotId).isItemValid(item);
		}else{	
			return false;
		}
	}



	public Slot createSlot(TileEntityUpgrade upgrade, int id) {
		try {
			Class<? extends Slot> slotClass = getSlot(id);
			
			Constructor slotConstructor = slotClass.getConstructor(new Class[] {IInventory.class, int.class, int.class, int.class});

			Object slotObject = slotConstructor.newInstance(new Object[] {upgrade, id, getSlotX(id), getSlotY(id)});

			return (Slot)slotObject;
		}catch(Exception e) {
			System.out.println("Failed to create slot! More info below.");
		
			e.printStackTrace();
			return null;
		}				         
	}
}