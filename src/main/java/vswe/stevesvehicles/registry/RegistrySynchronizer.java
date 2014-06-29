package vswe.stevesvehicles.registry;


import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.StartupQuery;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import vswe.stevesvehicles.network.PacketHandler;
import vswe.stevesvehicles.network.PacketType;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Map;

public class RegistrySynchronizer {

    public RegistrySynchronizer() {
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        DataOutputStream ds = new DataOutputStream(bs);

        try {
            ds.writeByte((byte) PacketType.REGISTRY.ordinal());
            writeSynchronizedData(ds);
        }catch (IOException ignored) {}

        PacketHandler.sendPacketToPlayer(bs.toByteArray(), player);
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

    private static void writeSynchronizedData(DataOutputStream ds) throws IOException {
        for (RegistryLoader registryLoader : RegistryLoader.registryLoaderList) {
            ds.writeShort((short)registryLoader.nextId);
            ds.writeShort(registryLoader.nameToIdMapping.size());
            //WHAT THE SERIOUSLY FUCK, for some reason the entrySet() returns a set of objects. When it's used in RegistryLoader it returns a set of Map.Entry<String, Integer> :S
            for (Object o : registryLoader.nameToIdMapping.entrySet()) {
                Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)o;

                ds.writeUTF(entry.getKey());
                ds.writeShort(entry.getValue());
            }
        }
    }

    public static void onPacket(ByteArrayDataInput reader) {
        for (RegistryLoader registryLoader : RegistryLoader.registryLoaderList) {
            registryLoader.clearLoadedRegistryData();

            registryLoader.nextId = reader.readShort();
            int count = reader.readShort();
            for (int i = 0; i < count; i++) {
                String name = reader.readUTF();
                int id = reader.readShort();
                registryLoader.nameToIdMapping.put(name, id); //WHAT THE SERIOUSLY FUCK 2, for some reaosn it has no clue nameToIdMapping is a <String, Integer>
            }

            registryLoader.loadFromRegistries();
        }
    }

    private static final String MAIN_NAME = "registries.dat";
    private static final String OLD_NAME = "registries_old.dat";
    private static final String NEW_NAME = "registries_new.dat";

    @SubscribeEvent
    public void onLoad(WorldEvent.Load event) {
        if (!event.world.isRemote && event.world.provider.dimensionId == 0) {
            boolean success = false;
            try {
                File mainFile = getInfoPath(event.world, MAIN_NAME);
                File oldFile = getInfoPath(event.world, OLD_NAME);

                if (mainFile == null || oldFile == null) {
                    System.out.println("Aborted registry reading, failed to locate where to find the files.");
                    return;
                }

                NBTTagCompound compound = loadCompound(mainFile);
                if (compound == null) {
                    compound = loadCompound(oldFile);
                }

                if (compound != null) {
                    RegistryLoader.readData(compound);
                    success = true;
                    System.out.println("Loading registry state successfully");
                }
            }finally {
                if (!success) {
                    System.out.println("Failed to load registry state");
                }
            }
        }
    }

    private NBTTagCompound loadCompound(File file) {
        if (file.exists()) {
            try {
                return CompressedStreamTools.readCompressed(new FileInputStream(file));
            } catch (StartupQuery.AbortedException e) {
                throw e; //don't know why to do this to be honest, got it from minecraft's SaveHandler
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @SubscribeEvent
    public void onSave(WorldEvent.Save event) {
        if (!event.world.isRemote && event.world.provider.dimensionId == 0) {
            boolean success = false;
            try {
                NBTTagCompound compound = new NBTTagCompound();
                RegistryLoader.writeData(compound);
                File mainFile = getInfoPath(event.world, MAIN_NAME);
                File oldFile = getInfoPath(event.world, OLD_NAME);
                File newFile = getInfoPath(event.world, NEW_NAME);

                if (mainFile == null || oldFile == null || newFile == null) {
                    System.out.println("Aborted registry writing, failed to locate where to put the files.");
                    return;
                }


                CompressedStreamTools.writeCompressed(compound, new FileOutputStream(newFile));

                if (newFile.exists()) {
                    if (oldFile.exists()) {
                        if (!oldFile.delete()) return;
                    }

                    if (mainFile.exists()) {
                        if (!mainFile.renameTo(oldFile)) return;
                        if (!mainFile.delete()) return;
                    }

                    if (newFile.renameTo(mainFile)) return;
                    if (!newFile.delete()) return;

                    success = true;
                    System.out.println("Saving registry state successfully");
                }
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (!success) {
                    System.out.println("Failed to save registry state");
                }
            }
        }
    }

    private File getInfoPath(World world, String fileName) {
        File dir = new File(((WorldServer)world).getChunkSaveLocation(), "sv");

        try {
            createFolder(dir);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return new File(dir, fileName);
    }

    private void createFolder(File dir) throws IOException {
        if (dir == null) {
            return;
        }

        File parent = dir.getParentFile();
        createFolder(parent);
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
    }
}
