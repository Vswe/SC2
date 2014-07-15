package vswe.stevesvehicles.module.data;


import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import vswe.stevesvehicles.localization.entry.info.LocalizationLabel;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.data.registry.ModuleRegistry;
import vswe.stevesvehicles.nbt.NBTHelper;
import vswe.stevesvehicles.item.ModItems;
import vswe.stevesvehicles.util.Tuple;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.vehicle.VehicleRegistry;
import vswe.stevesvehicles.vehicle.VehicleType;
import vswe.stevesvehicles.vehicle.version.VehicleVersion;

import java.util.ArrayList;
import java.util.List;

public final class ModuleDataItemHandler {

    public static String checkForErrors(ModuleDataHull hull, ArrayList<ModuleData> modules) {
        if (hull.getValidVehicles() == null || hull.getValidVehicles().isEmpty()) {
            return LocalizationLabel.NO_VEHICLE_TYPE.translate();
        }

        VehicleType vehicleType = hull.getValidVehicles().get(0);
        return checkForErrors(vehicleType, hull, modules);

    }

    public static String checkForErrors(VehicleType vehicle, ModuleDataHull hull, List<ModuleData> modules) {
        //Normal errors here
        if (getTotalCost(modules) > hull.getModularCapacity()) {
            return LocalizationLabel.CAPACITY_ERROR.translate();
        }


        for (int i = 0; i < modules.size(); i++) {
            ModuleData mod1 = modules.get(i);

            if (mod1.getValidVehicles() == null || !mod1.getValidVehicles().contains(vehicle)) {
                return LocalizationLabel.INVALID_VEHICLE_TYPE.translate(mod1.getName(), vehicle.getName());
            }

            if (mod1.getCost() > hull.getComplexityMax()) {
                return LocalizationLabel.COMPLEXITY_ERROR.translate(mod1.getName());
            }
            if (mod1.getParent() != null && !modules.contains(mod1.getParent())) {
                return LocalizationLabel.PARENT_ERROR.translate(mod1.getName(), mod1.getParent().getName());
            }
            if (mod1.getNemesis() != null) {
                for (ModuleData nemesis : mod1.getNemesis()) {
                    if (modules.contains(nemesis)) {
                        return LocalizationLabel.NEMESIS_ERROR.translate(mod1.getName(), nemesis.getName());
                    }
                }
            }
            if (mod1.getRequirement() != null){
                for (ModuleDataGroup group : mod1.getRequirement()) {
                    int count = 0;
                    for (ModuleData mod2 : group.getModules()) {
                        for (ModuleData mod3 : modules) {
                            if (mod2.equals(mod3)) {
                                count++;
                            }
                        }
                    }
                    if (count < group.getCount()) {
                        return LocalizationLabel.PARENT_ERROR.translate(mod1.getName(), group.getCountName() + " " + group.getName());
                    }
                }
            }

            for (int j = i+1; j < modules.size(); j++) {
                ModuleData mod2 = modules.get(j);

                if (mod1 == mod2) {
                    if (!mod1.getAllowDuplicate()) {
                        return LocalizationLabel.DUPLICATE_ERROR.translate(mod1.getName());
                    }
                }else if (mod1.getSides() != null && mod2.getSides() != null) {
                    ModuleSide clash = null;
                    for (ModuleSide side1 : mod1.getSides()) {
                        for (ModuleSide side2 : mod2.getSides()) {
                            if (side1 == side2) {
                                clash = side1;
                                break;
                            }
                        }
                        if (clash != null) {
                            break;
                        }
                    }
                    if (clash != null) {
                        return LocalizationLabel.CLASH_ERROR.translate(mod1.getName(), mod2.getName(), clash.toString());
                    }
                }
            }
        }

        return null;
    }

    public static int getTotalCost(List<ModuleData> modules) {
        int currentCost = 0;
        for (ModuleData module : modules) {
            currentCost += module.getCost();
        }
        return currentCost;
    }


    public static boolean isValidModuleItem(ModuleType type, ItemStack itemstack) {
        ModuleData module = ModItems.modules.getModuleData(itemstack);
        return isValidModuleItem(type, module);
    }

    public static boolean isValidModuleItem(ModuleType type, ModuleData module) {
        return module != null && module.getModuleType() == type;
    }

    public static boolean isItemOfModularType(ItemStack itemstack, Class<? extends ModuleBase> validClass) {
        if (itemstack.getItem() == ModItems.modules) {
            ModuleData module = ModItems.modules.getModuleData(itemstack);
            if (module != null) {
                if (validClass.isAssignableFrom(module.getModuleClass())) {
                    return true;
                }
            }
        }

        return false;
    }

    public static ItemStack createModularVehicle(List<ItemStack> moduleItems) {
        VehicleType vehicleType = null;
        List<ModuleData> modules = new ArrayList<ModuleData>();
        for (ItemStack moduleItem : moduleItems) {
            ModuleData moduleData = ModItems.modules.getModuleData(moduleItem);
            if (moduleData != null) {
                modules.add(moduleData);
                if (moduleData.getModuleType() == ModuleType.HULL) {
                    if (moduleData.getValidVehicles() == null ||moduleData.getValidVehicles().isEmpty()) {
                        return null;
                    }
                    vehicleType = moduleData.getValidVehicles().get(0);
                }
            }
        }

        if (vehicleType != null) {
            return createModularVehicle(vehicleType, modules, null);
        }else{
            return null;
        }
    }



    private static final String NBT_VEHICLE_ID = "VehicleId";
    public static ItemStack createModularVehicle(VehicleType vehicle, List<ModuleData> moduleDataList, List<ModuleBase> modules) {
        if (vehicle == null) {
            return null;
        }

        int vehicleId = VehicleRegistry.getInstance().getIdFromType(vehicle);
        if (vehicleId < 0) {
            return null;
        }

        NBTTagCompound data = new NBTTagCompound();
        data.setByte(NBT_VEHICLE_ID, (byte)vehicleId);

        NBTTagList modulesCompoundList = new NBTTagList();
        for (int i = 0; i < moduleDataList.size(); i++) {
            ModuleData moduleData = moduleDataList.get(i);
            ModuleBase module = modules != null ? modules.get(i) : null;
            NBTTagCompound moduleCompound = new NBTTagCompound();
            int id = ModuleRegistry.getIdFromModule(moduleData);
            if (id >= 0) {
                moduleCompound.setShort(VehicleBase.NBT_ID, (short) id);

                if (module != null) {
                    moduleData.addExtraData(moduleCompound, module);
                }else{
                    moduleData.addDefaultExtraData(moduleCompound);
                }

                modulesCompoundList.appendTag(moduleCompound);
            }
        }

        data.setTag(VehicleBase.NBT_MODULES, modulesCompoundList);

        ItemStack vehicleItem = new ItemStack(ModItems.vehicles, 1);
        vehicleItem.setTagCompound(data);
        VehicleVersion.addVersion(vehicleItem);

        return vehicleItem;
    }

    public static ItemStack createModularVehicle(VehicleBase vehicle) {
        return createModularVehicle(vehicle.getVehicleType(), vehicle.getModuleDataList(), vehicle.getModules());
    }

    public static List<ModuleData> getModulesFromItem(ItemStack item) {
        return getModulesFromItem(item, VehicleBase.NBT_MODULES);
    }

    public static List<ModuleData> getSpareModulesFromItem(ItemStack item) {
        return getModulesFromItem(item, VehicleBase.NBT_SPARES);
    }

    private static List<ModuleData> getModulesFromItem(ItemStack item, String tag) {
        NBTTagCompound compound = item.getTagCompound();
        if (compound != null && compound.hasKey(tag)) {
            List<ModuleData> modules = new ArrayList<ModuleData>();
            NBTTagList modulesList = compound.getTagList(tag, NBTHelper.COMPOUND.getId());
            for (int i = 0; i < modulesList.tagCount(); i++) {
                int id = modulesList.getCompoundTagAt(i).getShort(VehicleBase.NBT_ID);
                ModuleData module = ModuleRegistry.getModuleFromId(id);
                if (module != null) {
                    modules.add(module);
                }
            }
            return modules;
        }
        return null;
    }

    public static List<Tuple<ModuleData, NBTTagCompound>> getModulesAndCompoundsFromItem(ItemStack item) {
        return getModulesAndCompoundsFromItem(item, VehicleBase.NBT_MODULES);
    }

    public static List<Tuple<ModuleData, NBTTagCompound>> getSpareModulesAndCompoundsFromItem(ItemStack item) {
        return getModulesAndCompoundsFromItem(item, VehicleBase.NBT_SPARES);
    }

    private static List<Tuple<ModuleData, NBTTagCompound>> getModulesAndCompoundsFromItem(ItemStack item, String tag) {
        NBTTagCompound compound = item.getTagCompound();
        if (compound != null && compound.hasKey(tag)) {
            List<Tuple<ModuleData, NBTTagCompound>> modules = new ArrayList<Tuple<ModuleData, NBTTagCompound>>();
            NBTTagList modulesList = compound.getTagList(tag, NBTHelper.COMPOUND.getId());
            for (int i = 0; i < modulesList.tagCount(); i++) {
                NBTTagCompound moduleCompound = modulesList.getCompoundTagAt(i);
                int id = moduleCompound.getShort(VehicleBase.NBT_ID);
                ModuleData module = ModuleRegistry.getModuleFromId(id);
                if (module != null) {
                    modules.add(new Tuple<ModuleData, NBTTagCompound>(module, moduleCompound));
                }
            }
            return modules;
        }
        return null;

    }


    private ModuleDataItemHandler(){}

    public static List<ItemStack> getModularItems(ItemStack vehicle) {
        List<Tuple<ModuleData, NBTTagCompound>> modules = getModulesAndCompoundsFromItem(vehicle);
        List<ItemStack> items = new ArrayList<ItemStack>();
        for (Tuple<ModuleData, NBTTagCompound> module : modules) {
            ModuleData moduleData = module.getFirstObject();
            NBTTagCompound compound = module.getSecondObject();

            ItemStack item = new ItemStack(ModItems.modules, 1, ModuleRegistry.getIdFromModule(moduleData));
            if (moduleData.hasExtraData() && compound != null) {
                NBTTagCompound moduleCompound = (NBTTagCompound)compound.copy();
                moduleCompound.removeTag(VehicleBase.NBT_ID);
                NBTTagCompound itemCompound = new NBTTagCompound();
                item.setTagCompound(itemCompound);
                itemCompound.setTag(ModuleData.NBT_MODULE_EXTRA_DATA, moduleCompound);
            }

            items.add(item);
        }

        return items;
    }

    public static boolean hasModules(ItemStack vehicle) {
        NBTTagCompound compound = vehicle.getTagCompound();
        return compound != null && compound.hasKey(VehicleBase.NBT_MODULES);
    }
}
