package vswe.stevescarts.Listeners;
import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Items.ModItems;
import vswe.stevescarts.StevesCarts;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class PlayerSleepListener implements ITickHandler
{
    public PlayerSleepListener()
    {
		TickRegistry.registerTickHandler(this,Side.SERVER);
    }

	
	
	
	
    /**
     * Called at the "start" phase of a tick
     *
     * Multiple ticks may fire simultaneously- you will only be called once with all the firing ticks
     *
     * @param type
     * @param tickData
     */
    public void tickStart(EnumSet<TickType> type, Object... tickData) {

	}


	
    /**
     * Called at the "end" phase of a tick
     *
     * Multiple ticks may fire simultaneously- you will only be called once with all the firing ticks
     *
     * @param type
     * @param tickData
     */
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (type.contains(TickType.PLAYER)) {
			EntityPlayer player =  (EntityPlayer)tickData[0];
			
			if (StevesCarts.isChristmas && player.isPlayerFullyAsleep()) {
				for (int i = 0; i < player.inventory.getSizeInventory();i++) {
					ItemStack item = player.inventory.getStackInSlot(i);
					if (item != null && item.getItem() == ModItems.component && item.getItemDamage() == 56) {
						item.setItemDamage(item.getItemDamage() + 1);
					}
				}
				
				
			}
		}
	}

    /**
     * Returns the list of ticks this tick handler is interested in receiving at the minute
     *
     * @return
     */
    public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER);
	}

    /**
     * A profiling label for this tick handler
     * @return
     */
    public String getLabel() {
		return "StevesCarts.sleeping";
	}		
	
	
	
}