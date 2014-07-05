package vswe.stevesvehicles.localization.entry.module;


import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.LocalizedTextAdvanced;
import vswe.stevesvehicles.localization.LocalizedTextSimple;

public final class LocalizationProduction {
    public static final ILocalizedText OUTPUT = createSimple("output");
    public static final ILocalizedText SELECTION = createSimple("selection");
    public static final ILocalizedText INVALID = createSimple("invalid");
    public static final ILocalizedText CHANGE_MODE = createSimple("change_mode");
    public static final ILocalizedText NO_LIMIT = createSimple("no_limit");
    public static final ILocalizedText LIMIT = createSimple("limit");
    public static final ILocalizedText DISABLED = createSimple("disabled");
    public static final ILocalizedText CHANGE_LIMIT = createAdvanced("change_limit");
    public static final ILocalizedText CHANGE_LIMIT_TEN = createSimple("change_limit_ten");
    public static final ILocalizedText CHANGE_LIMIT_STACK = createSimple("change_limit_stack");

    private static final String HEADER = "steves_vehicles:gui.common.production:common.";
    private static ILocalizedText createSimple(String code) {
        return new LocalizedTextSimple(HEADER + code);
    }
    private static ILocalizedText createAdvanced(String code) {
        return new LocalizedTextAdvanced(HEADER + code);
    }

    private LocalizationProduction() {}
}

