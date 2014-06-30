package vswe.stevesvehicles.module.cart.tool;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.old.Helpers.EnchantmentInfo;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.common.addon.ModuleEnchants;
import vswe.stevesvehicles.module.cart.ModuleWorker;

public abstract class ModuleTool extends ModuleWorker {
	public ModuleTool(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

    protected ModuleEnchants enchanter;
	@Override
	public void init() {
		super.init();
		for (ModuleBase module : getVehicle().getModules()) {
			if (module instanceof ModuleEnchants) {
				enchanter = (ModuleEnchants)module;
				enchanter.addType(EnchantmentInfo.Enchantment_Type.TOOL);
				break;
			}			
		}		
	}	


	public boolean shouldSilkTouch(Block b, int x, int y, int z, int m) {
	    boolean doSilkTouch = false;

	    //try-catch here just because I need to give it a null player, and other mods might assume that they actually get a player, I don't know.
	    try {
	    	if (enchanter != null && enchanter.useSilkTouch() && b.canSilkHarvest(getVehicle().getWorld(), null, x, y, z, m)) {
	    		return true;
	    	}
	    }catch (Exception ignored) {}
	    return false;
	}

	public ItemStack getSilkTouchedItem(Block b, int m) {
	    int droppedMeta = 0;

        ItemStack stack = new ItemStack(b, 1, droppedMeta);
	    if (stack.getItem() != null && stack.getItem().getHasSubtypes()){
            return new ItemStack(b, 1, m);
	    }else{
            return stack;
        }
	}

}
