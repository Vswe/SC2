package vswe.stevesvehicles.old.Listeners;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import vswe.stevesvehicles.vehicle.entity.IVehicleEntity;

public class ChunkListener
{
    public ChunkListener()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

	@SubscribeEvent
	public void invoke(EntityEvent.EnteringChunk event) {	
		if (!event.entity.isDead && event.entity instanceof IVehicleEntity) {
			((IVehicleEntity)event.entity).getVehicle().loadChunks(event.newChunkX,event.newChunkZ);
		}

	}
}