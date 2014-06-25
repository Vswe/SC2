package vswe.stevescarts.old.TileEntities;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.tileentity.TileEntity;
import vswe.stevescarts.containers.ContainerBase;
import vswe.stevescarts.client.interfaces.GuiBase;

public abstract class TileEntityBase extends TileEntity {

	public TileEntityBase() {
		super();
	}
	
	/**
	 * Called when this tile entity receives a packet from the client.
	 * @param id The id of the packet
	 * @param data The bytes the client sent
	 * @param player The player associated with the client sending the packet
	 */
	public void receivePacket(int id, byte[] data, EntityPlayer player) {}	
	
	/**
	 * Returns a new interface for this tile entity
	 * @param inv The inventory of the player opening the interface
	 * @return The interface to be shown
	 */
	@SideOnly(Side.CLIENT)
	public abstract GuiBase getGui(InventoryPlayer inv);
	
	/**
	 * Returns a new container for this tile entity
	 * @param inv The inventory of the player opening the container
	 * @return The container to be used
	 */
	public abstract ContainerBase getContainer(InventoryPlayer inv);
	
	/**
	 *Synchronizes the client with the server by sending some data to it
	 * @param con The container associated with the player on the server
	 * @param crafting The player to send information to
	 * @param id The id of this data
	 * @param data The data to send
	 */
	public void updateGuiData(Container con, ICrafting crafting, int id, short data) {
		crafting.sendProgressBarUpdate(con, id, data);
	}
	
	/**
	 * Initializes the synchronizing from the server to the client
	 * @param con The container on the server for the player
	 * @param crafting The player
	 */
	public void initGuiData(Container con, ICrafting crafting) {}

	/**
	 * Check if some data has to be synchronized from the server to the client
	 * @param con The container on the server for the player
	 * @param crafting The player
	 */
	public void checkGuiData(Container con, ICrafting crafting) {}
	

	
	/**
	 * Called when the client is synchronized by receiving new data from the server
	 * @param id The id of the data
	 * @param data The data itself
	 */
	public void receiveGuiData(int id, short data) {}	
	
	/**
	 * If this Tile Entity can be used by the given player
	 * @param entityplayer The player that wants to interact
	 * @return If the player can use this tile entity
	 */
   public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }

        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }
	
	public short getShortFromInt(boolean first, int val) {
		if (first) {
			return (short)(val & 65535);
		}else {
			return (short)((val >> 16) & 65535);
		}
	}
	
	public int getIntFromShort(boolean first, int oldVal, short val) {
		if (first) {
			oldVal = (oldVal & -65536) | val;	
		}else{
			oldVal = (oldVal & 65535) | (val << 16);		
		}
		
		return oldVal;
	}	  
   
}
