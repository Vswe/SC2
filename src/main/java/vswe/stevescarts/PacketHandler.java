package vswe.stevescarts;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import vswe.stevescarts.Blocks.BlockCartAssembler;
import vswe.stevescarts.Blocks.ModBlocks;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Containers.ContainerBase;
import vswe.stevescarts.Containers.ContainerMinecart;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.TileEntities.TileEntityBase;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
//import net.minecraft.src.NetworkManager;

public class PacketHandler implements IPacketHandler {
	@Override
	public void onPacketData(INetworkManager network, Packet250CustomPayload packet, Player player) {
		int idForCrash = -1;
		try {
			ByteArrayDataInput reader = ByteStreams.newDataInput(packet.data);
			
			int id = reader.readByte();
			idForCrash = id;
			
			//if the client receives the packet
			if (! (player instanceof EntityPlayerMP)) {
				if (id == -1) {
					int x = reader.readInt();
					int y = reader.readInt();
					int z = reader.readInt();
					
					int len = packet.length - 13;
					byte[] data = new byte[len];
					for (int i = 0; i < len; i++) {
						data[i] = reader.readByte();
					}

					EntityPlayer ep = (EntityPlayer)player;
					World world = ep.worldObj;
					
                    ((BlockCartAssembler) ModBlocks.CART_ASSEMBLER.getBlock()).updateMultiBlock(world, x, y, z);
				}else{
					int entityid = reader.readInt();
					int len = packet.length - 5;
					byte[] data = new byte[len];
					for (int i = 0; i < len; i++) {
						data[i] = reader.readByte();
					}
				
					EntityPlayer ep = (EntityPlayer)player;
					World world = ep.worldObj;
					MinecartModular cart = getCart(entityid, world);
					if (cart != null) {	
						receivePacketAtCart(cart,id, data,ep);
					}
				}


			}else{			
				//if the server receive the packet
					
				EntityPlayer ep = (EntityPlayer)player;		
				World world = ep.worldObj;	
								
				if (ep.openContainer instanceof ContainerPlayer) {
					int entityid = reader.readInt();
					int len = packet.length - 5;
					byte[] data = new byte[len];
					for (int i = 0; i < len; i++) {
						data[i] = reader.readByte();
					}
					MinecartModular cart = getCart(entityid, world);
					if (cart != null) {		
						receivePacketAtCart(cart,id, data,ep);	
					}				
				}else{
								
					int len = packet.length - 1;
					byte[] data = new byte[len];
					for (int i = 0; i < len; i++) {
						data[i] = reader.readByte();
					}
					
					Container con = ep.openContainer;	

					if (con instanceof ContainerMinecart) {
						ContainerMinecart conMC = (ContainerMinecart)con;
						MinecartModular cart = conMC.cart;
						
						receivePacketAtCart(cart, id,data,ep);
					}else if(con instanceof ContainerBase) {
						ContainerBase conBase =(ContainerBase)con;
						TileEntityBase base = conBase.getTileEntity();
						if (base != null) {
							base.receivePacket(id, data, (EntityPlayer)player);
						}
					}
				}
			}
		}catch(Exception ex) {
			System.out.println("The " + (player instanceof EntityPlayerMP ? "client" : "server") +  " failed to process a packet with " + (idForCrash == -1 ?  "unknown id" : "id " + idForCrash ));		
		}
	
	}

	private void receivePacketAtCart(MinecartModular cart, int id,byte [] data, EntityPlayer player) {	
		for (ModuleBase module : cart.getModules()) {
			if (id >= module.getPacketStart() && id < module.getPacketStart() + module.totalNumberOfPackets()) {
				module.delegateReceivedPacket(id-module.getPacketStart(),data, player);
				break;
			}
		}
	}
	
	private MinecartModular getCart(int ID, World world) {
		for (Object e : world.loadedEntityList) {
			if (e instanceof Entity && ((Entity)e).entityId == ID && e instanceof MinecartModular) {
				return (MinecartModular)e;
			}
		}
		return null;
	}
	
	
	public static void sendPacket(int id, byte[] extraData) {		
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		DataOutputStream ds = new DataOutputStream(bs);

		try {
			ds.writeByte((byte)id);

			for (byte b : extraData) {
				ds.writeByte(b);
			}
			
		} catch (IOException e) {
			
		}
		
		PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket("SC2", bs.toByteArray()));				
	}
	
	public static void sendPacket(MinecartModular cart,int id, byte[] extraData) {		
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		DataOutputStream ds = new DataOutputStream(bs);

		try {
			ds.writeByte((byte)id);

			ds.writeInt(cart.entityId);
			
			for (byte b : extraData) {
				ds.writeByte(b);
			}
			
		} catch (IOException e) {
			
		}
		
		PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket("SC2", bs.toByteArray()));				
	}	
	
	public static void sendPacketToPlayer(int id, byte[] data, EntityPlayer player, MinecartModular cart) {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		DataOutputStream ds = new DataOutputStream(bs);

		try {
			ds.writeByte((byte)id);

			ds.writeInt(cart.entityId);
			
			for (byte b : data) {
				ds.writeByte(b);
			}
			
		} catch (IOException e) {
			
		}
		
		PacketDispatcher.sendPacketToPlayer(PacketDispatcher.getPacket("SC2", bs.toByteArray()), (Player)player);					
	}	
	
	

	public static void sendBlockInfoToClients(World world,byte[] data, int x, int y, int z) {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		DataOutputStream ds = new DataOutputStream(bs);

		try {
			ds.writeByte((byte)-1);

			ds.writeInt(x);
			ds.writeInt(y);
			ds.writeInt(z);
			
			for (byte b : data) {
				ds.writeByte(b);
			}
			
		} catch (IOException e) {
			
		}
		
		PacketDispatcher.sendPacketToAllAround(x, y, z, 64D, world.provider.dimensionId, PacketDispatcher.getPacket("SC2", bs.toByteArray()));			
	}

	
	
}
