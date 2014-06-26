package vswe.stevesvehicles.old.Listeners;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import vswe.stevesvehicles.old.StevesVehicles;
import vswe.stevesvehicles.vehicles.entities.EntityModularCart;
import vswe.stevesvehicles.vehicles.entities.IVehicleEntity;

public class TicketListener implements LoadingCallback {
	public TicketListener() {
		ForgeChunkManager.setForcedChunkLoadingCallback(StevesVehicles.instance, this);
	}


	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world) {
		for (Ticket ticket : tickets) {
			Entity entity = ticket.getEntity();
			if (entity instanceof IVehicleEntity) {
                IVehicleEntity vehicleEntity = (IVehicleEntity)entity;
                vehicleEntity.getVehicle().loadChunks(ticket);
			}		
		}
	}
   
}