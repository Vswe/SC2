package vswe.stevesvehicles.localization.entry.gui.module;

import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextAdvanced;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationTank {
    public static final ILocalizedText CREATIVE_MODE = createAdvanced("creative_tank.mode");
    public static final ILocalizedText CREATIVE_CHANGE_MODE = createSimple("creative_tank.change_mode");
    public static final ILocalizedText CREATIVE_RESET_MODE = createSimple("creative_tank.reset_mode");
    public static final ILocalizedText LOCKED = createSimple("common.locked");
    public static final ILocalizedText LOCK = createSimple("common.lock");
    public static final ILocalizedText UNLOCK = createSimple("common.unlock");
    public static final ILocalizedText EMPTY = createSimple("common.empty");
    public static final ILocalizedText INVALID_FLUID = createSimple("common.invalid_fluid");

    private static final String HEADER = "steves_vehicles:gui.common.tank:";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }
    private static ILocalizedText createAdvanced(String code) {
        return new LocalizedTextAdvanced(HEADER + code);
    }

    private LocalizationTank() {}
}
