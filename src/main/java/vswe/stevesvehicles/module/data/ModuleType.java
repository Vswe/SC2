package vswe.stevesvehicles.module.data;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.entry.info.LocalizationCategory;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.common.engine.ModuleEngine;
import vswe.stevesvehicles.module.common.addon.ModuleAddon;
import vswe.stevesvehicles.module.common.hull.ModuleHull;
import vswe.stevesvehicles.module.cart.attachment.ModuleAttachment;
import vswe.stevesvehicles.module.common.storage.ModuleStorage;
import vswe.stevesvehicles.module.cart.tool.ModuleTool;

public enum  ModuleType {
    HULL(ModuleHull.class, LocalizationCategory.HULL),
    ENGINE(ModuleEngine.class, LocalizationCategory.ENGINE),
    TOOL(ModuleTool.class, LocalizationCategory.TOOL),
    ATTACHMENT(ModuleAttachment.class, LocalizationCategory.ATTACHMENT),
    STORAGE(ModuleStorage.class, LocalizationCategory.STORAGE),
    ADDON(ModuleAddon.class, LocalizationCategory.ADDON),
    INVALID(ModuleBase.class, LocalizationCategory.INVALID);


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
