package vswe.stevesvehicles.modules.data;

import vswe.stevesvehicles.modules.ModuleBase;
import vswe.stevesvehicles.old.Helpers.ColorHelper;
import vswe.stevesvehicles.old.Helpers.Localization;

import java.util.List;


public class ModuleDataHull extends ModuleData {

    private int modularCapacity;
    private int engineMaxCount;
    private int addonMaxCount;
    private int complexityMax;

    public ModuleDataHull(String unlocalizedName, Class<? extends ModuleBase> moduleClass, int modularCapacity, int engineMaxCount, int addonMaxCount, int complexityMax) {
        super(unlocalizedName, moduleClass, 0);
        this.modularCapacity = modularCapacity;
        this.engineMaxCount = engineMaxCount;
        this.addonMaxCount = addonMaxCount;
        this.complexityMax = complexityMax;
    }

    public int getModularCapacity() {
        return modularCapacity;
    }
    public int getEngineMaxCount() {
        return engineMaxCount;
    }
    public int getAddonMaxCount() {
        return addonMaxCount;
    }
    public int getComplexityMax() {
        return complexityMax;
    }

    @Override
    public void addSpecificInformation(List<String> list) {
        list.add(ColorHelper.YELLOW + Localization.MODULE_INFO.MODULAR_CAPACITY.translate(String.valueOf(modularCapacity)));
        list.add(ColorHelper.PURPLE + Localization.MODULE_INFO.COMPLEXITY_CAP.translate(String.valueOf(complexityMax)));
        list.add(ColorHelper.ORANGE + Localization.MODULE_INFO.MAX_ENGINES.translate(String.valueOf(engineMaxCount)));
        list.add(ColorHelper.GREEN + Localization.MODULE_INFO.MAX_ADDONS.translate(String.valueOf(addonMaxCount)));
    }

}
