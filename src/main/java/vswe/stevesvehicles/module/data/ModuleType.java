package vswe.stevesvehicles.module.data;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.common.engine.ModuleEngine;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.module.common.addon.ModuleAddon;
import vswe.stevesvehicles.module.cart.hull.ModuleHull;
import vswe.stevesvehicles.module.cart.attachment.ModuleAttachment;
import vswe.stevesvehicles.module.common.storage.ModuleStorage;
import vswe.stevesvehicles.module.cart.tool.ModuleTool;

public enum  ModuleType {
    HULL(ModuleHull.class, Localization.MODULE_INFO.HULL_CATEGORY),
    ENGINE(ModuleEngine.class, Localization.MODULE_INFO.ENGINE_CATEGORY),
    TOOL(ModuleTool.class, Localization.MODULE_INFO.TOOL_CATEGORY),
    ATTACHMENT(ModuleAttachment.class, Localization.MODULE_INFO.ATTACHMENT_CATEGORY),
    STORAGE(ModuleStorage.class, Localization.MODULE_INFO.STORAGE_CATEGORY),
    ADDON(ModuleAddon.class, Localization.MODULE_INFO.ADDON_CATEGORY),
    INVALID(ModuleBase.class, Localization.MODULE_INFO.INVALID_CATEGORY);


    private Class<? extends ModuleBase> clazz;
    private ILocalizedText name;

    ModuleType(Class<? extends ModuleBase> clazz, ILocalizedText name) {
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
