package vswe.stevescarts.Modules.Addons;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Slots.SlotIncinerator;
public class ModuleIncinerator extends ModuleAddon {
	public ModuleIncinerator(MinecartModular cart) {
		super(cart);
	}


	public void incinerate(ItemStack item) {
		if (isItemValid(item)) {
			if (getIncinerationCost() != 0) {
				int amount = item.stackSize * getIncinerationCost();
				amount = getCart().drain(FluidRegistry.LAVA, amount, false);
				int incinerated = amount / getIncinerationCost();
				getCart().drain(FluidRegistry.LAVA, incinerated * getIncinerationCost(), true);
				item.stackSize -= incinerated;
			}else{
				item.stackSize = 0;
			}
		}
	}
	
	protected int getIncinerationCost() {
		return 3;
	}
	
	protected boolean isItemValid(ItemStack item) {
		if (item != null) {
			for(int i = 0; i < getInventorySize(); i++) {
				if (getStack(i) != null && item.isItemEqual(getStack(i))) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public boolean hasGui() {
		return true;
	}	
	
	@Override
	public void drawForeground(GuiMinecart gui) {
	    drawString(gui,"Incinerator", 8, 6, 0x404040);
	}
	
	@Override
	protected int getInventoryWidth() {
		return 4;
	}	
	
	@Override
	protected SlotBase getSlot(int slotId, int x, int y) {
		return new SlotIncinerator(getCart(),slotId,8+x*18,23+y*18);
	}	

}