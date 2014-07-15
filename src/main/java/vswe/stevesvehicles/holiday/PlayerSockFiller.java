package vswe.stevesvehicles.holiday;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.item.ComponentTypes;
import vswe.stevesvehicles.StevesVehicles;
import cpw.mods.fml.relauncher.Side;

public class PlayerSockFiller {

    public PlayerSockFiller() {
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void tickEnd(TickEvent.PlayerTickEvent event) {
        if (event.side == Side.SERVER) {
			EntityPlayer player = event.player;
			
			if (StevesVehicles.holidays.contains(HolidayType.CHRISTMAS) && player.isPlayerFullyAsleep()) {
				for (int i = 0; i < player.inventory.getSizeInventory();i++) {
					ItemStack item = player.inventory.getStackInSlot(i);
					if (ComponentTypes.SOCK.isStackOfType(item)) {
						item.setItemDamage(ComponentTypes.STUFFED_SOCK.getId());
					}
				}
				
				
			}
		}
	}

	
}