package vswe.stevesvehicles.network;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import vswe.stevesvehicles.container.ContainerVehicle;
import vswe.stevesvehicles.block.BlockCartAssembler;
import vswe.stevesvehicles.block.ModBlocks;
import vswe.stevesvehicles.registry.RegistrySynchronizer;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.container.ContainerBase;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.tileentity.TileEntityBase;

import vswe.stevesvehicles.vehicle.entity.IVehicleEntity;


public class PacketHandler {

    //TODO? replace the networking with DataWriters and DataReaders maybe?
    //TODO Might some clean up at least

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
        try {
            DataReader dr = new DataReader(event.packet.payload());
            PacketType type = dr.readEnum(PacketType.class);

            if (type == PacketType.BLOCK) {
                int x = dr.readSignedInteger();
                int y = dr.readSignedInteger();
                int z = dr.readSignedInteger();

                World world = player.worldObj;

                ((BlockCartAssembler) ModBlocks.CART_ASSEMBLER.getBlock()).updateMultiBlock(world, x, y, z);

            }else if(type == PacketType.VEHICLE){
                int id = dr.readByte();
                int entityId = dr.readInteger();

                int len = dr.readByte();
                byte[] data = new byte[len];
                for (int i = 0; i < len; i++) {
                    data[i] = (byte)dr.readByte();
                }

                World world = player.worldObj;
                vswe.stevesvehicles.vehicle.VehicleBase vehicle = getVehicle(entityId, world);
                if (vehicle != null) {
                    receivePacketAtVehicle(vehicle, id, data, player);
                }
            }else if(type == PacketType.REGISTRY) {
                RegistrySynchronizer.onPacket(dr);
            }


        }catch(Exception ex) {
            System.out.println("The client failed to process a packet.");
            ex.printStackTrace();
        }

    }

    @SubscribeEvent
    public void onServerPacket(FMLNetworkEvent.ServerCustomPacketEvent event) {
        EntityPlayer player = ((NetHandlerPlayServer)event.handler).playerEntity;
        try {
            DataReader dr = new DataReader(event.packet.payload());
            PacketType type = dr.readEnum(PacketType.class);
            World world = player.worldObj;

            if (type == PacketType.CONTAINER) {

                if (player.openContainer instanceof ContainerPlayer) {
                    int id = dr.readByte();
                    int entityId = dr.readInteger();

                    int len = dr.readByte();
                    byte[] data = new byte[len];
                    for (int i = 0; i < len; i++) {
                        data[i] = (byte)dr.readByte();
                    }
                    VehicleBase vehicle = getVehicle(entityId, world);
                    if (vehicle != null) {
                        receivePacketAtVehicle(vehicle, id, data, player);
                    }
                }else{
                    Container con = player.openContainer;

                    if (con instanceof ContainerVehicle) {
                        int id = dr.readByte();
                        int len = dr.readByte();
                        byte[] data = new byte[len];
                        for (int i = 0; i < len; i++) {
                            data[i] = (byte)dr.readByte();
                        }


                        ContainerVehicle containerVehicle = (ContainerVehicle)con;
                        VehicleBase vehicle = containerVehicle.getVehicle();

                        receivePacketAtVehicle(vehicle, id, data, player);
                    }else if(con instanceof ContainerBase) {
                        ContainerBase containerBase =(ContainerBase)con;
                        TileEntityBase base = containerBase.getTileEntity();
                        if (base != null) {
                            base.receivePacket(dr, player);
                        }
                    }
                }
            }

        }catch(Exception ex) {
            System.out.println("The server failed to process a packet.");
            ex.printStackTrace();
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
	


    public static DataWriter getDataWriter(PacketType type) {
        DataWriter dw = new DataWriter();
        dw.writeEnum(type);
        return dw;
    }


	public static void sendPacket(int id, byte[] extraData) {
        DataWriter dw = getDataWriter(PacketType.CONTAINER);
        dw.writeByte(id);
        dw.writeByte(extraData.length);
        for (byte b : extraData) {
            dw.writeByte(b);
        }
        dw.sendToServer();
	}

    public static void sendPacketToServer(DataWriter dw) {
        dw.sendToServer();
    }

	public static void sendPacket(VehicleBase vehicleBase,int id, byte[] extraData) {
        DataWriter dw = getDataWriter(PacketType.VEHICLE);
        dw.writeByte(id);

        dw.writeInteger(vehicleBase.getEntity().getEntityId());

        dw.writeByte(extraData.length);
        for (byte b : extraData) {
            dw.writeByte(b);
        }
        dw.sendToServer();
    }
	
	public static void sendPacketToPlayer(int id, byte[] data, EntityPlayer player, VehicleBase vehicle) {
        DataWriter dw = getDataWriter(PacketType.VEHICLE);

        dw.writeByte((byte) PacketType.VEHICLE.ordinal());
        dw.writeByte((byte)id);

        dw.writeInteger(vehicle.getEntity().getEntityId());

        dw.writeByte(data.length);
        for (byte b : data) {
            dw.writeByte(b);
        }
			
        dw.sendToPlayer((EntityPlayerMP)player);
	}

    public static void sendPacketToPlayer(DataWriter dw, EntityPlayer player) {
        dw.sendToPlayer((EntityPlayerMP)player);
    }
	
	

	public static void sendBlockInfoToClients(World world, byte[] data, int x, int y, int z) {
        DataWriter dw = getDataWriter(PacketType.BLOCK);
        dw.writeInteger(x);
        dw.writeInteger(y);
        dw.writeInteger(z);

        for (byte b : data) {
            dw.writeByte(b);
        }

        dw.sendToAllPlayersAround(world, x, y, z, 64);
	}

	
	
}
