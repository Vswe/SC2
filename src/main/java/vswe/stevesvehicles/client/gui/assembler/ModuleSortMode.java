package vswe.stevesvehicles.client.gui.assembler;


import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.container.slots.SlotAssembler;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.module.data.ModuleDataGroup;
import vswe.stevesvehicles.module.data.ModuleDataHull;
import vswe.stevesvehicles.module.data.ModuleDataItemHandler;
import vswe.stevesvehicles.module.data.ModuleSide;
import vswe.stevesvehicles.tileentity.TileEntityCartAssembler;

import java.util.ArrayList;
import java.util.List;

public enum ModuleSortMode {
    RELAXED {
        @Override
        public boolean isValid(TileEntityCartAssembler assembler, ModuleDataHull hull, ModuleData moduleData) {
            return true;
        }
    },
    NORMAL {
        @Override
        public boolean isValid(TileEntityCartAssembler assembler, ModuleDataHull hull, ModuleData moduleData) {
            moduleRecursiveCache.clear();
            return doesModuleDataFit(assembler, moduleData) && isModuleValid(hull, moduleData);
        }

        private List<ModuleData> moduleRecursiveCache = new ArrayList<ModuleData>();

        private boolean isModuleValid(ModuleDataHull hull, ModuleData module) {
            if (module.getCost() > hull.getComplexityMax()) {
                return false;
            }

            if (hull.getSides() != null && module.getSides() != null) {
                for (ModuleSide side : hull.getSides()) {
                    if (module.getSides().contains(side)) {
                        return false;
                    }
                }
            }

            try {
                if (moduleRecursiveCache.contains(module)) {
                    return true;
                }
                moduleRecursiveCache.add(module);

                if (module.getParent() != null && ! isModuleValid(hull, module.getParent())) {
                    return false;
                }else if(module.getRequirement() != null) {
                    for (ModuleDataGroup moduleDataGroup : module.getRequirement()) {
                        boolean isAnyValid = false;

                        for (ModuleData moduleData : moduleDataGroup.getModules()) {
                            if (isModuleValid(hull, moduleData)) {
                                isAnyValid = true;
                                break;
                            }
                        }

                        if (!isAnyValid) {
                            return false;
                        }
                    }
                }

                return true;
            }finally {
                moduleRecursiveCache.remove(module) ;
            }
        }
    },
    STRICT {
        @Override
        public boolean isValid(TileEntityCartAssembler assembler, ModuleDataHull hull, ModuleData moduleData) {
            if (!doesModuleDataFit(assembler, moduleData)) {
                return false;
            }

            List<ModuleData> modules = new ArrayList<ModuleData>();
            modules.addAll(assembler.getModules(false));
            modules.add(moduleData);
            return ModuleDataItemHandler.checkForErrors(hull.getVehicle(), hull, modules) == null;
        }
    };


    private static boolean doesModuleDataFit(TileEntityCartAssembler assembler, ModuleData module) {
        ItemStack item = module.getItemStack();
        for (SlotAssembler slot : assembler.getSlots()) {
            if (!slot.getHasStack() && slot.isItemValid(item)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public String toString() {
        return super.toString().charAt(0) + super.toString().toLowerCase().substring(1); //TODO localize
    }

    public abstract boolean isValid(TileEntityCartAssembler assembler, ModuleDataHull hull, ModuleData moduleData);


}
