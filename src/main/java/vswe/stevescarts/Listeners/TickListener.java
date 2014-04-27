package vswe.stevescarts.Listeners;
import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Carts.MinecartModular;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TickListener implements ITickHandler
{
    public TickListener()
    {
		TickRegistry.registerTickHandler(this,Side.CLIENT);
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
	@SideOnly(Side.CLIENT)
	private void renderOverlay() {
		net.minecraft.client.Minecraft minecraft = net.minecraft.client.Minecraft.getMinecraft();		
	    EntityPlayer player = minecraft.thePlayer;

        if (minecraft.currentScreen == null && player.ridingEntity != null && player.ridingEntity instanceof MinecartModular)
        {
            ((MinecartModular)player.ridingEntity).renderOverlay(minecraft);
        }
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
		if (type.contains(TickType.RENDER)) {
			renderOverlay();
		}
	}

    /**
     * Returns the list of ticks this tick handler is interested in receiving at the minute
     *
     * @return
     */
    public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.RENDER);
	}

    /**
     * A profiling label for this tick handler
     * @return
     */
    public String getLabel() {
		return "StevesCarts.render";
	}		
	
	
	
}