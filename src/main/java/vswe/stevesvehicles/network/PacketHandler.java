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
import vswe.stevesvehicles.buoy.EntityBuoy;
import vswe.stevesvehicles.container.ContainerBuoy;
import vswe.stevesvehicles.container.ContainerVehicle;
import vswe.stevesvehicles.block.BlockCartAssembler;
import vswe.stevesvehicles.block.ModBlocks;
import vswe.stevesvehicles.registry.RegistrySynchronizer;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.container.ContainerBase;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.tileentity.TileEntityBase;

import vswe.stevesvehicles.vehicle.entity.EntityModularBoat;
import vswe.stevesvehicles.vehicle.entity.IVehicleEntity;


public class PacketHandler {

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
                int entityId = dr.readInteger();

                World world = player.worldObj;
                VehicleBase vehicle = getVehicle(entityId, world);
                if (vehicle != null) {
                    receivePacketAtVehicle(vehicle, dr, player);
                }
            }else if(type == PacketType.REGISTRY) {
                RegistrySynchronizer.onPacket(dr);
            }else if(type == PacketType.BUOY) {
                Container container = player.openContainer;
                if (container instanceof ContainerBuoy) {
                    ((ContainerBuoy)container).receiveInfo(dr);
                }
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

            if (type == PacketType.BLOCK || type == PacketType.VEHICLE) {
                Container container = player.openContainer;
                if (container instanceof ContainerPlayer) {
                    int entityId = dr.readInteger();
                    VehicleBase vehicle = getVehicle(entityId, world);
                    if (vehicle != null) {
                        receivePacketAtVehicle(vehicle, dr, player);
                    }
                }else if (container instanceof ContainerVehicle) {
                    ContainerVehicle containerVehicle = (ContainerVehicle)container;
                    VehicleBase vehicle = containerVehicle.getVehicle();

                    receivePacketAtVehicle(vehicle, dr, player);
                }else if(container instanceof ContainerBase) {
                    ContainerBase containerBase = (ContainerBase)container;
                    TileEntityBase base = containerBase.getTileEntity();
                    if (base != null) {
                        base.receivePacket(dr, player);
                    }
                }
            }else if(type == PacketType.BOAT_MOVEMENT) {
                if (player.ridingEntity instanceof EntityModularBoat) {
                    ((EntityModularBoat)player.ridingEntity).onMovementPacket(dr);
                }
            }

        }catch(Exception ex) {
            System.out.println("The server failed to process a packet.");
            ex.printStackTrace();
        }

    }



	private void receivePacketAtVehicle(VehicleBase vehicle, DataReader dr, EntityPlayer player) {
        ModuleBase.delegateReceivedPacket(vehicle, dr, player);
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


    public static void sendPacketToServer(DataWriter dw) {
        dw.sendToServer();
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
