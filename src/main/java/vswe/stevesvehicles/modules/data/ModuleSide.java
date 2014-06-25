package vswe.stevesvehicles.modules.data;

import vswe.stevesvehicles.old.Helpers.Localization;

public enum ModuleSide {
    NONE(Localization.MODULE_INFO.SIDE_NONE),
    TOP(Localization.MODULE_INFO.SIDE_TOP),
    CENTER(Localization.MODULE_INFO.SIDE_CENTER),
    BOTTOM(Localization.MODULE_INFO.SIDE_BOTTOM),
    BACK(Localization.MODULE_INFO.SIDE_BACK),
    LEFT(Localization.MODULE_INFO.SIDE_LEFT),
    RIGHT(Localization.MODULE_INFO.SIDE_RIGHT),
    FRONT(Localization.MODULE_INFO.SIDE_FRONT);

    private Localization.MODULE_INFO name;
    private ModuleSide(Localization.MODULE_INFO name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name.translate();
    }

}
