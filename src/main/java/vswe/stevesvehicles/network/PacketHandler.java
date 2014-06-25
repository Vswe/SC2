package vswe.stevesvehicles.network;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import vswe.stevesvehicles.containers.ContainerVehicle;
import vswe.stevesvehicles.old.Blocks.BlockCartAssembler;
import vswe.stevesvehicles.old.Blocks.ModBlocks;
import vswe.stevesvehicles.vehicles.VehicleBase;
import vswe.stevesvehicles.vehicles.entities.EntityModularCart;
import vswe.stevesvehicles.containers.ContainerBase;
import vswe.stevesvehicles.modules.ModuleBase;
import vswe.stevesvehicles.old.TileEntities.TileEntityBase;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import vswe.stevesvehicles.vehicles.entities.IVehicleEntity;


import static vswe.stevesvehicles.old.StevesCarts.CHANNEL;
import static vswe.stevesvehicles.old.StevesCarts.packetHandler;

public class PacketHandler {


    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
        int idForCrash = -1;
        try {
            byte[] bytes = event.packet.payload().array();
            ByteArrayDataInput reader = ByteStreams.newDataInput(bytes);

            int id = reader.readByte();
            idForCrash = id;


            if (id == -1) {
                int x = reader.readInt();
                int y = reader.readInt();
                int z = reader.readInt();

                int len = bytes.length - 13;
                byte[] data = new byte[len];
                for (int i = 0; i < len; i++) {
                    data[i] = reader.readByte();
                }

                World world = player.worldObj;

                ((BlockCartAssembler) ModBlocks.CART_ASSEMBLER.getBlock()).updateMultiBlock(world, x, y, z);
            }else{
                int entityId = reader.readInt();
                int len = bytes.length - 5;
                byte[] data = new byte[len];
                for (int i = 0; i < len; i++) {
                    data[i] = reader.readByte();
                }

                World world = player.worldObj;
                VehicleBase vehicle = getVehicle(entityId, world);
                if (vehicle != null) {
                    receivePacketAtVehicle(vehicle, id, data, player);
                }
            }


        }catch(Exception ex) {
            System.out.println("The client failed to process a packet with " + (idForCrash == -1 ?  "unknown id" : "id " + idForCrash ));
        }

    }

    @SubscribeEvent
    public void onServerPacket(FMLNetworkEvent.ServerCustomPacketEvent event) {
        EntityPlayer player = ((NetHandlerPlayServer)event.handler).playerEntity;
        int idForCrash = -1;
        try {
            byte[] bytes = event.packet.payload().array();
            ByteArrayDataInput reader = ByteStreams.newDataInput(bytes);

            int id = reader.readByte();
            idForCrash = id;



            World world = player.worldObj;

            if (player.openContainer instanceof ContainerPlayer) {
                int entityId = reader.readInt();
                int len = bytes.length - 5;
                byte[] data = new byte[len];
                for (int i = 0; i < len; i++) {
                    data[i] = reader.readByte();
                }
                VehicleBase vehicle = getVehicle(entityId, world);
                if (vehicle != null) {
                    receivePacketAtVehicle(vehicle, id, data, player);
                }
            }else{

                int len = bytes.length - 1;
                byte[] data = new byte[len];
                for (int i = 0; i < len; i++) {
                    data[i] = reader.readByte();
                }

                Container con = player.openContainer;

                if (con instanceof ContainerVehicle) {
                    ContainerVehicle containerVehicle = (ContainerVehicle)con;
                    VehicleBase vehicle = containerVehicle.getVehicle();

                    receivePacketAtVehicle(vehicle, id, data, player);
                }else if(con instanceof ContainerBase) {
                    ContainerBase containerBase =(ContainerBase)con;
                    TileEntityBase base = containerBase.getTileEntity();
                    if (base != null) {
                        base.receivePacket(id, data, player);
                    }
                }
            }

        }catch(Exception ex) {
            System.out.println("The server failed to process a packet with " + (idForCrash == -1 ?  "unknown id" : "id " + idForCrash ));
        }

    }



	private void receivePacketAtVehicle(VehicleBase vehicle, int id,byte [] data, EntityPlayer player) {
		for (ModuleBase module : vehicle.getModules()) {
			if (id >= module.getPacketStart() && id < module.getPacketStart() + module.totalNumberOfPackets()) {
				module.delegateReceivedPacket(id-module.getPacketStart(),data, player);
				break;
			}
		}
	}
	
	private VehicleBase getVehicle(int id, World world) {
        Entity entity = world.getEntityByID(id);

        if (entity instanceof IVehicleEntity) {
            return ((IVehicleEntity)entity).getVehicle();
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
			
		}catch (IOException ignored) {}
		
		packetHandler.sendToServer(createPacket(bs.toByteArray()));
	}

    private static FMLProxyPacket createPacket(byte[] bytes) {
        ByteBuf buf = Unpooled.copiedBuffer(bytes);
        return new FMLProxyPacket(buf, CHANNEL);
    }

	public static void sendPacket(EntityModularCart cart,int id, byte[] extraData) {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		DataOutputStream ds = new DataOutputStream(bs);

		try {
			ds.writeByte((byte)id);

			ds.writeInt(cart.getEntityId());
			
			for (byte b : extraData) {
				ds.writeByte(b);
			}
			
		}catch (IOException ignored) {}

        packetHandler.sendToServer(createPacket(bs.toByteArray()));
	}	
	
	public static void sendPacketToPlayer(int id, byte[] data, EntityPlayer player, VehicleBase vehicle) {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		DataOutputStream ds = new DataOutputStream(bs);

		try {
			ds.writeByte((byte)id);

			ds.writeInt(vehicle.getEntity().getEntityId());
			
			for (byte b : data) {
				ds.writeByte(b);
			}
			
		}catch (IOException ignored) {}

        packetHandler.sendTo(createPacket(bs.toByteArray()), (EntityPlayerMP) player);
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
			
		}catch (IOException ignored) {}

        packetHandler.sendToAllAround(createPacket(bs.toByteArray()), new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, 64));
	}

	
	
}
