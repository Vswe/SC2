package vswe.stevesvehicles.upgrade.effect.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import vswe.stevesvehicles.tileentity.TileEntityUpgrade;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;

public abstract class InventoryEffect extends InterfaceEffect {

	protected ArrayList<Slot> slots;

	public InventoryEffect(TileEntityUpgrade upgrade) {
		super(upgrade);
		
		slots = new ArrayList<Slot>();
	}
	

	
	public Class<? extends Slot> getSlot(int id) {
		return Slot.class;
	}
	
	public void onInventoryChanged() {}

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
        return slotId >= 0 && slotId < slots.size() && slots.get(slotId).isItemValid(item);
	}



	public Slot createSlot(int id) {
		try {
			Class<? extends Slot> slotClass = getSlot(id);
			
			Constructor slotConstructor = slotClass.getConstructor(IInventory.class, int.class, int.class, int.class);

			Object slotObject = slotConstructor.newInstance(upgrade, id, getSlotX(id), getSlotY(id));

			return (Slot)slotObject;
		}catch(Exception e) {
			System.out.println("Failed to create slot! More info below.");
		
			e.printStackTrace();
			return null;
		}				         
	}

    public ItemStack getStack(int id) {
        return upgrade.getStackInSlot(id);
    }

    public void setStack(int id, ItemStack item) {
        upgrade.setInventorySlotContents(id, item);
    }
}