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
import vswe.stevesvehicles.container.ContainerVehicle;
import vswe.stevesvehicles.old.Blocks.BlockCartAssembler;
import vswe.stevesvehicles.old.Blocks.ModBlocks;
import vswe.stevesvehicles.registry.RegistrySynchronizer;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;
import vswe.stevesvehicles.container.ContainerBase;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.old.TileEntities.TileEntityBase;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import vswe.stevesvehicles.vehicle.entity.IVehicleEntity;


import static vswe.stevesvehicles.old.StevesVehicles.CHANNEL;
import static vswe.stevesvehicles.old.StevesVehicles.packetHandler;

public class PacketHandler {

    //TODO? replace the networking with DataWriters and DataReaders maybe?
    //TODO Might some clean up at least

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
        try {
            byte[] bytes = event.packet.payload().array();
            ByteArrayDataInput reader = ByteStreams.newDataInput(bytes);

            PacketType type = PacketType.values()[reader.readByte()];


            if (type == PacketType.BLOCK) {
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
            }else if(type == PacketType.VEHICLE){
                int id = reader.readByte();
                int entityId = reader.readInt();
                int len = bytes.length - 6;
                byte[] data = new byte[len];
                for (int i = 0; i < len; i++) {
                    data[i] = reader.readByte();
                }

                World world = player.worldObj;
                vswe.stevesvehicles.vehicle.VehicleBase vehicle = getVehicle(entityId, world);
                if (vehicle != null) {
                    receivePacketAtVehicle(vehicle, id, data, player);
                }
            }else if(type == PacketType.REGISTRY) {
                RegistrySynchronizer.onPacket(reader);
            }


        }catch(Exception ex) {
            System.out.println("The client failed to process a packet.");
        }

    }

    @SubscribeEvent
    public void onServerPacket(FMLNetworkEvent.ServerCustomPacketEvent event) {
        EntityPlayer player = ((NetHandlerPlayServer)event.handler).playerEntity;
        try {
            byte[] bytes = event.packet.payload().array();
            ByteArrayDataInput reader = ByteStreams.newDataInput(bytes);

            PacketType type = PacketType.values()[reader.readByte()];
            World world = player.worldObj;

            if (type == PacketType.VEHICLE || type == PacketType.BLOCK) {
                int id = reader.readByte();
                if (player.openContainer instanceof ContainerPlayer) {
                    int entityId = reader.readInt();
                    int len = bytes.length - 5;
                    byte[] data = new byte[len];
                    for (int i = 0; i < len; i++) {
                        data[i] = reader.readByte();
                    }
                    vswe.stevesvehicles.vehicle.VehicleBase vehicle = getVehicle(entityId, world);
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
                        vswe.stevesvehicles.vehicle.VehicleBase vehicle = containerVehicle.getVehicle();

                        receivePacketAtVehicle(vehicle, id, data, player);
                    }else if(con instanceof ContainerBase) {
                        ContainerBase containerBase =(ContainerBase)con;
                        TileEntityBase base = containerBase.getTileEntity();
                        if (base != null) {
                            base.receivePacket(id, data, player);
                        }
                    }
                }
            }

        }catch(Exception ex) {
            System.out.println("The server failed to process a packet.");
        }

    }



	private void receivePacketAtVehicle(vswe.stevesvehicles.vehicle.VehicleBase vehicle, int id,byte [] data, EntityPlayer player) {
		for (ModuleBase module : vehicle.getModules()) {
			if (id >= module.getPacketStart() && id < module.getPacketStart() + module.totalNumberOfPackets()) {
				module.delegateReceivedPacket(id-module.getPacketStart(),data, player);
				break;
			}
		}
	}
	
	private vswe.stevesvehicles.vehicle.VehicleBase getVehicle(int id, World world) {
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
            ds.writeByte((byte) PacketType.VEHICLE.ordinal());
			ds.writeByte((byte)id);

			for (byte b : extraData) {
				ds.writeByte(b);
			}
			
		}catch (IOException ignored) {}
		
		packetHandler.sendToServer(createPacket(bs.toByteArray()));

        try {
            bs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ds.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

    private static FMLProxyPacket createPacket(byte[] bytes) {
        ByteBuf buf = Unpooled.copiedBuffer(bytes);
        return new FMLProxyPacket(buf, CHANNEL);
    }

	public static void sendPacket(EntityModularCart cart,int id, byte[] extraData) {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		DataOutputStream ds = new DataOutputStream(bs);

		try {
            ds.writeByte((byte) PacketType.VEHICLE.ordinal());
			ds.writeByte((byte)id);

			ds.writeInt(cart.getEntityId());
			
			for (byte b : extraData) {
				ds.writeByte(b);
			}
			
		}catch (IOException ignored) {}

        packetHandler.sendToServer(createPacket(bs.toByteArray()));

        try {
            bs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ds.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public static void sendPacketToPlayer(int id, byte[] data, EntityPlayer player, vswe.stevesvehicles.vehicle.VehicleBase vehicle) {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		DataOutputStream ds = new DataOutputStream(bs);

		try {
            ds.writeByte((byte) PacketType.VEHICLE.ordinal());
			ds.writeByte((byte)id);

			ds.writeInt(vehicle.getEntity().getEntityId());
			
			for (byte b : data) {
				ds.writeByte(b);
			}
			
		}catch (IOException ignored) {}

        packetHandler.sendTo(createPacket(bs.toByteArray()), (EntityPlayerMP) player);

        try {
            bs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ds.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

    public static void sendPacketToPlayer(byte[] data, EntityPlayer player) {
        packetHandler.sendTo(createPacket(data), (EntityPlayerMP) player);
    }
	
	

	public static void sendBlockInfoToClients(World world,byte[] data, int x, int y, int z) {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		DataOutputStream ds = new DataOutputStream(bs);

		try {
            ds.writeByte((byte)PacketType.BLOCK.ordinal());

			ds.writeInt(x);
			ds.writeInt(y);
			ds.writeInt(z);
			
			for (byte b : data) {
				ds.writeByte(b);
			}
			
		}catch (IOException ignored) {}

        packetHandler.sendToAllAround(createPacket(bs.toByteArray()), new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, 64));

        try {
            bs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ds.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	
	
}
