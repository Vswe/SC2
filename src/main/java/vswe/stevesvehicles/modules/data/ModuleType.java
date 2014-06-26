package vswe.stevesvehicles.modules.data;


import vswe.stevesvehicles.modules.ModuleBase;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Modules.Addons.ModuleAddon;
import vswe.stevesvehicles.modules.hull.ModuleHull;
import vswe.stevesvehicles.old.Modules.ModuleAttachment;
import vswe.stevesvehicles.old.Modules.Storages.ModuleStorage;
import vswe.stevesvehicles.old.Modules.Workers.Tools.ModuleTool;

public enum  ModuleType {
    HULL(ModuleHull.class, Localization.MODULE_INFO.HULL_CATEGORY),
    ENGINE(ModuleHull.class, Localization.MODULE_INFO.ENGINE_CATEGORY),
    TOOL(ModuleTool.class, Localization.MODULE_INFO.TOOL_CATEGORY),
    ATTACHMENT(ModuleAttachment.class, Localization.MODULE_INFO.ATTACHMENT_CATEGORY),
    STORAGE(ModuleStorage.class, Localization.MODULE_INFO.STORAGE_CATEGORY),
    ADDON(ModuleAddon.class, Localization.MODULE_INFO.ADDON_CATEGORY),
    INVALID(ModuleBase.class, Localization.MODULE_INFO.INVALID_CATEGORY);


    private Class<? extends ModuleBase> clazz;
    private Localization.MODULE_INFO name;

    ModuleType(Class<? extends ModuleBase> clazz, Localization.MODULE_INFO name) {
        this.clazz = clazz;
        this.name = name;
    }

    public Class<? extends ModuleBase> getClazz() {
        return clazz;
    }

    public String getName() {
        return name.translate();
    }
}
