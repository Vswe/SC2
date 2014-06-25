package vswe.stevesvehicles.registries;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistryLoader<R extends IRegistry<E>, E> {

    private static List<RegistryLoader> registryLoaderList = new ArrayList<RegistryLoader>();

    public RegistryLoader() {
        registryLoaderList.add(this);
    }

    //TODO sync to the clients
    private HashMap<Integer, E> idToObjectMapping = new HashMap<Integer, E>();
    private HashMap<String, Integer> nameToIdMapping = new HashMap<String, Integer>();
    private Map<String, R> registries = new HashMap<String, R>();
    private int nextId;


    private static final String NBT_REGISTRIES = "Registries";
    private static final String NBT_NEXT_ID = "NextModuleId";
    private static final String NBT_MODULES = "Modules";
    private static final String NBT_KEY = "K";
    private static final String NBT_VALUE = "V";
    private static final int NBT_COMPOUND_TYPE_ID = 10;

    //TODO call this on world save
    public static void writeData(NBTTagCompound compound) {
        NBTTagList registriesList = new NBTTagList();
        for (RegistryLoader registryLoader : registryLoaderList) {
            NBTTagCompound registryCompound = new NBTTagCompound();
            registryLoader.writeRegistryData(registryCompound);
            registriesList.appendTag(registryCompound);
        }
        compound.setTag(NBT_REGISTRIES, registriesList);
    }


    private void writeRegistryData(NBTTagCompound compound) {
        compound.setShort(NBT_NEXT_ID, (short)nextId);
        NBTTagList list = new NBTTagList();

        for (Map.Entry<String, Integer> entry : nameToIdMapping.entrySet()) {
            if (idToObjectMapping.containsKey(entry.getValue())) {
                NBTTagCompound moduleCompound = new NBTTagCompound();
                moduleCompound.setString(NBT_KEY, entry.getKey());
                moduleCompound.setShort(NBT_VALUE, (short)(int)entry.getValue());
                list.appendTag(moduleCompound);
            }
        }

        compound.setTag(NBT_MODULES, list);
    }

    //TODO call this on world load
    public static void readData(NBTTagCompound compound) {
        if (compound.hasKey(NBT_REGISTRIES)) {
            NBTTagList registriesList = compound.getTagList(NBT_REGISTRIES, NBT_COMPOUND_TYPE_ID);
            for (int i = 0; i < registriesList.tagCount(); i++) {
                NBTTagCompound registryCompound = registriesList.getCompoundTagAt(i);
                registryLoaderList.get(i).readRegistryData(registryCompound);
            }
        }else{
            for (RegistryLoader registryLoader : registryLoaderList) {
                registryLoader.readRegistryData(null);
            }
        }
    }

    private void readRegistryData(NBTTagCompound compound) {
        nextId = 0;
        idToObjectMapping.clear();
        nameToIdMapping.clear();

        if (compound != null && compound.hasKey(NBT_NEXT_ID)) {
            nextId = compound.getShort(NBT_NEXT_ID);
            NBTTagList tags = compound.getTagList(NBT_MODULES, NBT_COMPOUND_TYPE_ID);
            for (int i = 0; i < tags.tagCount(); i++) {
                NBTTagCompound moduleCompound = tags.getCompoundTagAt(i);
                String key = moduleCompound.getString(NBT_KEY);
                int value = (int)moduleCompound.getShort(NBT_VALUE);
                System.out.println("Loaded name to id mapping. (K = " + key + ", V = " + value + ")"); //TODO Move to a log file?
                nameToIdMapping.put(key, value);
            }
        }

        for (R moduleRegistry : registries.values()) {
            for (E module : moduleRegistry.getElements()) {
                String code = moduleRegistry.getFullCode(module);
                Integer id = nameToIdMapping.get(code);
                if (id == null) {
                    id = nextId++;
                    nameToIdMapping.put(code, id);
                    System.out.println("Added new name to id mapping. (K = " + code + ", V = " + id + ")"); //TODO Move to a log file?
                }

                idToObjectMapping.put(id, module);
            }
        }
    }

    public void add(R registry) {
        if (registries.containsKey(registry.getCode())) {
            System.err.println("A registry with this code has already been registered. Failed to register a second registry with code " + registry.getCode());
        }else{
            registries.put(registry.getCode(), registry);
        }
    }
}
