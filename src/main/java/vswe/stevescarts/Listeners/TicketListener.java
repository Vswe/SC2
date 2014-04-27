package vswe.stevescarts.Listeners;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Carts.MinecartModular;
 
public class TicketListener implements LoadingCallback {
	public TicketListener() {
		ForgeChunkManager.setForcedChunkLoadingCallback(StevesCarts.instance, this);
	}


	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world) {
		for (Ticket ticket : tickets) {
			Entity entity = ticket.getEntity();
			if (entity instanceof MinecartModular) {
				MinecartModular cart = (MinecartModular)entity;
				cart.loadChunks(ticket);			
			}		
		}
	}
   
}